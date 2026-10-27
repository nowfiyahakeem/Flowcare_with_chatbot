package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowCareTopAppBar(
    currentRole: UserRole,
    currentLanguage: AppLanguage,
    onRoleSelected: (UserRole) -> Unit,
    onLanguageToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showRoleMenu by remember { mutableStateOf(false) }

    Surface(
        color = CharcoalBlack,
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Hub Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(FlowTeal, FlowTealDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalHospital,
                        contentDescription = "FlowCare Logo",
                        tint = CharcoalBlack,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "FLOW",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = PureWhite,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "CARE",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = FlowTeal,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = if (currentLanguage == AppLanguage.ENGLISH) "Erode Hub • Smart Arrival" else "ஈரோடு மையம் • ஸ்மார்ட் வருகை",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedGrey,
                        fontSize = 10.sp
                    )
                }
            }

            // Right side: Language Toggle and Role Switcher
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Language Toggle Button
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = CharcoalCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onLanguageToggle() }
                        .testTag("language_toggle_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Translate,
                            contentDescription = "Translate",
                            tint = FlowTeal,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.ENGLISH) "தமிழ்" else "EN",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    }
                }

                // Role Switcher Dropdown
                Box {
                    val (roleColor, roleText) = when (currentRole) {
                        UserRole.PATIENT -> FlowTeal to if (currentLanguage == AppLanguage.ENGLISH) "Patient" else "நோயாளி"
                        UserRole.RECEPTIONIST -> StatusWarning to if (currentLanguage == AppLanguage.ENGLISH) "Reception" else "வரவேற்பு"
                        UserRole.ADMIN -> StatusInfo to if (currentLanguage == AppLanguage.ENGLISH) "Admin" else "நிர்வாகி"
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = roleColor.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, roleColor.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { showRoleMenu = true }
                            .testTag("role_switcher_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(roleColor)
                            )
                            Text(
                                text = roleText,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = roleColor
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Switch Role",
                                tint = roleColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showRoleMenu,
                        onDismissRequest = { showRoleMenu = false },
                        modifier = Modifier
                            .background(CharcoalCard)
                            .border(1.dp, CharcoalBorder, RoundedCornerShape(8.dp))
                    ) {
                        DropdownMenuItem(
                            text = { Text("Patient Portal", color = PureWhite) },
                            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = FlowTeal) },
                            onClick = {
                                onRoleSelected(UserRole.PATIENT)
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Receptionist OPD Desk", color = PureWhite) },
                            leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = null, tint = StatusWarning) },
                            onClick = {
                                onRoleSelected(UserRole.RECEPTIONIST)
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Hospital Admin Portal", color = PureWhite) },
                            leadingIcon = { Icon(Icons.Filled.AdminPanelSettings, contentDescription = null, tint = StatusInfo) },
                            onClick = {
                                onRoleSelected(UserRole.ADMIN)
                                showRoleMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: DoctorStatus,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        DoctorStatus.CONSULTING -> Triple(StatusSuccess.copy(alpha = 0.15f), StatusSuccess, if (language == AppLanguage.ENGLISH) "Consulting" else "பரிசோதிக்கிறார்")
        DoctorStatus.ARRIVED -> Triple(FlowTeal.copy(alpha = 0.15f), FlowTeal, if (language == AppLanguage.ENGLISH) "In Chamber" else "அறையில் உள்ளார்")
        DoctorStatus.ON_BREAK -> Triple(StatusWarning.copy(alpha = 0.15f), StatusWarning, if (language == AppLanguage.ENGLISH) "On Break" else "இடைவேளை")
        DoctorStatus.DELAYED -> Triple(StatusDanger.copy(alpha = 0.15f), StatusDanger, if (language == AppLanguage.ENGLISH) "Delayed" else "தாமதம்")
        DoctorStatus.DEPARTED -> Triple(MutedGrey.copy(alpha = 0.15f), LightGrey, if (language == AppLanguage.ENGLISH) "Departed" else "கிளம்பிவிட்டார்")
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}

@Composable
fun InteractiveRouteMapCard(
    originName: String = "Perundurai Road, Erode",
    destinationName: String,
    distanceKm: Float,
    travelMinutes: Int,
    recommendedDepartureTime: String,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CharcoalCard),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
        modifier = modifier.fillMaxWidth()
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Navigation,
                        contentDescription = "Navigation",
                        tint = FlowTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Smart Route & Travel Time" else "பயண வழி மற்றும் நேரம்",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                }

                Surface(
                    color = StatusSuccess.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Traffic: Normal" else "போக்குவரத்து: சீரானது",
                        color = StatusSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Visual route canvas diagram
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CharcoalSurface)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val startX = 30f
                    val endX = size.width - 30f
                    val midY = size.height / 2f

                    // Draw road background
                    drawLine(
                        color = Color(0xFF3F3F46),
                        start = Offset(startX, midY),
                        end = Offset(endX, midY),
                        strokeWidth = 14f
                    )

                    // Draw progress green route
                    drawLine(
                        color = FlowTeal,
                        start = Offset(startX, midY),
                        end = Offset(startX + (endX - startX) * 0.7f, midY),
                        strokeWidth = 6f
                    )

                    // Dashed line ahead
                    drawLine(
                        color = FlowTealLight,
                        start = Offset(startX + (endX - startX) * 0.7f, midY),
                        end = Offset(endX, midY),
                        strokeWidth = 6f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                    )

                    // Origin Circle
                    drawCircle(color = StatusInfo, radius = 10f, center = Offset(startX, midY))
                    drawCircle(color = PureWhite, radius = 5f, center = Offset(startX, midY))

                    // Destination Circle
                    drawCircle(color = FlowTeal, radius = 12f, center = Offset(endX, midY))
                    drawCircle(color = CharcoalBlack, radius = 6f, center = Offset(endX, midY))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Your Location" else "தங்கள் இடம்",
                            color = LightGrey,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "Erode East",
                            color = PureWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Surface(
                        color = CharcoalGrey,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FlowTeal.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Filled.DirectionsCar, contentDescription = null, tint = FlowTeal, modifier = Modifier.size(14.dp))
                            Text(
                                text = "$travelMinutes mins • $distanceKm km",
                                color = FlowTealLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Hospital" else "மருத்துவமனை",
                            color = LightGrey,
                            fontSize = 11.sp
                        )
                        Text(
                            text = destinationName.take(14) + "...",
                            color = PureWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Key Departure Banner
            Surface(
                color = FlowTealContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FlowTeal)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Alarm,
                            contentDescription = "Departure Alarm",
                            tint = FlowTealLight,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Recommended Departure" else "பரிந்துரைக்கப்பட்ட புறப்படும் நேரம்",
                                fontSize = 11.sp,
                                color = OffWhite
                            )
                            Text(
                                text = recommendedDepartureTime,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = PureWhite
                            )
                        }
                    }

                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Includes 15m Buffer" else "15 நிமி முன்பதிவு",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FlowTealLight
                    )
                }
            }
        }
    }
}

@Composable
fun PriorityPassCard(
    isEnrolled: Boolean,
    onPurchaseClick: () -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CharcoalCard
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Brush.horizontalGradient(listOf(FlowTeal, Color(0xFFF59E0B)))
        ),
        modifier = modifier.fillMaxWidth()
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFFF59E0B), FlowTeal))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Bolt,
                            contentDescription = "Fast Track",
                            tint = CharcoalBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "FlowCare Priority Pass™",
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = 15.sp
                        )
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Fast-Track OP Token & Zero Delay" else "விரைவு டோக்கன் மற்றும் தாமதமின்மை",
                            color = MutedGrey,
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "₹50 / visit",
                        color = Color(0xFFF59E0B),
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = if (language == AppLanguage.ENGLISH)
                    "Get priority placement ahead of standard walk-ins, precision doctor arrival SMS alerts, and reserved waiting bay."
                else
                    "சாதாரண டோக்கன்களுக்கு முன் முன்னுரிமை, நேரடி எஸ்.எம்.எஸ் எச்சரிக்கைகள் மற்றும் காத்திருப்பு இருக்கை உத்தரவாதம்.",
                color = LightGrey,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Button(
                onClick = onPurchaseClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEnrolled) StatusSuccess else FlowTeal
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .testTag("priority_pass_cta_btn")
            ) {
                Icon(
                    imageVector = if (isEnrolled) Icons.Filled.CheckCircle else Icons.Filled.FlashOn,
                    contentDescription = null,
                    tint = if (isEnrolled) PureWhite else CharcoalBlack,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = if (isEnrolled)
                        (if (language == AppLanguage.ENGLISH) "Priority Pass Active" else "முன்னுரிமை பாஸ் செயலில் உள்ளது")
                    else
                        (if (language == AppLanguage.ENGLISH) "Activate Pass for ₹50" else "₹50-க்கு முன்னுரிமை பெறவும்"),
                    color = if (isEnrolled) PureWhite else CharcoalBlack,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
