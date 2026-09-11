package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.CommonAccountsHelper
import com.example.model.PucNature
import com.example.ui.theme.CleanPaperSurface
import com.example.ui.theme.MintGreenPrimary
import com.example.ui.theme.SoftCharcoalText
import com.example.ui.theme.SoftCharcoalTextMuted
import com.example.ui.theme.SoftCharcoalTextSecondary
import com.example.ui.theme.getPucClassTheme

@Composable
fun CommonAccountsDialog(
    onDismiss: () -> Unit,
    onSelectAccountCode: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = CleanPaperSurface,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = MintGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "69 Cuentas Más Frecuentes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = SoftCharcoalText
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = SoftCharcoalTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Selecciona una cuenta frecuente para ver su detalle, descripción, dinámicas y ejemplos.",
                    fontSize = 11.sp,
                    color = SoftCharcoalTextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFE0E6E2))
                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CommonAccountsHelper.commonAccountsList, key = { it.code }) { acc ->
                        val theme = getPucClassTheme(acc.code)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectAccountCode(acc.code)
                                    onDismiss()
                                },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, theme.borderColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = theme.accentColor,
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.size(width = 54.dp, height = 28.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = acc.code,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = Color.White
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = acc.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.5.sp,
                                            color = SoftCharcoalText
                                        )
                                    }
                                    Surface(
                                        color = if (acc.nature == PucNature.DEBITO) Color(0xFFE8F5E9) else Color(0xFFE3F2FD),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = if (acc.nature == PucNature.DEBITO) "Débito" else "Crédito",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (acc.nature == PucNature.DEBITO) Color(0xFF2E7D32) else Color(0xFF1565C0),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = acc.description,
                                    fontSize = 11.sp,
                                    color = SoftCharcoalTextSecondary,
                                    maxLines = 2,
                                    lineHeight = 14.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "💡 Ejemplo: ${acc.practicalExample}",
                                    fontSize = 10.5.sp,
                                    color = MintGreenPrimary,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 2,
                                    lineHeight = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
