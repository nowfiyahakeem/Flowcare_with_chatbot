package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.model.AppLanguage
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentModal(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onConfirmPayment: (method: String) -> Unit,
    language: AppLanguage
) {
    if (!isOpen) return

    var selectedMethod by remember { mutableStateOf("UPI / GPay / PhonePe") }
    var isProcessing by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CharcoalCard,
        dragHandle = { BottomSheetDefaults.DragHandle(color = CharcoalBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "FlowCare Priority Pass" else "முன்னுரிமை பாஸ் கட்டணம்",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Secure Payment Gateway Sandbox" else "பாதுகாப்பான கட்டண முறை (மாதிரி)",
                        style = MaterialTheme.typography.bodySmall,
                        color = LightGrey
                    )
                }

                Surface(
                    color = FlowTeal.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "₹50.00",
                        color = FlowTeal,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            // Note that this is a simulated sandbox integration
            Surface(
                color = CharcoalSurface,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Test Mode",
                        tint = FlowTealLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "Demo Payment Sandbox: No real bank account will be charged. Clicking Pay confirms your fast-track pass instantly."
                        else
                            "மாதிரி கட்டண முறை: வங்கி கணக்கில் பணம் கழிக்கப்படாது. உடனடியாக பாஸ் செயல்படுத்தப்படும்.",
                        fontSize = 11.sp,
                        color = LightGrey,
                        lineHeight = 15.sp
                    )
                }
            }

            Text(
                text = if (language == AppLanguage.ENGLISH) "Select Payment Method" else "கட்டண முறையை தேர்வு செய்க",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = PureWhite
            )

            val methods = listOf(
                "UPI / GPay / PhonePe",
                "Debit / Credit Card",
                "Net Banking (SBI / HDFC / Canara)"
            )

            methods.forEach { method ->
                val isSelected = selectedMethod == method
                Surface(
                    color = if (isSelected) FlowTealContainer.copy(alpha = 0.4f) else CharcoalSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) FlowTeal else CharcoalBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { selectedMethod = method }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = when (method) {
                                    "UPI / GPay / PhonePe" -> Icons.Filled.QrCodeScanner
                                    "Debit / Credit Card" -> Icons.Filled.CreditCard
                                    else -> Icons.Filled.AccountBalance
                                },
                                contentDescription = null,
                                tint = if (isSelected) FlowTeal else LightGrey,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = method,
                                color = if (isSelected) PureWhite else OffWhite,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }

                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedMethod = method },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = FlowTeal,
                                unselectedColor = MutedGrey
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick = {
                    isProcessing = true
                    onConfirmPayment(selectedMethod)
                    onDismiss()
                },
                enabled = !isProcessing,
                colors = ButtonDefaults.buttonColors(containerColor = FlowTeal),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("confirm_payment_btn")
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = CharcoalBlack,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = CharcoalBlack,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Pay ₹50 & Activate Pass" else "₹50 செலுத்தி பாஸ் பெறுக",
                        color = CharcoalBlack,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
