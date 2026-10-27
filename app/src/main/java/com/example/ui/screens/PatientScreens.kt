package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.SmartArrivalEngine
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun PatientDashboardScreen(
    currentUser: UserProfile,
    hospitals: List<Hospital>,
    doctors: List<Doctor>,
    appointments: List<TokenAppointment>,
    language: AppLanguage,
    onNavigateToTokens: () -> Unit,
    onNavigateToHospitals: (specialtyFilter: String?) -> Unit,
    onOpenPriorityPass: () -> Unit,
    onBookDoctor: (Doctor, Hospital) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeAppointment = appointments.firstOrNull {
        it.patientId == currentUser.id && (it.status == TokenStatus.WAITING || it.status == TokenStatus.IN_CONSULTATION)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CharcoalBlack)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        // Welcome Header & Quick Status
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Vanakkam, ${currentUser.fullName.split(" ").first()}" else "வணக்கம், ${currentUser.fullName.split(" ").first()}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Erode Medical District • Live OPDs" else "ஈரோடு மருத்துவ மாவட்டம் • நேரலை நிலை",
                        style = MaterialTheme.typography.bodySmall,
                        color = FlowTealLight
                    )
                }

                Surface(
                    color = CharcoalSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(StatusSuccess)
                        )
                        Text(
                            text = "Live",
                            color = StatusSuccess,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Emergency Quick SOS Bar
        item {
            Surface(
                color = StatusDanger.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StatusDanger.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Emergency,
                            contentDescription = "Emergency",
                            tint = StatusDanger,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Emergency Helpline: 108" else "அவசர உதவி எண்: 108",
                                fontWeight = FontWeight.Bold,
                                color = PureWhite,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Erode GH 24/7 Casualty Available" else "ஈரோடு அரசு அவசர சிகிச்சை பிரிவு தயார்",
                                color = LightGrey,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Surface(
                        color = StatusDanger,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    ) {
                        Text(
                            text = "Call 108",
                            color = PureWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Active Token Banner (if booked)
        item {
            if (activeAppointment != null) {
                val doctor = doctors.find { it.id == activeAppointment.doctorId }
                val prediction = doctor?.let {
                    SmartArrivalEngine.calculate(
                        it,
                        activeAppointment.queuePosition,
                        activeAppointment.userTravelMinutes,
                        activeAppointment.arrivalBufferMinutes
                    )
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = CharcoalCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, FlowTeal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToTokens() }
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
                            Surface(
                                color = FlowTeal.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = activeAppointment.tokenNumber,
                                    color = FlowTealLight,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            doctor?.let { StatusBadge(status = it.status, language = language) }
                        }

                        Column {
                            Text(
                                text = activeAppointment.doctorName,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${activeAppointment.doctorSpecialty} • ${activeAppointment.hospitalName}",
                                color = LightGrey,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Divider(color = CharcoalBorder, thickness = 0.5.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) "Queue Position" else "வரிசை எண்",
                                    color = MutedGrey,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = if (activeAppointment.queuePosition == 0) "Your Turn!" else "${activeAppointment.queuePosition} ahead",
                                    color = if (activeAppointment.queuePosition == 0) StatusSuccess else PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            prediction?.let {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = if (language == AppLanguage.ENGLISH) "Recommended Departure" else "புறப்படும் நேரம்",
                                        color = FlowTealLight,
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = it.recommendedDepartureTime,
                                        color = PureWhite,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = onNavigateToTokens,
                            colors = ButtonDefaults.buttonColors(containerColor = FlowTealContainer),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "View Live Queue & Route Map" else "நேரடி வரிசை & வழியைக் காண்க",
                                color = FlowTealLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = FlowTealLight,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Priority Pass Promo Card
        item {
            PriorityPassCard(
                isEnrolled = currentUser.isPriorityPassHolder,
                onPurchaseClick = onOpenPriorityPass,
                language = language
            )
        }

        // Quick Specialty Filter Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Find Care by Specialty" else "பிரிவு வாரியாக மருத்துவர்கள்",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )

                val specialties = listOf(
                    "Cardiology" to Icons.Filled.Favorite,
                    "Pediatrics" to Icons.Filled.ChildCare,
                    "Orthopedics" to Icons.Filled.AccessibilityNew,
                    "Gynecology" to Icons.Filled.PregnantWoman,
                    "General Medicine" to Icons.Filled.MedicalServices
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(specialties) { (spec, icon) ->
                        Surface(
                            color = CharcoalCard,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onNavigateToHospitals(spec) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(FlowTeal.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = FlowTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = spec,
                                    color = PureWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Featured Hospitals in Erode
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Erode Medical Centers" else "ஈரோடு மருத்துவமனைகள்",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
                Text(
                    text = if (language == AppLanguage.ENGLISH) "View All" else "அனைத்தும் காண்க",
                    color = FlowTeal,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigateToHospitals(null) }
                )
            }
        }

        val erodeHospitals = hospitals.filter { it.district == "Erode" }
        items(erodeHospitals) { hospital ->
            HospitalCard(
                hospital = hospital,
                doctors = doctors.filter { it.hospitalId == hospital.id },
                language = language,
                onBookDoctor = onBookDoctor
            )
        }
    }
}

@Composable
fun HospitalCard(
    hospital: Hospital,
    doctors: List<Doctor>,
    language: AppLanguage,
    onBookDoctor: (Doctor, Hospital) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = CharcoalCard),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) hospital.name else hospital.nameTamil,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (hospital.isVerified) {
                            Icon(
                                imageVector = Icons.Filled.Verified,
                                contentDescription = "Verified",
                                tint = FlowTeal,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Text(
                        text = if (language == AppLanguage.ENGLISH) hospital.address else hospital.addressTamil,
                        color = LightGrey,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    color = CharcoalSurface,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(12.dp))
                        Text(
                            text = "${hospital.rating}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = PureWhite
                        )
                    }
                }
            }

            // Key Quick Details: Distance & ICU Beds
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = CharcoalSurface,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = FlowTeal, modifier = Modifier.size(12.dp))
                        Text(
                            text = "${hospital.distanceKm} km • ~${hospital.travelMinutesFromCenter}m drive",
                            color = LightGrey,
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    color = CharcoalSurface,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Filled.Bed, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(12.dp))
                        Text(
                            text = "${hospital.availableIcuBeds} ICU Beds",
                            color = StatusSuccess,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Expandable Doctors List
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${doctors.size} " + if (language == AppLanguage.ENGLISH) "Doctors on Duty Today" else "மருத்துவர்கள் பணியில்",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FlowTealLight
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = FlowTealLight,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    doctors.forEach { doctor ->
                        Surface(
                            color = CharcoalSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.ENGLISH) doctor.name else doctor.nameTamil,
                                            fontWeight = FontWeight.Bold,
                                            color = PureWhite,
                                            fontSize = 13.sp
                                        )
                                        StatusBadge(status = doctor.status, language = language)
                                    }
                                    Text(
                                        text = "${if (language == AppLanguage.ENGLISH) doctor.specialty else doctor.specialtyTamil} • ${doctor.roomNo}",
                                        color = LightGrey,
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = "Fee: " + if (doctor.consultationFee == 0) "Free Govt OP" else "₹${doctor.consultationFee}",
                                        color = FlowTeal,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Button(
                                    onClick = { onBookDoctor(doctor, hospital) },
                                    colors = ButtonDefaults.buttonColors(containerColor = FlowTeal),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ENGLISH) "Book Token" else "முன்பதிவு",
                                        color = CharcoalBlack,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HospitalSearchScreen(
    hospitals: List<Hospital>,
    doctors: List<Doctor>,
    initialSpecialty: String?,
    language: AppLanguage,
    onBookDoctor: (Doctor, Hospital) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("Erode") }
    var selectedSpecialty by remember { mutableStateOf(initialSpecialty ?: "All") }

    val districts = listOf("Erode", "Coimbatore", "Salem", "Chennai", "All TN")
    val specialties = listOf("All", "Cardiology", "Pediatrics", "Orthopedics", "Gynecology", "General Medicine")

    val filteredHospitals = hospitals.filter { hospital ->
        val matchesDistrict = if (selectedDistrict == "All TN") true else hospital.district == selectedDistrict
        val matchesSpecialty = if (selectedSpecialty == "All") true else hospital.specialties.contains(selectedSpecialty)
        val matchesQuery = searchQuery.isBlank() ||
                hospital.name.contains(searchQuery, ignoreCase = true) ||
                hospital.address.contains(searchQuery, ignoreCase = true) ||
                hospital.specialties.any { it.contains(searchQuery, ignoreCase = true) }
        matchesDistrict && matchesSpecialty && matchesQuery
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CharcoalBlack)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (language == AppLanguage.ENGLISH) "Search hospitals, doctors or specialties..." else "மருத்துவமனை அல்லது மருத்துவர் தேடுக...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search", tint = FlowTeal) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear", tint = LightGrey)
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("hospital_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FlowTeal,
                unfocusedBorderColor = CharcoalBorder,
                focusedTextColor = PureWhite,
                unfocusedTextColor = PureWhite
            )
        )

        // District filter chips
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Filter by District (Tamil Nadu)" else "மாவட்டம் தேர்வு செய்க",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = LightGrey
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(districts) { dist ->
                    val isSelected = selectedDistrict == dist
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) FlowTeal else CharcoalCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) FlowTeal else CharcoalBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { selectedDistrict = dist }
                    ) {
                        Text(
                            text = dist + if (dist == "Erode") " (Focus)" else "",
                            color = if (isSelected) CharcoalBlack else PureWhite,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Specialty filter chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(specialties) { spec ->
                val isSelected = selectedSpecialty == spec
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) FlowTealContainer else CharcoalSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) FlowTeal else CharcoalBorder),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { selectedSpecialty = spec }
                ) {
                    Text(
                        text = spec,
                        color = if (isSelected) FlowTealLight else LightGrey,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Text(
            text = "${filteredHospitals.size} " + if (language == AppLanguage.ENGLISH) "hospitals found in $selectedDistrict" else "மருத்துவமனைகள் கண்டறியப்பட்டன",
            fontSize = 12.sp,
            color = MutedGrey
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredHospitals) { hospital ->
                HospitalCard(
                    hospital = hospital,
                    doctors = doctors.filter { it.hospitalId == hospital.id },
                    language = language,
                    onBookDoctor = onBookDoctor
                )
            }
        }
    }
}

@Composable
fun MyTokensScreen(
    appointments: List<TokenAppointment>,
    doctors: List<Doctor>,
    hospitals: List<Hospital>,
    language: AppLanguage,
    onCancelAppointment: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedBuffer by remember { mutableStateOf(15) }

    val userAppointments = appointments.filter {
        it.status == TokenStatus.WAITING || it.status == TokenStatus.IN_CONSULTATION
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CharcoalBlack)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Live Token Queue & Smart Arrival" else "நேரடி டோக்கன் & ஸ்மார்ட் வருகை திட்டம்",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PureWhite
            )
            Text(
                text = if (language == AppLanguage.ENGLISH) "Dynamic AI estimation based on doctor pace, queue and traffic" else "மருத்துவர் வேகம் மற்றும் பயண நேரத்தை அடிப்படையாகக் கொண்ட AI கணிப்பு",
                style = MaterialTheme.typography.bodySmall,
                color = FlowTealLight
            )
        }

        if (userAppointments.isEmpty()) {
            item {
                Surface(
                    color = CharcoalCard,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ConfirmationNumber,
                            contentDescription = null,
                            tint = FlowTeal,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "No Active Tokens" else "செயலில் உள்ள டோக்கன்கள் இல்லை",
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = 16.sp
                        )
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Browse hospitals in Erode to book your OPD consultation slot." else "ஈரோட்டின் மருத்துவமனைகளை பார்த்து உங்கள் டோக்கனை முன்பதிவு செய்க.",
                            color = LightGrey,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(userAppointments) { apt ->
                val doctor = doctors.find { it.id == apt.doctorId }
                val hospital = hospitals.find { it.id == apt.hospitalId }

                val prediction = doctor?.let {
                    SmartArrivalEngine.calculate(
                        it,
                        apt.queuePosition,
                        apt.userTravelMinutes,
                        selectedBuffer
                    )
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = CharcoalCard),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, FlowTeal),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Ticket Top Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = apt.tokenNumber,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black,
                                        color = FlowTeal
                                    )
                                    if (apt.isPriorityPass) {
                                        Surface(
                                            color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "PRIORITY",
                                                color = Color(0xFFF59E0B),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = apt.patientName + " (${apt.patientAge}y)",
                                    color = LightGrey,
                                    fontSize = 12.sp
                                )
                            }

                            doctor?.let { StatusBadge(status = it.status, language = language) }
                        }

                        Divider(color = CharcoalBorder, thickness = 0.5.dp)

                        // Doctor & OPD Chamber
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = apt.doctorName, fontWeight = FontWeight.Bold, color = PureWhite, fontSize = 15.sp)
                                Text(text = "${apt.doctorSpecialty} • ${doctor?.roomNo ?: "OPD Room"}", color = LightGrey, fontSize = 12.sp)
                                Text(text = apt.hospitalName, color = FlowTealLight, fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) "Serving Now" else "தற்போது பரிசோதனை",
                                    color = MutedGrey,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = doctor?.currentlyServingToken ?: "None",
                                    color = StatusSuccess,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        // Smart Arrival Engine Insights Box
                        prediction?.let { pred ->
                            Surface(
                                color = CharcoalSurface,
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, FlowTealContainer)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = FlowTeal, modifier = Modifier.size(16.dp))
                                            Text(
                                                text = if (language == AppLanguage.ENGLISH) "Smart Arrival Engine" else "ஸ்மார்ட் வருகை கணிப்பு",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = FlowTeal
                                            )
                                        }

                                        Text(
                                            text = "${pred.confidenceScore}% Confidence",
                                            color = StatusSuccess,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = if (language == AppLanguage.ENGLISH) "Estimated Consultation" else "பரிசோதனை நேரம்",
                                                color = LightGrey,
                                                fontSize = 11.sp
                                            )
                                            Text(
                                                text = pred.predictedConsultationTime,
                                                color = PureWhite,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = if (language == AppLanguage.ENGLISH) "Est. Wait Time" else "காத்திருப்பு நேரம்",
                                                color = LightGrey,
                                                fontSize = 11.sp
                                            )
                                            Text(
                                                text = "${pred.estimatedWaitMinutes} mins",
                                                color = FlowTealLight,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )
                                        }
                                    }

                                    Text(
                                        text = if (language == AppLanguage.ENGLISH) pred.explanationEn else pred.explanationTa,
                                        color = OffWhite,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            // Interactive Buffer selector
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) "Arrival Buffer:" else "வருகை இடைவெளி:",
                                    color = LightGrey,
                                    fontSize = 12.sp
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf(10, 15, 20).forEach { mins ->
                                        val isSel = selectedBuffer == mins
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSel) FlowTeal else CharcoalSurface,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable { selectedBuffer = mins }
                                        ) {
                                            Text(
                                                text = "${mins}m",
                                                color = if (isSel) CharcoalBlack else PureWhite,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Interactive route card
                            InteractiveRouteMapCard(
                                destinationName = apt.hospitalName,
                                distanceKm = apt.userDistanceKm,
                                travelMinutes = apt.userTravelMinutes,
                                recommendedDepartureTime = pred.recommendedDepartureTime,
                                language = language
                            )
                        }

                        // Cancel Appointment Button
                        OutlinedButton(
                            onClick = { onCancelAppointment(apt.id) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusDanger),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StatusDanger.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        ) {
                            Icon(Icons.Filled.Cancel, contentDescription = null, tint = StatusDanger, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Cancel This Token" else "டோக்கனை ரத்து செய்க",
                                color = StatusDanger,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CareBuddyScreen(
    chatMessages: List<ChatMessage>,
    language: AppLanguage,
    onSendMessage: (String) -> Unit,
    onBookSuggestedDoctor: (hospitalId: String, doctorId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }

    val quickPrompts = listOf(
        "Chest pain & heart checkup in Erode",
        "Best child doctor at KMCH",
        "Free OPD at Erode GH",
        "How does smart arrival work?"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CharcoalBlack)
            .padding(16.dp)
    ) {
        // Chat Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(FlowTeal.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.SmartToy,
                    contentDescription = "Care Buddy AI",
                    tint = FlowTeal,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column {
                Text(
                    text = "FlowCare Care Buddy™",
                    fontWeight = FontWeight.Bold,
                    color = PureWhite,
                    fontSize = 16.sp
                )
                Text(
                    text = if (language == AppLanguage.ENGLISH) "AI Health Triage & Erode Hospital Advisor" else "AI நல ஆலோசகர் & மருத்துவமனை வழிகாட்டி",
                    color = FlowTealLight,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Quick prompt chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            items(quickPrompts) { prompt ->
                Surface(
                    color = CharcoalCard,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSendMessage(prompt) }
                ) {
                    Text(
                        text = prompt,
                        color = PureWhite,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Chat message list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(chatMessages) { msg ->
                val isUser = msg.isFromUser
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
                ) {
                    Surface(
                        color = if (isUser) FlowTeal else CharcoalCard,
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomStart = if (isUser) 14.dp else 2.dp,
                            bottomEnd = if (isUser) 2.dp else 14.dp
                        ),
                        border = if (!isUser) androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder) else null,
                        modifier = Modifier.widthIn(max = 300.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) msg.messageEn else msg.messageTa,
                                color = if (isUser) CharcoalBlack else PureWhite,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )

                            // Action button if bot recommended a hospital/doctor
                            if (!isUser && msg.actionHospitalId != null && msg.actionDoctorId != null) {
                                Button(
                                    onClick = { onBookSuggestedDoctor(msg.actionHospitalId, msg.actionDoctorId) },
                                    colors = ButtonDefaults.buttonColors(containerColor = FlowTeal),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = CharcoalBlack, modifier = Modifier.size(14.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = if (language == AppLanguage.ENGLISH) "Book Token Now" else "முன்பதிவு செய்க",
                                        color = CharcoalBlack,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = msg.timestamp,
                        fontSize = 10.sp,
                        color = MutedGrey,
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp, end = 4.dp)
                    )
                }
            }
        }

        // Input field and send button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text(if (language == AppLanguage.ENGLISH) "Ask symptoms, wait time or doctors..." else "கேள்விகள் அல்லது அறிகுறிகளை தட்டச்சு செய்க...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("care_buddy_chat_input"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FlowTeal,
                    unfocusedBorderColor = CharcoalBorder,
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite
                )
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        val text = inputText
                        inputText = ""
                        onSendMessage(text)
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(FlowTeal)
                    .testTag("care_buddy_send_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = CharcoalBlack
                )
            }
        }
    }
}
