package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDialog(
    isOpen: Boolean,
    doctor: Doctor?,
    hospital: Hospital?,
    currentUser: UserProfile,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onConfirmBooking: (
        patientName: String,
        patientAge: Int,
        patientMobile: String,
        isPriorityPass: Boolean,
        arrivalBufferMinutes: Int,
        notes: String
    ) -> Unit
) {
    if (!isOpen || doctor == null || hospital == null) return

    var patientName by remember { mutableStateOf(currentUser.fullName) }
    var patientAge by remember { mutableStateOf(currentUser.age.toString()) }
    var patientMobile by remember { mutableStateOf(currentUser.mobile) }
    var selectedBuffer by remember { mutableStateOf(15) }
    var isPriorityPassSelected by remember { mutableStateOf(currentUser.isPriorityPassHolder) }
    var visitNotes by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CharcoalCard,
        dragHandle = { BottomSheetDefaults.DragHandle(color = CharcoalBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Book OPD Token" else "OPD டோக்கன் முன்பதிவு",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                    Text(
                        text = hospital.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = FlowTealLight
                    )
                }

                Surface(
                    color = FlowTeal.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (doctor.consultationFee == 0) "FREE OP" else "₹${doctor.consultationFee}",
                        color = FlowTeal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Surface(
                color = CharcoalSurface,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(FlowTeal.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = FlowTeal,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) doctor.name else doctor.nameTamil,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${if (language == AppLanguage.ENGLISH) doctor.specialty else doctor.specialtyTamil} • ${doctor.roomNo}",
                            color = LightGrey,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Today: ${doctor.scheduleTime}",
                            color = MutedGrey,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            OutlinedTextField(
                value = patientName,
                onValueChange = { patientName = it },
                label = { Text(if (language == AppLanguage.ENGLISH) "Patient Full Name" else "நோயாளி பெயர்") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("patient_name_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FlowTeal,
                    unfocusedBorderColor = CharcoalBorder,
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = patientAge,
                    onValueChange = { patientAge = it.filter { char -> char.isDigit() } },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Age" else "வயது") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("patient_age_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlowTeal,
                        unfocusedBorderColor = CharcoalBorder,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    )
                )

                OutlinedTextField(
                    value = patientMobile,
                    onValueChange = { patientMobile = it },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Mobile Number" else "அலைபேசி எண்") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(2f)
                        .testTag("patient_mobile_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlowTeal,
                        unfocusedBorderColor = CharcoalBorder,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    )
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Select Arrival Buffer (Minutes before token turn)" else "வருகை இடைவெளி நேரம்",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LightGrey
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(10, 15, 20).forEach { mins ->
                        val isSelected = selectedBuffer == mins
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) FlowTeal else CharcoalSurface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) FlowTeal else CharcoalBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedBuffer = mins }
                        ) {
                            Text(
                                text = "$mins mins" + if (mins == 15) " ★" else "",
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                color = if (isSelected) CharcoalBlack else PureWhite,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            Surface(
                color = CharcoalSurface,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isPriorityPassSelected) Color(0xFFF59E0B) else CharcoalBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isPriorityPassSelected = !isPriorityPassSelected }
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = isPriorityPassSelected,
                            onCheckedChange = { isPriorityPassSelected = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFF59E0B),
                                uncheckedColor = MutedGrey
                            )
                        )
                        Column {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Fast-Track Priority Pass (₹50)" else "முன்னுரிமை பாஸ் (₹50)",
                                fontWeight = FontWeight.Bold,
                                color = PureWhite,
                                fontSize = 12.sp
                            )
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Prioritizes your token ahead in OPD queue" else "வரிசையில் முன்னுரிமை வழங்கப்படும்",
                                color = MutedGrey,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = visitNotes,
                onValueChange = { visitNotes = it },
                label = { Text(if (language == AppLanguage.ENGLISH) "Reason for consultation (optional)" else "காரணம் / அறிகுறிகள்") },
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FlowTeal,
                    unfocusedBorderColor = CharcoalBorder,
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite
                )
            )

            validationError?.let { err ->
                Text(
                    text = err,
                    color = StatusDanger,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = {
                    if (patientName.isBlank() || patientAge.isBlank() || patientMobile.isBlank()) {
                        validationError = "Please enter patient name, age and mobile."
                        return@Button
                    }
                    val ageInt = patientAge.toIntOrNull() ?: 25
                    onConfirmBooking(
                        patientName,
                        ageInt,
                        patientMobile,
                        isPriorityPassSelected,
                        selectedBuffer,
                        visitNotes
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = FlowTeal),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("confirm_booking_btn")
            ) {
                Icon(
                    imageVector = Icons.Filled.ConfirmationNumber,
                    contentDescription = null,
                    tint = CharcoalBlack,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Confirm & Generate Token" else "டோக்கனை உறுதி செய்க",
                    color = CharcoalBlack,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
