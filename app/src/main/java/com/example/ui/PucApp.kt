package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.TaxGuideScreen
import com.example.ui.theme.CleanPaperBackground
import com.example.ui.theme.CleanPaperBorder
import com.example.ui.theme.CleanPaperSurface
import com.example.ui.theme.ColombiaBlue
import com.example.ui.theme.ColombiaRed
import com.example.ui.theme.ColombiaYellow
import com.example.ui.theme.MintGreenPrimary
import com.example.ui.theme.MintGreenPrimaryContainer
import com.example.ui.theme.SoftCharcoalText
import com.example.ui.theme.SoftCharcoalTextMuted
import com.example.ui.theme.SoftCharcoalTextSecondary
import com.example.viewmodel.PucViewModel
import kotlinx.coroutines.delay

sealed class PucNavDestination(
    val title: String,
    val icon: ImageVector,
    val testTag: String
) {
    data object Catalog : PucNavDestination("Catálogo PUC", Icons.AutoMirrored.Filled.MenuBook, "nav_catalog")
    data object TaxGuide : PucNavDestination("Retenciones", Icons.Default.Payments, "nav_taxes")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PucApp() {
    val viewModel: PucViewModel = viewModel()
    val isDataLoaded by viewModel.isDataLoaded.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(isDataLoaded) {
        if (isDataLoaded) {
            delay(1200) // Minimum 1.2 seconds premium splash screen display duration
            showSplash = false
        }
    }

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    val destinations = listOf(
        PucNavDestination.Catalog,
        PucNavDestination.TaxGuide
    )

    if (!isDataLoaded || showSplash) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CleanPaperBackground),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Surface(
                    color = MintGreenPrimaryContainer,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = MintGreenPrimary,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "PUC Colombia",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftCharcoalText
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Cargando base de datos contable...",
                    fontSize = 13.sp,
                    color = SoftCharcoalTextSecondary
                )
            }
        }
        return
    }

    Scaffold(
        topBar = {
            Column {
                // Subtle Colombia flag tricolor strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight()
                            .background(ColombiaYellow)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(ColombiaBlue)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(ColombiaRed)
                    )
                }

                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "PUC Colombia",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SoftCharcoalText
                                )
                            )
                            Text(
                                text = "Decreto 2650 de 1993 • Catálogo Jerárquico Offline",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = SoftCharcoalTextSecondary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CleanPaperSurface
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(CleanPaperBorder)
                )
            }
        },
        bottomBar = {
            if (!isTablet) {
                NavigationBar(
                    containerColor = CleanPaperSurface,
                    modifier = Modifier.testTag("puc_bottom_navigation")
                ) {
                    destinations.forEachIndexed { index, destination ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Icon(destination.icon, contentDescription = destination.title) },
                            label = { Text(destination.title, fontSize = 10.5.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) },
                            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                                selectedIconColor = MintGreenPrimary,
                                selectedTextColor = MintGreenPrimary,
                                unselectedIconColor = SoftCharcoalTextMuted,
                                unselectedTextColor = SoftCharcoalTextMuted,
                                indicatorColor = MintGreenPrimaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        if (isTablet) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NavigationRail(
                    containerColor = CleanPaperSurface
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    destinations.forEachIndexed { index, destination ->
                        NavigationRailItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Icon(destination.icon, contentDescription = destination.title) },
                            label = { Text(destination.title, fontSize = 11.sp) },
                            colors = androidx.compose.material3.NavigationRailItemDefaults.colors(
                                selectedIconColor = MintGreenPrimary,
                                selectedTextColor = MintGreenPrimary,
                                unselectedIconColor = SoftCharcoalTextMuted,
                                unselectedTextColor = SoftCharcoalTextMuted,
                                indicatorColor = MintGreenPrimaryContainer
                            )
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(CleanPaperBorder)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    when (selectedTab) {
                        0 -> CatalogScreen(viewModel = viewModel)
                        1 -> TaxGuideScreen()
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> CatalogScreen(viewModel = viewModel)
                    1 -> TaxGuideScreen()
                }
            }
        }
    }
}
