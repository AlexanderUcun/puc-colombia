package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.PucAccount
import com.example.ui.theme.CleanPaperBackground
import com.example.ui.theme.CleanPaperSurface
import com.example.ui.theme.MintGreenPrimary
import com.example.ui.theme.SoftCharcoalText
import com.example.ui.theme.SoftCharcoalTextSecondary
import com.example.ui.theme.getPucClassTheme
import com.example.viewmodel.PucViewModel

@Composable
fun FavoritesScreen(
    viewModel: PucViewModel,
    modifier: Modifier = Modifier,
    onSelectAccount: (PucAccount) -> Unit
) {
    val favoriteAccounts by viewModel.favoriteAccounts.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CleanPaperBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = null,
                tint = MintGreenPrimary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Cuentas Guardadas (${favoriteAccounts.size})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = SoftCharcoalText
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (favoriteAccounts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.size(70.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.BookmarkBorder,
                                contentDescription = null,
                                tint = MintGreenPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sin cuentas guardadas",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SoftCharcoalText
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Explora el catálogo y presiona el ícono de marcador en cualquier cuenta para guardarla aquí.",
                        fontSize = 12.sp,
                        color = SoftCharcoalTextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoriteAccounts, key = { it.code }) { account ->
                    val theme = getPucClassTheme(account.code)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectAccount(account) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanPaperSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, theme.borderColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = theme.accentColor,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(width = 54.dp, height = 34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = account.code,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = account.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = SoftCharcoalText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = account.description,
                                    fontSize = 11.sp,
                                    color = SoftCharcoalTextSecondary,
                                    maxLines = 2,
                                    lineHeight = 14.sp
                                )
                            }

                            IconButton(
                                onClick = { viewModel.toggleFavorite(account) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = "Quitar de favoritos",
                                    tint = MintGreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
