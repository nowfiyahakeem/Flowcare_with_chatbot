package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.FlowCareRepository
import com.example.model.*
import com.example.ui.components.BookingDialog
import com.example.ui.components.FlowCareTopAppBar
import com.example.ui.components.PaymentModal
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlowCareTheme(darkTheme = true) {
                FlowCareApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowCareApp() {
    val context = LocalContext.current

    val currentUser by FlowCareRepository.currentUser.collectAsStateWithLifecycle()
    val currentLanguage by FlowCareRepository.currentLanguage.collectAsStateWithLifecycle()
    val hospitals by FlowCareRepository.hospitals.collectAsStateWithLifecycle()
    val doctors by FlowCareRepository.doctors.collectAsStateWithLifecycle()
    val appointments by FlowCareRepository.appointments.collectAsStateWithLifecycle()
    val chatMessages by FlowCareRepository.chatMessages.collectAsStateWithLifecycle()
    val payments by FlowCareRepository.payments.collectAsStateWithLifecycle()

    var patientTab by remember { mutableStateOf("Dashboard") }
    var specialtyFilterForSearch by remember { mutableStateOf<String?>(null) }

    // Dialog & Modal States
    var selectedBookingDoctor by remember { mutableStateOf<Doctor?>(null) }
    var selectedBookingHospital by remember { mutableStateOf<Hospital?>(null) }
    var isBookingDialogOpen by remember { mutableStateOf(false) }
    var isPriorityPassModalOpen by remember { mutableStateOf(false) }

    // BackHandler for Patient tabs
    BackHandler(enabled = currentUser.role == UserRole.PATIENT && patientTab != "Dashboard") {
        patientTab = "Dashboard"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CharcoalBlack,
        topBar = {
            FlowCareTopAppBar(
                currentRole = currentUser.role,
                currentLanguage = currentLanguage,
                onRoleSelected = { role ->
                    FlowCareRepository.setRole(role)
                    Toast.makeText(
                        context,
                        "Switched to ${role.name.lowercase().replaceFirstChar { it.uppercase() }} Portal",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onLanguageToggle = {
                    val nextLang = if (currentLanguage == AppLanguage.ENGLISH) AppLanguage.TAMIL else AppLanguage.ENGLISH
                    FlowCareRepository.setLanguage(nextLang)
                }
            )
        },
        bottomBar = {
            if (currentUser.role == UserRole.PATIENT) {
                NavigationBar(
                    containerColor = CharcoalSurface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.border(0.5.dp, CharcoalBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                ) {
                    val items = listOf(
                        Triple("Dashboard", if (currentLanguage == AppLanguage.ENGLISH) "Home" else "முகப்பு", Icons.Filled.Home),
                        Triple("Hospitals", if (currentLanguage == AppLanguage.ENGLISH) "Hospitals" else "மருத்துவமனை", Icons.Filled.LocalHospital),
                        Triple("Tokens", if (currentLanguage == AppLanguage.ENGLISH) "My Queue" else "என் டோக்கன்", Icons.Filled.ConfirmationNumber),
                        Triple("CareBuddy", if (currentLanguage == AppLanguage.ENGLISH) "Care Buddy" else "கேர் படி", Icons.Filled.SmartToy)
                    )

                    items.forEach { (tabKey, label, icon) ->
                        val isSelected = patientTab == tabKey
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (tabKey == "Hospitals") specialtyFilterForSearch = null
                                patientTab = tabKey
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) CharcoalBlack else LightGrey
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
                                    color = if (isSelected) FlowTeal else LightGrey
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = FlowTeal,
                                selectedIconColor = CharcoalBlack,
                                unselectedIconColor = LightGrey
                            ),
                            modifier = Modifier.testTag("nav_item_${tabKey.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentUser.role) {
                UserRole.PATIENT -> {
                    when (patientTab) {
                        "Dashboard" -> {
                            PatientDashboardScreen(
                                currentUser = currentUser,
                                hospitals = hospitals,
                                doctors = doctors,
                                appointments = appointments,
                                language = currentLanguage,
                                onNavigateToTokens = { patientTab = "Tokens" },
                                onNavigateToHospitals = { spec ->
                                    specialtyFilterForSearch = spec
                                    patientTab = "Hospitals"
                                },
                                onOpenPriorityPass = { isPriorityPassModalOpen = true },
                                onBookDoctor = { doc, hsp ->
                                    selectedBookingDoctor = doc
                                    selectedBookingHospital = hsp
                                    isBookingDialogOpen = true
                                }
                            )
                        }

                        "Hospitals" -> {
                            HospitalSearchScreen(
                                hospitals = hospitals,
                                doctors = doctors,
                                initialSpecialty = specialtyFilterForSearch,
                                language = currentLanguage,
                                onBookDoctor = { doc, hsp ->
                                    selectedBookingDoctor = doc
                                    selectedBookingHospital = hsp
                                    isBookingDialogOpen = true
                                }
                            )
                        }

                        "Tokens" -> {
                            MyTokensScreen(
                                appointments = appointments,
                                doctors = doctors,
                                hospitals = hospitals,
                                language = currentLanguage,
                                onCancelAppointment = { aptId ->
                                    FlowCareRepository.cancelAppointment(aptId)
                                    Toast.makeText(context, "Token appointment cancelled", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        "CareBuddy" -> {
                            CareBuddyScreen(
                                chatMessages = chatMessages,
                                language = currentLanguage,
                                onSendMessage = { text ->
                                    FlowCareRepository.sendChatMessage(text)
                                },
                                onBookSuggestedDoctor = { hspId, docId ->
                                    val hsp = hospitals.find { it.id == hspId }
                                    val doc = doctors.find { it.id == docId }
                                    if (doc != null && hsp != null) {
                                        selectedBookingDoctor = doc
                                        selectedBookingHospital = hsp
                                        isBookingDialogOpen = true
                                    }
                                }
                            )
                        }
                    }
                }

                UserRole.RECEPTIONIST -> {
                    ReceptionistScreen(
                        hospitals = hospitals,
                        doctors = doctors,
                        appointments = appointments,
                        language = currentLanguage,
                        onCallNext = { docId ->
                            FlowCareRepository.receptionistCallNext(docId)
                        },
                        onUpdateTokenStatus = { aptId, newStatus ->
                            FlowCareRepository.receptionistUpdateTokenStatus(aptId, newStatus)
                        },
                        onRegisterWalkIn = { name, age, mobile, hspId, docId ->
                            val token = FlowCareRepository.receptionistRegisterWalkIn(name, age, mobile, hspId, docId)
                            Toast.makeText(context, "Issued Walk-In Token: ${token.tokenNumber}", Toast.LENGTH_LONG).show()
                        },
                        onUpdateDoctorStatus = { docId, status, delayMins, checkIn, checkOut ->
                            FlowCareRepository.receptionistUpdateDoctorStatus(docId, status, delayMins, checkIn, checkOut)
                        }
                    )
                }

                UserRole.ADMIN -> {
                    AdminScreen(
                        hospitals = hospitals,
                        doctors = doctors,
                        appointments = appointments,
                        payments = payments,
                        language = currentLanguage,
                        onToggleVerifyHospital = { hspId ->
                            FlowCareRepository.adminToggleVerifyHospital(hspId)
                        },
                        onImportSampleCsv = {
                            FlowCareRepository.adminImportSampleCsv()
                            Toast.makeText(context, "Imported Erode medical centers from CSV!", Toast.LENGTH_SHORT).show()
                        },
                        onAddHospital = { hsp ->
                            FlowCareRepository.adminAddHospital(hsp)
                        }
                    )
                }
            }

            // Booking Modal
            BookingDialog(
                isOpen = isBookingDialogOpen,
                doctor = selectedBookingDoctor,
                hospital = selectedBookingHospital,
                currentUser = currentUser,
                language = currentLanguage,
                onDismiss = { isBookingDialogOpen = false },
                onConfirmBooking = { name, age, mobile, isPriority, buffer, notes ->
                    val bookedApt = FlowCareRepository.bookToken(
                        patientName = name,
                        patientAge = age,
                        patientMobile = mobile,
                        hospitalId = selectedBookingHospital?.id ?: "",
                        doctorId = selectedBookingDoctor?.id ?: "",
                        isPriorityPass = isPriority,
                        arrivalBufferMinutes = buffer,
                        notes = notes
                    )
                    Toast.makeText(
                        context,
                        "Token Booked: ${bookedApt.tokenNumber}! Check Live Queue.",
                        Toast.LENGTH_LONG
                    ).show()
                    patientTab = "Tokens"
                }
            )

            // Priority Pass Payment Modal
            PaymentModal(
                isOpen = isPriorityPassModalOpen,
                language = currentLanguage,
                onDismiss = { isPriorityPassModalOpen = false },
                onConfirmPayment = { method ->
                    FlowCareRepository.purchasePriorityPass(currentUser.fullName)
                    Toast.makeText(
                        context,
                        "Priority Pass Activated via $method! Fast-track OP enabled.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }
}
