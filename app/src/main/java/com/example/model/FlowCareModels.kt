package com.example.model

enum class AppLanguage {
    ENGLISH, TAMIL
}

enum class UserRole {
    PATIENT, RECEPTIONIST, ADMIN
}

data class UserProfile(
    val id: String,
    val fullName: String,
    val age: Int,
    val email: String,
    val mobile: String,
    val role: UserRole,
    val assignedHospitalId: String? = null,
    val isPriorityPassHolder: Boolean = false
)

enum class DoctorStatus(val labelEn: String, val labelTa: String) {
    ARRIVED("Arrived", "வந்துவிட்டார்"),
    CONSULTING("Consulting", "பரிசோதனையில் உள்ளார்"),
    ON_BREAK("On Break", "இடைவேளையில் உள்ளார்"),
    DELAYED("Delayed", "தாமதமாகிறது"),
    DEPARTED("Departed", "சென்றுவிட்டார்")
}

data class Doctor(
    val id: String,
    val hospitalId: String,
    val name: String,
    val nameTamil: String,
    val specialty: String,
    val specialtyTamil: String,
    val experienceYears: Int,
    val qualification: String,
    val roomNo: String,
    val consultationFee: Int,
    val rating: Float,
    val reviewCount: Int,
    val scheduleTime: String,
    val scheduleTimeTamil: String,
    val status: DoctorStatus = DoctorStatus.ARRIVED,
    val delayMinutes: Int = 0,
    val actualArrivalTime: String = "08:50 AM",
    val actualDepartureTime: String? = null,
    val avgConsultationMinutes: Int = 12,
    val currentlyServingToken: String? = null
)

data class Hospital(
    val id: String,
    val name: String,
    val nameTamil: String,
    val district: String,
    val address: String,
    val addressTamil: String,
    val rating: Float,
    val totalReviews: Int,
    val isVerified: Boolean,
    val phone: String,
    val emergencyAvailable: Boolean = true,
    val ambulanceNumber: String = "108",
    val distanceKm: Float = 4.5f,
    val travelMinutesFromCenter: Int = 18,
    val specialties: List<String> = emptyList(),
    val totalBeds: Int = 150,
    val availableIcuBeds: Int = 8
)

enum class TokenStatus(val labelEn: String, val labelTa: String) {
    WAITING("Waiting in Queue", "வரிசையில் காத்திருப்பு"),
    IN_CONSULTATION("In Consultation", "பரிசோதனை நடக்கிறது"),
    COMPLETED("Completed", "முடிவடைந்தது"),
    NO_SHOW("No Show", "வரவில்லை"),
    CANCELLED("Cancelled", "ரத்து செய்யப்பட்டது")
}

data class TokenAppointment(
    val id: String,
    val tokenNumber: String,
    val patientId: String,
    val patientName: String,
    val patientAge: Int,
    val patientMobile: String,
    val hospitalId: String,
    val hospitalName: String,
    val doctorId: String,
    val doctorName: String,
    val doctorSpecialty: String,
    val bookingDate: String,
    val timeSlot: String,
    val status: TokenStatus = TokenStatus.WAITING,
    val queuePosition: Int,
    val isPriorityPass: Boolean = false,
    val arrivalBufferMinutes: Int = 15,
    val userDistanceKm: Float = 5.2f,
    val userTravelMinutes: Int = 20,
    val notes: String = "",
    val isWalkIn: Boolean = false,
    val createdAt: String = "09:30 AM"
)

data class ArrivalPrediction(
    val predictedConsultationTime: String,
    val estimatedWaitMinutes: Int,
    val recommendedDepartureTime: String,
    val travelMinutes: Int,
    val arrivalBufferMinutes: Int,
    val confidenceScore: Int,
    val explanationEn: String,
    val explanationTa: String,
    val lastUpdated: String
)

data class ChatMessage(
    val id: String,
    val isFromUser: Boolean,
    val messageEn: String,
    val messageTa: String,
    val timestamp: String,
    val actionHospitalId: String? = null,
    val actionDoctorId: String? = null,
    val actionSpecialty: String? = null
)

data class PaymentRecord(
    val id: String,
    val patientName: String,
    val amount: Int = 50,
    val purpose: String = "FlowCare Priority Pass",
    val date: String,
    val status: String = "Completed",
    val transactionRef: String
)

data class HospitalBookingRule(
    val hospitalId: String,
    val maxTokensPerHour: Int = 6,
    val defaultBufferMinutes: Int = 15,
    val emergencySlotsReserved: Int = 2
)
