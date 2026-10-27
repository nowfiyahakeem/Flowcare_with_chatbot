package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceptionistScreen(
    hospitals: List<Hospital>,
    doctors: List<Doctor>,
    appointments: List<TokenAppointment>,
    language: AppLanguage,
    onCallNext: (doctorId: String) -> Unit,
    onUpdateTokenStatus: (appointmentId: String, newStatus: TokenStatus) -> Unit,
    onRegisterWalkIn: (name: String, age: Int, mobile: String, hospitalId: String, doctorId: String) -> Unit,
    onUpdateDoctorStatus: (doctorId: String, status: DoctorStatus, delayMinutes: Int, checkIn: String?, checkOut: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedHospitalId by remember { mutableStateOf(hospitals.firstOrNull()?.id ?: "hsp_lotus_erd") }
    var selectedDoctorId by remember { mutableStateOf(doctors.firstOrNull { it.hospitalId == selectedHospitalId }?.id ?: "") }
    var showWalkInDialog by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    var statusFilter by remember { mutableStateOf<TokenStatus?>(null) }

    val hospital = hospitals.find { it.id == selectedHospitalId }
    val hospitalDoctors = doctors.filter { it.hospitalId == selectedHospitalId }
    val currentDoctor = doctors.find { it.id == selectedDoctorId } ?: hospitalDoctors.firstOrNull()

    // Filter appointments for this hospital and optionally doctor
    val hospitalAppointments = appointments.filter { it.hospitalId == selectedHospitalId }
    val filteredAppointments = hospitalAppointments.filter { apt ->
        (currentDoctor == null || apt.doctorId == currentDoctor.id) &&
                (statusFilter == null || apt.status == statusFilter)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CharcoalBlack)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        // Header & Hospital Selector
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "OPD Reception Desk" else "OPD வரவேற்பு பலகை",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                    Text(
                        text = hospital?.name ?: "Erode Medical Center",
                        color = FlowTealLight,
                        fontSize = 12.sp
                    )
                }

                // Walk-in patient button
                Button(
                    onClick = { showWalkInDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = FlowTeal),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("register_walkin_btn")
                ) {
                    Icon(Icons.Filled.PersonAdd, contentDescription = null, tint = CharcoalBlack, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Walk-In" else "நேரடி நோயாளி",
                        color = CharcoalBlack,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Hospital Switcher Tabs
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(hospitals.filter { it.district == "Erode" }) { hsp ->
                    val isSel = hsp.id == selectedHospitalId
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSel) FlowTealContainer else CharcoalCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) FlowTeal else CharcoalBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                selectedHospitalId = hsp.id
                                val firstDoc = doctors.firstOrNull { it.hospitalId == hsp.id }
                                if (firstDoc != null) selectedDoctorId = firstDoc.id
                            }
                    ) {
                        Text(
                            text = hsp.name.split(" ").take(2).joinToString(" "),
                            color = if (isSel) FlowTealLight else PureWhite,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Doctor OPD Live Control Panel
        item {
            currentDoctor?.let { doc ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CharcoalCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) doc.name else doc.nameTamil,
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "${doc.specialty} • ${doc.roomNo}",
                                    color = LightGrey,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Checked In: ${doc.actualArrivalTime}",
                                    color = FlowTealLight,
                                    fontSize = 11.sp
                                )
                            }

                            StatusBadge(status = doc.status, language = language)
                        }

                        // Call Next Patient Hero Button
                        Button(
                            onClick = {
                                onCallNext(doc.id)
                                snackbarMessage = "Called next token for ${doc.name}!"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("call_next_patient_btn")
                        ) {
                            Icon(Icons.Filled.Campaign, contentDescription = null, tint = PureWhite, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Call Next Patient to Room" else "அடுத்த நோயாளியை அழைக்கவும்",
                                color = PureWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Divider(color = CharcoalBorder, thickness = 0.5.dp)

                        // Doctor Attendance Status Switcher
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Update Doctor Live Status:" else "மருத்துவர் நிலை மாற்றம்:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LightGrey
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(DoctorStatus.values()) { status ->
                                val isSelected = doc.status == status
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) FlowTeal else CharcoalSurface,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            onUpdateDoctorStatus(doc.id, status, doc.delayMinutes, null, null)
                                            snackbarMessage = "Doctor status updated to ${status.labelEn}"
                                        }
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ENGLISH) status.labelEn else status.labelTa,
                                        color = if (isSelected) CharcoalBlack else PureWhite,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        // Report Delay Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Add Delay:" else "தாமதம் அறிவிப்பு:",
                                color = LightGrey,
                                fontSize = 12.sp
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(0, 10, 20, 30).forEach { mins ->
                                    val isCurrentDelay = doc.delayMinutes == mins
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (isCurrentDelay) StatusDanger else CharcoalSurface,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .clickable {
                                                val newStatus = if (mins > 0) DoctorStatus.DELAYED else DoctorStatus.ARRIVED
                                                onUpdateDoctorStatus(doc.id, newStatus, mins, null, null)
                                                snackbarMessage = if (mins > 0) "Reported $mins min delay. Arrival predictions recalculated." else "Delay cleared."
                                            }
                                    ) {
                                        Text(
                                            text = if (mins == 0) "None" else "+${mins}m",
                                            color = PureWhite,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Daily Queue Summary Analytics
        item {
            val waitingCount = hospitalAppointments.count { it.status == TokenStatus.WAITING }
            val completedCount = hospitalAppointments.count { it.status == TokenStatus.COMPLETED }
            val inRoomCount = hospitalAppointments.count { it.status == TokenStatus.IN_CONSULTATION }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = CharcoalCard,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Waiting", color = LightGrey, fontSize = 11.sp)
                        Text(text = "$waitingCount", color = FlowTeal, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }

                Surface(
                    color = CharcoalCard,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "In Room", color = LightGrey, fontSize = 11.sp)
                        Text(text = "$inRoomCount", color = StatusSuccess, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }

                Surface(
                    color = CharcoalCard,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Completed", color = LightGrey, fontSize = 11.sp)
                        Text(text = "$completedCount", color = StatusInfo, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }
        }

        // Today's Token List Header
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Queue Roster (${filteredAppointments.size} Patients)" else "இன்றைய வரிசை பட்டியல்",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PureWhite
            )
        }

        items(filteredAppointments) { apt ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CharcoalCard),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (apt.status == TokenStatus.IN_CONSULTATION) StatusSuccess else CharcoalBorder
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = apt.tokenNumber,
                                fontWeight = FontWeight.Black,
                                color = if (apt.status == TokenStatus.IN_CONSULTATION) StatusSuccess else FlowTeal,
                                fontSize = 14.sp
                            )
                            if (apt.isPriorityPass) {
                                Surface(
                                    color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "FAST",
                                        color = Color(0xFFF59E0B),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            if (apt.isWalkIn) {
                                Surface(
                                    color = FlowTeal.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "WALK-IN",
                                        color = FlowTeal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "${apt.patientName} (${apt.patientAge}y) • ${apt.patientMobile}",
                            color = PureWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Status: ${apt.status.labelEn} • Pos: ${if (apt.status == TokenStatus.IN_CONSULTATION) "Now in Room" else "#${apt.queuePosition}"}",
                            color = LightGrey,
                            fontSize = 11.sp
                        )
                    }

                    // Action buttons: Complete / No-Show
                    if (apt.status == TokenStatus.WAITING || apt.status == TokenStatus.IN_CONSULTATION) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = { onUpdateTokenStatus(apt.id, TokenStatus.COMPLETED) },
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(StatusSuccess.copy(alpha = 0.2f))
                            ) {
                                Icon(Icons.Filled.Check, contentDescription = "Complete", tint = StatusSuccess, modifier = Modifier.size(18.dp))
                            }

                            IconButton(
                                onClick = { onUpdateTokenStatus(apt.id, TokenStatus.NO_SHOW) },
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(StatusDanger.copy(alpha = 0.2f))
                            ) {
                                Icon(Icons.Filled.Close, contentDescription = "No Show", tint = StatusDanger, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Walk-in Dialog
    if (showWalkInDialog) {
        var wName by remember { mutableStateOf("") }
        var wAge by remember { mutableStateOf("") }
        var wMobile by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showWalkInDialog = false },
            containerColor = CharcoalCard,
            title = {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Register Walk-In Patient" else "நேரடி நோயாளி பதிவு",
                    color = PureWhite,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = wName,
                        onValueChange = { wName = it },
                        label = { Text("Patient Name") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FlowTeal,
                            unfocusedBorderColor = CharcoalBorder,
                            focusedTextColor = PureWhite,
                            unfocusedTextColor = PureWhite
                        )
                    )
                    OutlinedTextField(
                        value = wAge,
                        onValueChange = { wAge = it.filter { char -> char.isDigit() } },
                        label = { Text("Age") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FlowTeal,
                            unfocusedBorderColor = CharcoalBorder,
                            focusedTextColor = PureWhite,
                            unfocusedTextColor = PureWhite
                        )
                    )
                    OutlinedTextField(
                        value = wMobile,
                        onValueChange = { wMobile = it },
                        label = { Text("Mobile Number") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FlowTeal,
                            unfocusedBorderColor = CharcoalBorder,
                            focusedTextColor = PureWhite,
                            unfocusedTextColor = PureWhite
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (wName.isNotBlank() && currentDoctor != null) {
                            val age = wAge.toIntOrNull() ?: 30
                            onRegisterWalkIn(wName, age, wMobile, selectedHospitalId, currentDoctor.id)
                            showWalkInDialog = false
                            snackbarMessage = "Walk-in patient registered with immediate token!"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FlowTeal)
                ) {
                    Text("Issue Token", color = CharcoalBlack, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWalkInDialog = false }) {
                    Text("Cancel", color = LightGrey)
                }
            }
        )
    }
}
