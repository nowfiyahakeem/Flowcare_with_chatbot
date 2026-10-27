package com.example.engine

import com.example.model.ArrivalPrediction
import com.example.model.Doctor
import com.example.model.DoctorStatus
import java.util.Calendar
import java.util.Locale

object SmartArrivalEngine {

    /**
     * Calculates the estimated consultation time and recommended departure time
     * based on live queue position, doctor attendance, current delays, travel time, and buffer.
     */
    fun calculate(
        doctor: Doctor,
        queuePosition: Int, // 0 means currently being served or next immediately
        userTravelMinutes: Int,
        arrivalBufferMinutes: Int
    ): ArrivalPrediction {
        val now = Calendar.getInstance()
        
        // Base waiting time = queue position * average consultation duration
        val avgDuration = if (doctor.avgConsultationMinutes > 0) doctor.avgConsultationMinutes else 12
        var waitMinutes = queuePosition * avgDuration

        // Add doctor delay if reported
        if (doctor.delayMinutes > 0) {
            waitMinutes += doctor.delayMinutes
        }

        // Adjust for doctor status
        when (doctor.status) {
            DoctorStatus.ON_BREAK -> waitMinutes += 15
            DoctorStatus.DELAYED -> waitMinutes += 10
            DoctorStatus.DEPARTED -> waitMinutes += 60 // Next shift or delay
            DoctorStatus.CONSULTING -> {
                // If queue position is 0, they are in progress or immediately next
                if (queuePosition == 0) waitMinutes = 2
            }
            DoctorStatus.ARRIVED -> {
                // Doctor is in room, preparing to start
                if (queuePosition == 0) waitMinutes = 5
            }
        }

        // Calculate predicted consultation time
        val consultCalendar = Calendar.getInstance().apply {
            timeInMillis = now.timeInMillis
            add(Calendar.MINUTE, waitMinutes)
        }

        val consultTimeStr = formatTime(consultCalendar)

        // Recommended departure time = Predicted Consultation Time - travel time - arrival buffer
        val totalPrepMinutes = userTravelMinutes + arrivalBufferMinutes
        val departureCalendar = Calendar.getInstance().apply {
            timeInMillis = consultCalendar.timeInMillis
            add(Calendar.MINUTE, -totalPrepMinutes)
        }

        val departureTimeStr = formatTime(departureCalendar)

        // Confidence score algorithm:
        // Higher confidence if doctor is currently consulting and queue is small
        var confidence = when (doctor.status) {
            DoctorStatus.CONSULTING -> if (queuePosition <= 4) 95 else 89
            DoctorStatus.ARRIVED -> 88
            DoctorStatus.DELAYED -> 76
            DoctorStatus.ON_BREAK -> 81
            DoctorStatus.DEPARTED -> 65
        }

        // Formulate explanation strings
        val statusTextEn = when (doctor.status) {
            DoctorStatus.CONSULTING -> "Doctor is actively consulting (Avg ${avgDuration}m/patient)"
            DoctorStatus.ARRIVED -> "Doctor is in OPD chamber"
            DoctorStatus.ON_BREAK -> "Doctor is on a short 15m medical break"
            DoctorStatus.DELAYED -> "Doctor is delayed by ${doctor.delayMinutes} mins"
            DoctorStatus.DEPARTED -> "Doctor has completed morning shift"
        }

        val statusTextTa = when (doctor.status) {
            DoctorStatus.CONSULTING -> "மருத்துவர் பரிசோதித்து வருகிறார் (சராசரி ${avgDuration} நிமி/நோயாளி)"
            DoctorStatus.ARRIVED -> "மருத்துவர் அறையில் உள்ளார்"
            DoctorStatus.ON_BREAK -> "மருத்துவர் 15 நிமிடம் இடைவேளையில் உள்ளார்"
            DoctorStatus.DELAYED -> "மருத்துவர் ${doctor.delayMinutes} நிமிடம் தாமதமாக வருகிறார்"
            DoctorStatus.DEPARTED -> "மருத்துவர் கிளம்பிவிட்டார்"
        }

        val explanationEn = "$statusTextEn. $queuePosition patient(s) ahead. Leave home by $departureTimeStr to arrive $arrivalBufferMinutes mins prior."
        val explanationTa = "$statusTextTa. தங்களுக்கு முன் $queuePosition நபர்கள். $arrivalBufferMinutes நிமிடம் முன்னதாக அடைய $departureTimeStr மணிக்கு புறப்படவும்."

        val updatedTimeStr = formatTime(now)

        return ArrivalPrediction(
            predictedConsultationTime = consultTimeStr,
            estimatedWaitMinutes = waitMinutes,
            recommendedDepartureTime = departureTimeStr,
            travelMinutes = userTravelMinutes,
            arrivalBufferMinutes = arrivalBufferMinutes,
            confidenceScore = confidence,
            explanationEn = explanationEn,
            explanationTa = explanationTa,
            lastUpdated = updatedTimeStr
        )
    }

    private fun formatTime(calendar: Calendar): String {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val amPm = if (hour >= 12) "PM" else "AM"
        val hour12 = when (hour % 12) {
            0 -> 12
            else -> hour % 12
        }
        return String.format(Locale.getDefault(), "%02d:%02d %s", hour12, minute, amPm)
    }
}
