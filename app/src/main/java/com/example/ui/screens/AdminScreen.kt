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
import com.example.ui.theme.*

@Composable
fun AdminScreen(
    hospitals: List<Hospital>,
    doctors: List<Doctor>,
    appointments: List<TokenAppointment>,
    payments: List<PaymentRecord>,
    language: AppLanguage,
    onToggleVerifyHospital: (String) -> Unit,
    onImportSampleCsv: () -> Unit,
    onAddHospital: (Hospital) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("Overview") }
    var showImportSuccessAlert by remember { mutableStateOf(false) }

    val tabs = listOf("Overview", "Hospitals", "CSV Import", "Booking Rules", "Payments")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CharcoalBlack)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        // Admin Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "System Administration" else "நிர்வாக பலகை",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "FlowCare Tamil Nadu • Central Engine" else "FlowCare தமிழ்நாடு • மத்திய நிர்வாகம்",
                        color = FlowTealLight,
                        fontSize = 12.sp
                    )
                }

                Surface(
                    color = StatusInfo.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Super Admin",
                        color = StatusInfo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Sub-tabs
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tabs) { tab ->
                    val isSel = selectedTab == tab
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSel) FlowTeal else CharcoalCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) FlowTeal else CharcoalBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { selectedTab = tab }
                    ) {
                        Text(
                            text = tab,
                            color = if (isSel) CharcoalBlack else PureWhite,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        when (selectedTab) {
            "Overview" -> {
                item {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Real Database Telemetry" else "நேரடி தரவு விவரங்கள்",
                        fontWeight = FontWeight.Bold,
                        color = PureWhite,
                        fontSize = 14.sp
                    )
                }

                item {
                    val stats = listOf(
                        Triple("Active Hospitals", "${hospitals.size}", Icons.Filled.LocalHospital),
                        Triple("Specialist Doctors", "${doctors.size}", Icons.Filled.MedicalServices),
                        Triple("Tokens Issued", "${appointments.size}", Icons.Filled.ConfirmationNumber),
                        Triple("Priority Passes", "${payments.size}", Icons.Filled.FlashOn)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            stats.take(2).forEach { (title, count, icon) ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = CharcoalCard),
                                    shape = RoundedCornerShape(14.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(icon, contentDescription = null, tint = FlowTeal, modifier = Modifier.size(22.dp))
                                        Text(text = count, fontSize = 22.sp, fontWeight = FontWeight.Black, color = PureWhite)
                                        Text(text = title, fontSize = 11.sp, color = LightGrey)
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            stats.takeLast(2).forEach { (title, count, icon) ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = CharcoalCard),
                                    shape = RoundedCornerShape(14.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(icon, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(22.dp))
                                        Text(text = count, fontSize = 22.sp, fontWeight = FontWeight.Black, color = PureWhite)
                                        Text(text = title, fontSize = 11.sp, color = LightGrey)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CharcoalCard),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "System Operational Health",
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "99.98% Uptime",
                                    color = StatusSuccess,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = "• Smart Arrival Prediction Engine: Optimal (Avg delay variance: ±3.2 mins)\n" +
                                        "• Erode Regional District Queue Latency: 42ms\n" +
                                        "• Verified Hospital Ratio: 87.5% (7 of 8 centers certified)\n" +
                                        "• Priority Pass Revenue Collected: ₹${payments.sumOf { it.amount }}",
                                color = LightGrey,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            "Hospitals" -> {
                item {
                    Text(
                        text = "Hospital Verification & Registry",
                        fontWeight = FontWeight.Bold,
                        color = PureWhite,
                        fontSize = 14.sp
                    )
                }

                items(hospitals) { hsp ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CharcoalCard),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = hsp.name,
                                        fontWeight = FontWeight.Bold,
                                        color = PureWhite,
                                        fontSize = 13.sp
                                    )
                                    if (hsp.isVerified) {
                                        Icon(Icons.Filled.Verified, contentDescription = null, tint = FlowTeal, modifier = Modifier.size(16.dp))
                                    }
                                }
                                Text(
                                    text = "${hsp.district} • ${hsp.phone}",
                                    color = LightGrey,
                                    fontSize = 11.sp
                                )
                            }

                            Button(
                                onClick = { onToggleVerifyHospital(hsp.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (hsp.isVerified) StatusSuccess.copy(alpha = 0.2f) else CharcoalSurface
                                ),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (hsp.isVerified) StatusSuccess else FlowTeal
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = if (hsp.isVerified) "Verified" else "Verify",
                                    color = if (hsp.isVerified) StatusSuccess else FlowTeal,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            "CSV Import" -> {
                item {
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
                            Text(
                                text = "Bulk Hospital & Doctor CSV Importer",
                                fontWeight = FontWeight.Bold,
                                color = PureWhite,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Import vetted hospital profiles, specialty rosters, and OPD timings from standard CSV files into the active FlowCare database.",
                                color = LightGrey,
                                fontSize = 12.sp
                            )

                            // CSV Preview Box
                            Surface(
                                color = CharcoalSurface,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "id,name,district,address,phone,specialties\n" +
                                            "hsp_erode_trust,\"Erode Cancer Centre\",Erode,Thindal,+914242271234,Oncology|Radiology\n" +
                                            "hsp_perundurai_irt,\"Govt Medical College Hospital\",Erode,Perundurai,+914294220910,Pulmonology|Surgery",
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    color = FlowTealLight,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }

                            Button(
                                onClick = {
                                    onImportSampleCsv()
                                    showImportSuccessAlert = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = FlowTeal),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("import_csv_btn")
                            ) {
                                Icon(Icons.Filled.UploadFile, contentDescription = null, tint = CharcoalBlack, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Import Sample Erode Hospital CSV",
                                    color = CharcoalBlack,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            if (showImportSuccessAlert) {
                                Surface(
                                    color = StatusSuccess.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "✓ Successfully imported 2 new Erode medical centers into database!",
                                        color = StatusSuccess,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            "Booking Rules" -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CharcoalCard),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "Hospital Booking Rules Engine",
                                fontWeight = FontWeight.Bold,
                                color = PureWhite,
                                fontSize = 15.sp
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = "Max Token Slots Per Hour", color = PureWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text(text = "6 patients / hour (Ensures minimum 10 mins consultation per patient)", color = LightGrey, fontSize = 11.sp)
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = "Default Smart Arrival Buffer", color = PureWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text(text = "15 minutes before projected token slot", color = LightGrey, fontSize = 11.sp)
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = "Emergency Reserve Slots", color = PureWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text(text = "2 slots reserved per OPD block for red triage walk-ins", color = LightGrey, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            "Payments" -> {
                item {
                    Text(
                        text = "Priority Pass Payment Records (${payments.size})",
                        fontWeight = FontWeight.Bold,
                        color = PureWhite,
                        fontSize = 14.sp
                    )
                }

                items(payments) { p ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CharcoalCard),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = p.patientName, fontWeight = FontWeight.Bold, color = PureWhite, fontSize = 13.sp)
                                Text(text = "${p.purpose} • ${p.transactionRef}", color = LightGrey, fontSize = 11.sp)
                                Text(text = p.date, color = MutedGrey, fontSize = 10.sp)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "₹${p.amount}", fontWeight = FontWeight.Black, color = Color(0xFFF59E0B), fontSize = 14.sp)
                                Text(text = p.status, color = StatusSuccess, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
