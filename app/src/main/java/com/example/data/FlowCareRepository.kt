package com.example.data

import com.example.engine.SmartArrivalEngine
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object FlowCareRepository {

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "usr_001",
            fullName = "Karthik Subramanian",
            age = 32,
            email = "karthik.erode@flowcare.in",
            mobile = "+91 98427 54321",
            role = UserRole.PATIENT,
            isPriorityPassHolder = false
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    private val _hospitals = MutableStateFlow<List<Hospital>>(emptyList())
    val hospitals: StateFlow<List<Hospital>> = _hospitals.asStateFlow()

    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

    private val _appointments = MutableStateFlow<List<TokenAppointment>>(emptyList())
    val appointments: StateFlow<List<TokenAppointment>> = _appointments.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _payments = MutableStateFlow<List<PaymentRecord>>(emptyList())
    val payments: StateFlow<List<PaymentRecord>> = _payments.asStateFlow()

    private val _bookingRules = MutableStateFlow<Map<String, HospitalBookingRule>>(emptyMap())
    val bookingRules: StateFlow<Map<String, HospitalBookingRule>> = _bookingRules.asStateFlow()

    init {
        seedInitialData()
    }

    private fun seedInitialData() {
        val seededHospitals = listOf(
            Hospital(
                id = "hsp_lotus_erd",
                name = "Lotus Hospital & Research Centre",
                nameTamil = "லோட்டஸ் மருத்துவமனை & ஆராய்ச்சி மையம்",
                district = "Erode",
                address = "Poondurai Main Road, Kollampalayam, Erode - 638002",
                addressTamil = "பூந்துறை மெயின் ரோடு, கொல்லம்பாளையம், ஈரோடு - 638002",
                rating = 4.8f,
                totalReviews = 342,
                isVerified = true,
                phone = "+91 424 228 2828",
                emergencyAvailable = true,
                distanceKm = 4.2f,
                travelMinutesFromCenter = 15,
                specialties = listOf("Cardiology", "Orthopedics", "Neurology", "General Medicine", "Pediatrics"),
                totalBeds = 180,
                availableIcuBeds = 12
            ),
            Hospital(
                id = "hsp_kmch_erd",
                name = "KMCH Speciality Hospital Erode",
                nameTamil = "கே.எம்.சி.ஹெச் சிறப்பு மருத்துவமனை ஈரோடு",
                district = "Erode",
                address = "Perundurai Road, Near Collectorate, Erode - 638011",
                addressTamil = "பெருந்துறை ரோடு, ஆட்சியர் அலுவலகம் அருகில், ஈரோடு - 638011",
                rating = 4.7f,
                totalReviews = 428,
                isVerified = true,
                phone = "+91 424 225 6666",
                emergencyAvailable = true,
                distanceKm = 3.8f,
                travelMinutesFromCenter = 12,
                specialties = listOf("Cardiology", "Pediatrics", "Gastroenterology", "Oncology", "Nephrology"),
                totalBeds = 220,
                availableIcuBeds = 16
            ),
            Hospital(
                id = "hsp_gh_erd",
                name = "Erode Govt District Headquarters Hospital",
                nameTamil = "ஈரோடு அரசு மாவட்ட தலைமை மருத்துவமனை",
                district = "Erode",
                address = "Brough Road, Opp. Bus Stand, Erode - 638001",
                addressTamil = "பிரப் ரோடு, பழைய பேருந்து நிலையம் எதிரில், ஈரோடு - 638001",
                rating = 4.4f,
                totalReviews = 890,
                isVerified = true,
                phone = "+91 424 226 2307",
                emergencyAvailable = true,
                distanceKm = 1.5f,
                travelMinutesFromCenter = 8,
                specialties = listOf("General Medicine", "Pediatrics", "Orthopedics", "Obstetrics", "Emergency"),
                totalBeds = 450,
                availableIcuBeds = 24
            ),
            Hospital(
                id = "hsp_sudha_erd",
                name = "Sudha Multi-Speciality Hospital",
                nameTamil = "சுதா பல்நோக்கு மருத்துவமனை",
                district = "Erode",
                address = "Perundurai Road, Kumalan Kuttai, Erode - 638011",
                addressTamil = "பெருந்துறை ரோடு, குமலன் குட்டை, ஈரோடு - 638011",
                rating = 4.6f,
                totalReviews = 210,
                isVerified = true,
                phone = "+91 424 226 2950",
                emergencyAvailable = true,
                distanceKm = 5.1f,
                travelMinutesFromCenter = 18,
                specialties = listOf("Gynecology", "Pediatrics", "General Surgery", "Cardiology"),
                totalBeds = 120,
                availableIcuBeds = 6
            ),
            Hospital(
                id = "hsp_maruthi_erd",
                name = "Maruthi Medical Centre & Hospital",
                nameTamil = "மாருதி மருத்துவ மையம் & மருத்துவமனை",
                district = "Erode",
                address = "564, Perundurai Road, Sengodampalayam, Erode - 638012",
                addressTamil = "564, பெருந்துறை ரோடு, செங்கோடம்பாளையம், ஈரோடு - 638012",
                rating = 4.5f,
                totalReviews = 175,
                isVerified = false,
                phone = "+91 424 226 4949",
                emergencyAvailable = true,
                distanceKm = 6.4f,
                travelMinutesFromCenter = 22,
                specialties = listOf("Trauma Care", "Orthopedics", "Neurology", "General Medicine"),
                totalBeds = 100,
                availableIcuBeds = 4
            ),
            Hospital(
                id = "hsp_kmch_cbe",
                name = "Kovai Medical Center and Hospital (KMCH)",
                nameTamil = "கோவை மருத்துவ மையம் மற்றும் மருத்துவமனை",
                district = "Coimbatore",
                address = "Avinashi Road, Civil Aerodrome Post, Coimbatore - 641014",
                addressTamil = "அவிநாசி ரோடு, விமான நிலையம் அருகில், கோயம்புத்தூர் - 641014",
                rating = 4.9f,
                totalReviews = 1450,
                isVerified = true,
                phone = "+91 422 432 3800",
                emergencyAvailable = true,
                distanceKm = 85.0f,
                travelMinutesFromCenter = 95,
                specialties = listOf("Cardiology", "Organ Transplant", "Neurology", "Pediatrics"),
                totalBeds = 800,
                availableIcuBeds = 50
            ),
            Hospital(
                id = "hsp_manipal_salem",
                name = "Manipal Hospital Salem",
                nameTamil = "மணிபால் மருத்துவமனை சேலம்",
                district = "Salem",
                address = "Dalmia Board, Bangalore Highway, Salem - 636012",
                addressTamil = "டால்மியா போர்டு, பெங்களூர் நெடுஞ்சாலை, சேலம் - 636012",
                rating = 4.7f,
                totalReviews = 560,
                isVerified = true,
                phone = "+91 427 234 6600",
                emergencyAvailable = true,
                distanceKm = 62.0f,
                travelMinutesFromCenter = 75,
                specialties = listOf("Cardiology", "Orthopedics", "Oncology", "Gastroenterology"),
                totalBeds = 250,
                availableIcuBeds = 20
            ),
            Hospital(
                id = "hsp_apollo_chn",
                name = "Apollo Speciality Hospitals",
                nameTamil = "அப்போலோ சிறப்பு மருத்துவமனைகள்",
                district = "Chennai",
                address = "Greams Road, Thousand Lights, Chennai - 600006",
                addressTamil = "கிரீம்ஸ் ரோடு, ஆயிரம் விளக்கு, சென்னை - 600006",
                rating = 4.8f,
                totalReviews = 2100,
                isVerified = true,
                phone = "+91 44 2829 0200",
                emergencyAvailable = true,
                distanceKm = 390.0f,
                travelMinutesFromCenter = 360,
                specialties = listOf("Cardiology", "Neuro Surgery", "Robotic Surgery", "Oncology"),
                totalBeds = 600,
                availableIcuBeds = 45
            )
        )
        _hospitals.value = seededHospitals

        val seededDoctors = listOf(
            Doctor(
                id = "doc_karthik_lotus",
                hospitalId = "hsp_lotus_erd",
                name = "Dr. R. Karthikeyan",
                nameTamil = "டாக்டர் ஆர். கார்த்திகேயன்",
                specialty = "Cardiology",
                specialtyTamil = "இதயவியல் துறை",
                experienceYears = 18,
                qualification = "MBBS, MD, DM (Cardio)",
                roomNo = "OPD-102 (First Floor)",
                consultationFee = 450,
                rating = 4.9f,
                reviewCount = 184,
                scheduleTime = "09:00 AM - 01:30 PM",
                scheduleTimeTamil = "காலை 09:00 - மதியம் 01:30",
                status = DoctorStatus.CONSULTING,
                delayMinutes = 0,
                actualArrivalTime = "08:52 AM",
                avgConsultationMinutes = 12,
                currentlyServingToken = "FLW-ERD-101"
            ),
            Doctor(
                id = "doc_malathi_kmch",
                hospitalId = "hsp_kmch_erd",
                name = "Dr. S. Malathi",
                nameTamil = "டாக்டர் எஸ். மாலதி",
                specialty = "Pediatrics",
                specialtyTamil = "குழந்தைகள் நலம்",
                experienceYears = 14,
                qualification = "MBBS, DCH, DNB (Ped)",
                roomNo = "OPD-204 (Child Wing)",
                consultationFee = 350,
                rating = 4.8f,
                reviewCount = 132,
                scheduleTime = "09:30 AM - 02:00 PM",
                scheduleTimeTamil = "காலை 09:30 - மதியம் 02:00",
                status = DoctorStatus.ARRIVED,
                delayMinutes = 0,
                actualArrivalTime = "09:15 AM",
                avgConsultationMinutes = 10,
                currentlyServingToken = "FLW-ERD-201"
            ),
            Doctor(
                id = "doc_venkatesh_gh",
                hospitalId = "hsp_gh_erd",
                name = "Dr. K. Venkatesh",
                nameTamil = "டாக்டர் கே. வெங்கடேஷ்",
                specialty = "Orthopedics",
                specialtyTamil = "எலும்பியல் துறை",
                experienceYears = 16,
                qualification = "MBBS, MS (Ortho)",
                roomNo = "Room 14 (Ground Floor)",
                consultationFee = 0, // Government free
                rating = 4.6f,
                reviewCount = 290,
                scheduleTime = "08:30 AM - 01:00 PM",
                scheduleTimeTamil = "காலை 08:30 - மதியம் 01:00",
                status = DoctorStatus.CONSULTING,
                delayMinutes = 10,
                actualArrivalTime = "08:40 AM",
                avgConsultationMinutes = 8,
                currentlyServingToken = "FLW-GH-08"
            ),
            Doctor(
                id = "doc_priya_sudha",
                hospitalId = "hsp_sudha_erd",
                name = "Dr. Priya Soundararajan",
                nameTamil = "டாக்டர் பிரியா சௌந்தரராஜன்",
                specialty = "Gynecology",
                specialtyTamil = "மகப்பேறு & மகளிர் நலம்",
                experienceYears = 12,
                qualification = "MBBS, MS (OBG)",
                roomNo = "OPD-302",
                consultationFee = 400,
                rating = 4.7f,
                reviewCount = 98,
                scheduleTime = "10:00 AM - 02:00 PM",
                scheduleTimeTamil = "காலை 10:00 - மதியம் 02:00",
                status = DoctorStatus.ON_BREAK,
                delayMinutes = 15,
                actualArrivalTime = "09:55 AM",
                avgConsultationMinutes = 14,
                currentlyServingToken = "FLW-SUD-05"
            ),
            Doctor(
                id = "doc_anand_maruthi",
                hospitalId = "hsp_maruthi_erd",
                name = "Dr. M. Anand",
                nameTamil = "டாக்டர் எம். ஆனந்த்",
                specialty = "General Medicine",
                specialtyTamil = "பொது மருத்துவம்",
                experienceYears = 9,
                qualification = "MBBS, MD (Gen Med)",
                roomNo = "OPD-105",
                consultationFee = 300,
                rating = 4.5f,
                reviewCount = 67,
                scheduleTime = "09:00 AM - 01:00 PM",
                scheduleTimeTamil = "காலை 09:00 - மதியம் 01:00",
                status = DoctorStatus.DELAYED,
                delayMinutes = 25,
                actualArrivalTime = "09:25 AM",
                avgConsultationMinutes = 11,
                currentlyServingToken = "FLW-MRT-02"
            )
        )
        _doctors.value = seededDoctors

        val initialAppointments = listOf(
            TokenAppointment(
                id = "apt_001",
                tokenNumber = "FLW-ERD-104",
                patientId = "usr_001",
                patientName = "Karthik Subramanian",
                patientAge = 32,
                patientMobile = "+91 98427 54321",
                hospitalId = "hsp_lotus_erd",
                hospitalName = "Lotus Hospital & Research Centre",
                doctorId = "doc_karthik_lotus",
                doctorName = "Dr. R. Karthikeyan",
                doctorSpecialty = "Cardiology",
                bookingDate = "Today, Oct 27",
                timeSlot = "10:00 AM - 11:30 AM",
                status = TokenStatus.WAITING,
                queuePosition = 3,
                isPriorityPass = false,
                arrivalBufferMinutes = 15,
                userDistanceKm = 4.2f,
                userTravelMinutes = 15,
                notes = "Routine ECG & Blood Pressure follow-up",
                createdAt = "08:15 AM"
            ),
            TokenAppointment(
                id = "apt_002",
                tokenNumber = "FLW-ERD-101",
                patientId = "usr_demo_1",
                patientName = "Nagarajan V",
                patientAge = 58,
                patientMobile = "+91 94433 11223",
                hospitalId = "hsp_lotus_erd",
                hospitalName = "Lotus Hospital & Research Centre",
                doctorId = "doc_karthik_lotus",
                doctorName = "Dr. R. Karthikeyan",
                doctorSpecialty = "Cardiology",
                bookingDate = "Today, Oct 27",
                timeSlot = "09:00 AM - 09:30 AM",
                status = TokenStatus.IN_CONSULTATION,
                queuePosition = 0,
                isPriorityPass = true,
                createdAt = "07:50 AM"
            ),
            TokenAppointment(
                id = "apt_003",
                tokenNumber = "FLW-ERD-102",
                patientId = "usr_demo_2",
                patientName = "Meenakshi Sundaram",
                patientAge = 46,
                patientMobile = "+91 98940 33445",
                hospitalId = "hsp_lotus_erd",
                hospitalName = "Lotus Hospital & Research Centre",
                doctorId = "doc_karthik_lotus",
                doctorName = "Dr. R. Karthikeyan",
                doctorSpecialty = "Cardiology",
                bookingDate = "Today, Oct 27",
                timeSlot = "09:30 AM - 10:00 AM",
                status = TokenStatus.WAITING,
                queuePosition = 1,
                isPriorityPass = false,
                createdAt = "08:00 AM"
            ),
            TokenAppointment(
                id = "apt_004",
                tokenNumber = "FLW-ERD-103",
                patientId = "usr_demo_3",
                patientName = "Suresh Kumar",
                patientAge = 29,
                patientMobile = "+91 97890 66778",
                hospitalId = "hsp_lotus_erd",
                hospitalName = "Lotus Hospital & Research Centre",
                doctorId = "doc_karthik_lotus",
                doctorName = "Dr. R. Karthikeyan",
                doctorSpecialty = "Cardiology",
                bookingDate = "Today, Oct 27",
                timeSlot = "09:45 AM - 10:15 AM",
                status = TokenStatus.WAITING,
                queuePosition = 2,
                isPriorityPass = false,
                createdAt = "08:05 AM"
            ),
            TokenAppointment(
                id = "apt_005",
                tokenNumber = "FLW-ERD-201",
                patientId = "usr_demo_4",
                patientName = "Baby of Deepa (4y)",
                patientAge = 4,
                patientMobile = "+91 98421 99887",
                hospitalId = "hsp_kmch_erd",
                hospitalName = "KMCH Speciality Hospital Erode",
                doctorId = "doc_malathi_kmch",
                doctorName = "Dr. S. Malathi",
                doctorSpecialty = "Pediatrics",
                bookingDate = "Today, Oct 27",
                timeSlot = "09:30 AM - 10:00 AM",
                status = TokenStatus.WAITING,
                queuePosition = 0,
                isPriorityPass = true,
                createdAt = "08:30 AM"
            )
        )
        _appointments.value = initialAppointments

        val initialPayments = listOf(
            PaymentRecord(
                id = "pay_001",
                patientName = "Nagarajan V",
                amount = 50,
                purpose = "FlowCare Priority Pass - Fast Track",
                date = "27 Oct, 08:02 AM",
                status = "Success",
                transactionRef = "UPI/FC98421098"
            ),
            PaymentRecord(
                id = "pay_002",
                patientName = "Deepa Ramesh",
                amount = 50,
                purpose = "FlowCare Priority Pass - Fast Track",
                date = "27 Oct, 08:34 AM",
                status = "Success",
                transactionRef = "UPI/FC77823190"
            )
        )
        _payments.value = initialPayments

        _bookingRules.value = mapOf(
            "hsp_lotus_erd" to HospitalBookingRule(hospitalId = "hsp_lotus_erd", maxTokensPerHour = 6, defaultBufferMinutes = 15, emergencySlotsReserved = 2),
            "hsp_kmch_erd" to HospitalBookingRule(hospitalId = "hsp_kmch_erd", maxTokensPerHour = 7, defaultBufferMinutes = 15, emergencySlotsReserved = 3),
            "hsp_gh_erd" to HospitalBookingRule(hospitalId = "hsp_gh_erd", maxTokensPerHour = 10, defaultBufferMinutes = 10, emergencySlotsReserved = 5)
        )

        // Seed initial Care Buddy triage conversation
        _chatMessages.value = listOf(
            ChatMessage(
                id = "msg_001",
                isFromUser = false,
                messageEn = "Vanakkam! I am your FlowCare AI Care Buddy. I can recommend top hospitals and doctors in Erode & Tamil Nadu based on your symptoms, track token wait times, and help you plan your departure. How may I assist you today?",
                messageTa = "வணக்கம்! நான் உங்கள் FlowCare AI கேர் படி. அறிகுறிகளுக்கு ஏற்ப ஈரோடு மற்றும் தமிழ்நாட்டின் சிறந்த மருத்துவர்களை பரிந்துரைக்கவும், டோக்கன் காத்திருப்பு நேரத்தை கணக்கிடவும் நான் உதவுவேன். உங்களுக்கு என்ன உதவி தேவை?",
                timestamp = "09:00 AM"
            )
        )
    }

    // Switch Language
    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    // Switch Role
    fun setRole(role: UserRole, assignedHospitalId: String? = null) {
        val current = _currentUser.value
        _currentUser.value = current.copy(
            role = role,
            assignedHospitalId = assignedHospitalId ?: if (role == UserRole.RECEPTIONIST) "hsp_lotus_erd" else null
        )
    }

    // Register / Login User
    fun registerUser(fullName: String, age: Int, email: String, mobile: String) {
        _currentUser.value = UserProfile(
            id = "usr_" + UUID.randomUUID().toString().take(6),
            fullName = fullName,
            age = age,
            email = email,
            mobile = mobile,
            role = UserRole.PATIENT,
            isPriorityPassHolder = false
        )
    }

    fun purchasePriorityPass(patientName: String) {
        _currentUser.value = _currentUser.value.copy(isPriorityPassHolder = true)
        val newPayment = PaymentRecord(
            id = "pay_" + UUID.randomUUID().toString().take(6),
            patientName = patientName,
            amount = 50,
            purpose = "FlowCare Priority Pass - Fast Track",
            date = "Today, Just now",
            status = "Success",
            transactionRef = "UPI/FC" + (10000000..99999999).random()
        )
        _payments.value = listOf(newPayment) + _payments.value
    }

    // Book a Token
    fun bookToken(
        patientName: String,
        patientAge: Int,
        patientMobile: String,
        hospitalId: String,
        doctorId: String,
        isPriorityPass: Boolean,
        arrivalBufferMinutes: Int,
        notes: String
    ): TokenAppointment {
        val hospital = _hospitals.value.find { it.id == hospitalId }
        val doctor = _doctors.value.find { it.id == doctorId }

        val hospitalCode = when (hospitalId) {
            "hsp_lotus_erd" -> "ERD"
            "hsp_kmch_erd" -> "KMCH"
            "hsp_gh_erd" -> "GH"
            "hsp_sudha_erd" -> "SUD"
            else -> "FLW"
        }

        // Count how many currently in queue for this doctor
        val existingDoctorTokens = _appointments.value.filter {
            it.doctorId == doctorId && (it.status == TokenStatus.WAITING || it.status == TokenStatus.IN_CONSULTATION)
        }
        val tokenNum = "FLW-$hospitalCode-" + (100 + existingDoctorTokens.size + 1)
        val queuePos = existingDoctorTokens.count { it.status == TokenStatus.WAITING }

        val newApt = TokenAppointment(
            id = "apt_" + UUID.randomUUID().toString().take(8),
            tokenNumber = tokenNum,
            patientId = _currentUser.value.id,
            patientName = patientName,
            patientAge = patientAge,
            patientMobile = patientMobile,
            hospitalId = hospitalId,
            hospitalName = hospital?.name ?: "Erode Hospital",
            doctorId = doctorId,
            doctorName = doctor?.name ?: "Consultant Doctor",
            doctorSpecialty = doctor?.specialty ?: "General",
            bookingDate = "Today, Oct 27",
            timeSlot = "10:30 AM - 12:00 PM",
            status = TokenStatus.WAITING,
            queuePosition = queuePos,
            isPriorityPass = isPriorityPass,
            arrivalBufferMinutes = arrivalBufferMinutes,
            userDistanceKm = hospital?.distanceKm ?: 4.5f,
            userTravelMinutes = hospital?.travelMinutesFromCenter ?: 18,
            notes = notes,
            createdAt = "Just now"
        )

        _appointments.value = _appointments.value + newApt
        return newApt
    }

    fun cancelAppointment(appointmentId: String) {
        val target = _appointments.value.find { it.id == appointmentId } ?: return
        val docId = target.doctorId

        _appointments.value = _appointments.value.map { apt ->
            if (apt.id == appointmentId) {
                apt.copy(status = TokenStatus.CANCELLED)
            } else apt
        }
        recalculateQueueForDoctor(docId)
    }

    // Receptionist Actions
    fun receptionistCallNext(doctorId: String) {
        val list = _appointments.value
        // Current in consultation becomes completed
        val currentInRoom = list.find { it.doctorId == doctorId && it.status == TokenStatus.IN_CONSULTATION }
        // Next waiting
        val nextWaiting = list.filter { it.doctorId == doctorId && it.status == TokenStatus.WAITING }
            .minByOrNull { it.queuePosition }

        _appointments.value = list.map { apt ->
            when {
                apt.id == currentInRoom?.id -> apt.copy(status = TokenStatus.COMPLETED)
                apt.id == nextWaiting?.id -> apt.copy(status = TokenStatus.IN_CONSULTATION, queuePosition = 0)
                else -> apt
            }
        }

        // Update doctor currently serving token
        _doctors.value = _doctors.value.map { doc ->
            if (doc.id == doctorId) {
                doc.copy(
                    status = DoctorStatus.CONSULTING,
                    currentlyServingToken = nextWaiting?.tokenNumber ?: "None"
                )
            } else doc
        }

        recalculateQueueForDoctor(doctorId)
    }

    fun receptionistUpdateTokenStatus(appointmentId: String, newStatus: TokenStatus) {
        val target = _appointments.value.find { it.id == appointmentId } ?: return
        val docId = target.doctorId

        _appointments.value = _appointments.value.map {
            if (it.id == appointmentId) it.copy(status = newStatus) else it
        }

        recalculateQueueForDoctor(docId)
    }

    fun receptionistRegisterWalkIn(
        patientName: String,
        patientAge: Int,
        patientMobile: String,
        hospitalId: String,
        doctorId: String
    ): TokenAppointment {
        val hospital = _hospitals.value.find { it.id == hospitalId }
        val doctor = _doctors.value.find { it.id == doctorId }
        val hospitalCode = "WLK"
        val existingDoctorTokens = _appointments.value.filter {
            it.doctorId == doctorId && (it.status == TokenStatus.WAITING || it.status == TokenStatus.IN_CONSULTATION)
        }
        val tokenNum = "FLW-$hospitalCode-" + (200 + existingDoctorTokens.size + 1)
        val queuePos = existingDoctorTokens.count { it.status == TokenStatus.WAITING }

        val walkInApt = TokenAppointment(
            id = "apt_" + UUID.randomUUID().toString().take(8),
            tokenNumber = tokenNum,
            patientId = "usr_walkin_" + UUID.randomUUID().toString().take(4),
            patientName = patientName,
            patientAge = patientAge,
            patientMobile = patientMobile,
            hospitalId = hospitalId,
            hospitalName = hospital?.name ?: "Erode Hospital",
            doctorId = doctorId,
            doctorName = doctor?.name ?: "Doctor",
            doctorSpecialty = doctor?.specialty ?: "General",
            bookingDate = "Today, Oct 27",
            timeSlot = "Walk-in Queue",
            status = TokenStatus.WAITING,
            queuePosition = queuePos,
            isPriorityPass = false,
            isWalkIn = true,
            createdAt = "Just now"
        )

        _appointments.value = _appointments.value + walkInApt
        return walkInApt
    }

    fun receptionistUpdateDoctorStatus(
        doctorId: String,
        status: DoctorStatus,
        delayMinutes: Int,
        actualArrivalTime: String? = null,
        actualDepartureTime: String? = null
    ) {
        _doctors.value = _doctors.value.map { doc ->
            if (doc.id == doctorId) {
                doc.copy(
                    status = status,
                    delayMinutes = delayMinutes,
                    actualArrivalTime = actualArrivalTime ?: doc.actualArrivalTime,
                    actualDepartureTime = actualDepartureTime ?: doc.actualDepartureTime
                )
            } else doc
        }
        recalculateQueueForDoctor(doctorId)
    }

    private fun recalculateQueueForDoctor(doctorId: String) {
        val waiting = _appointments.value
            .filter { it.doctorId == doctorId && it.status == TokenStatus.WAITING }
            .sortedBy { if (it.isPriorityPass) 0 else 1 } // Priority pass prioritizes

        var currentPos = 1
        val updatedMap = waiting.associate { it.id to currentPos++ }

        _appointments.value = _appointments.value.map { apt ->
            if (apt.doctorId == doctorId && apt.status == TokenStatus.WAITING) {
                apt.copy(queuePosition = updatedMap[apt.id] ?: apt.queuePosition)
            } else apt
        }
    }

    // Admin Actions
    fun adminAddHospital(hospital: Hospital) {
        _hospitals.value = _hospitals.value + hospital
    }

    fun adminToggleVerifyHospital(hospitalId: String) {
        _hospitals.value = _hospitals.value.map {
            if (it.id == hospitalId) it.copy(isVerified = !it.isVerified) else it
        }
    }

    fun adminAddDoctor(doctor: Doctor) {
        _doctors.value = _doctors.value + doctor
    }

    fun adminImportSampleCsv() {
        val importedHospitals = listOf(
            Hospital(
                id = "hsp_erode_trust",
                name = "Erode Cancer Centre & Multi-Speciality",
                nameTamil = "ஈரோடு புற்றுநோய் மையம்",
                district = "Erode",
                address = "Thindal, Erode - 638012",
                addressTamil = "திண்டல், ஈரோடு - 638012",
                rating = 4.7f,
                totalReviews = 110,
                isVerified = true,
                phone = "+91 424 227 1234",
                emergencyAvailable = true,
                distanceKm = 7.0f,
                travelMinutesFromCenter = 20,
                specialties = listOf("Oncology", "Radiology", "General Medicine")
            ),
            Hospital(
                id = "hsp_perundurai_irt",
                name = "Government Medical College Hospital, Perundurai",
                nameTamil = "அரசு மருத்துவக் கல்லூரி மருத்துவமனை, பெருந்துறை",
                district = "Erode",
                address = "Sanatorium, Perundurai, Erode - 638053",
                addressTamil = "சானடோரியம், பெருந்துறை, ஈரோடு - 638053",
                rating = 4.5f,
                totalReviews = 490,
                isVerified = true,
                phone = "+91 4294 220 910",
                emergencyAvailable = true,
                distanceKm = 18.0f,
                travelMinutesFromCenter = 30,
                specialties = listOf("Pulmonology", "Chest Medicine", "General Surgery", "Orthopedics")
            )
        )
        _hospitals.value = _hospitals.value + importedHospitals
    }

    // Care Buddy Chat Triage Logic
    fun sendChatMessage(userText: String) {
        val userMsgId = "msg_" + UUID.randomUUID().toString().take(6)
        val botMsgId = "msg_" + UUID.randomUUID().toString().take(6)

        val userMessage = ChatMessage(
            id = userMsgId,
            isFromUser = true,
            messageEn = userText,
            messageTa = userText,
            timestamp = "Just now"
        )

        // Smart triage analysis
        val lower = userText.lowercase()
        val botResponse: ChatMessage = when {
            lower.contains("heart") || lower.contains("chest") || lower.contains("இதயம்") || lower.contains("மார்பு") -> {
                ChatMessage(
                    id = botMsgId,
                    isFromUser = false,
                    messageEn = "⚠️ Chest discomfort or cardiac symptoms need urgent evaluation. In Erode, Dr. R. Karthikeyan (DM Cardiology) is currently available at Lotus Hospital & Research Centre. Estimated queue wait: ~25 mins. Emergency Helpline: 108.",
                    messageTa = "⚠️ மார்பு வலி அல்லது இதய உபாதைகளுக்கு அவசர பரிசோதனை அவசியம். ஈரோட்டில், டாக்டர் ஆர். கார்த்திகேயன் (இதயவியல்) லோட்டஸ் மருத்துவமனையில் தற்போது இருக்கிறார். காத்திருப்பு நேரம்: ~25 நிமிடம்.",
                    timestamp = "Just now",
                    actionHospitalId = "hsp_lotus_erd",
                    actionDoctorId = "doc_karthik_lotus",
                    actionSpecialty = "Cardiology"
                )
            }
            lower.contains("fever") || lower.contains("child") || lower.contains("baby") || lower.contains("குழந்தை") || lower.contains("காய்ச்சல்") -> {
                ChatMessage(
                    id = botMsgId,
                    isFromUser = false,
                    messageEn = "For pediatric care or child fever, Dr. S. Malathi (DCH Pediatrics) at KMCH Speciality Hospital, Erode has verified arrival. Token queue is moving fast (~10m per patient).",
                    messageTa = "குழந்தைகள் நலம் மற்றும் காய்ச்சலுக்கு, கே.எம்.சி.ஹெச் மருத்துவமனையில் டாக்டர் எஸ். மாலதி அறையில் உள்ளார். வரிசை விரைவாக நகர்கிறது.",
                    timestamp = "Just now",
                    actionHospitalId = "hsp_kmch_erd",
                    actionDoctorId = "doc_malathi_kmch",
                    actionSpecialty = "Pediatrics"
                )
            }
            lower.contains("bone") || lower.contains("fracture") || lower.contains("knee") || lower.contains("எலும்பு") || lower.contains("முறிவு") -> {
                ChatMessage(
                    id = botMsgId,
                    isFromUser = false,
                    messageEn = "For orthopedic and joint care, Dr. K. Venkatesh at Erode Government District Headquarters Hospital is consulting. Zero consultation charges (Free Government OP).",
                    messageTa = "எலும்பியல் மற்றும் மூட்டு சிகிச்சைக்கு, ஈரோடு அரசு தலைமை மருத்துவமனையில் டாக்டர் கே. வெங்கடேஷ் பரிசோதனை செய்து வருகிறார். கட்டணம் இலவசம்.",
                    timestamp = "Just now",
                    actionHospitalId = "hsp_gh_erd",
                    actionDoctorId = "doc_venkatesh_gh",
                    actionSpecialty = "Orthopedics"
                )
            }
            lower.contains("token") || lower.contains("prediction") || lower.contains("formula") || lower.contains("தோக்கன்") -> {
                ChatMessage(
                    id = botMsgId,
                    isFromUser = false,
                    messageEn = "FlowCare's Smart Arrival Engine calculates: Departure Time = Predicted Consultation Time - Your Travel Time - 15m Buffer. If doctor delays, your recommended departure shifts dynamically!",
                    messageTa = "FlowCare ஸ்மார்ட் வருகை கணக்கீடு: புறப்படும் நேரம் = கணிக்கப்பட்ட பரிசோதனை நேரம் - பயண நேரம் - 15 நிமிடம் முன்னெச்சரிக்கை இடைவெளி.",
                    timestamp = "Just now"
                )
            }
            else -> {
                ChatMessage(
                    id = botMsgId,
                    isFromUser = false,
                    messageEn = "I can guide you to verified specialists across Erode district, check current live tokens, or book a fast-track appointment. What symptoms are you experiencing?",
                    messageTa = "ஈரோடு மாவட்டத்தின் சிறந்த மருத்துவர்கள், நேரடி டோக்கன் நிலை மற்றும் முன்பதிவு செய்ய நான் உதவுகிறேன். தங்களுக்கு என்ன சிகிச்சை தேவை?",
                    timestamp = "Just now"
                )
            }
        }

        _chatMessages.value = _chatMessages.value + userMessage + botResponse
    }
}
