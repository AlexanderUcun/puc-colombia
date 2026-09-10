package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.PucAccount
import com.example.model.PucNature
import com.example.ui.theme.CleanPaperBackground
import com.example.ui.theme.CleanPaperBorder
import com.example.ui.theme.CleanPaperCard
import com.example.ui.theme.CleanPaperSurface
import com.example.ui.theme.DynamicCreditBlockBg
import com.example.ui.theme.DynamicCreditBlockBorder
import com.example.ui.theme.DynamicCreditBlockTitle
import com.example.ui.theme.DynamicDebitBlockBg
import com.example.ui.theme.DynamicDebitBlockBorder
import com.example.ui.theme.DynamicDebitBlockTitle
import com.example.ui.theme.MintGreenPrimary
import com.example.ui.theme.NatureCreditBg
import com.example.ui.theme.NatureCreditText
import com.example.ui.theme.NatureDebitBg
import com.example.ui.theme.NatureDebitText
import com.example.ui.theme.SoftCharcoalText
import com.example.ui.theme.SoftCharcoalTextMuted
import com.example.ui.theme.SoftCharcoalTextSecondary
import com.example.ui.theme.getPucClassTheme
import com.example.viewmodel.PucViewModel

sealed class CatalogDestination {
    data object Classes : CatalogDestination()
    data class Groups(val classAccount: PucAccount) : CatalogDestination()
    data class Accounts(val groupAccount: PucAccount, val classAccount: PucAccount) : CatalogDestination()
    data class Detail(val account: PucAccount, val breadcrumbPath: List<Pair<String, String>>) : CatalogDestination()
}

fun getClassIcon(code: String): ImageVector {
    return when (code) {
        "1" -> Icons.Outlined.AccountBalance
        "2" -> Icons.Outlined.CreditCard
        "3" -> Icons.Outlined.Savings
        "4" -> Icons.AutoMirrored.Outlined.TrendingUp
        "5" -> Icons.Outlined.Receipt
        "6" -> Icons.Outlined.Inventory
        "7" -> Icons.Outlined.Build
        "8" -> Icons.Outlined.Description
        "9" -> Icons.Outlined.Description
        else -> Icons.Outlined.AccountBalance
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: PucViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val allAccounts by viewModel.allAccountsState.collectAsStateWithLifecycle()
    val flatSearchAccounts by viewModel.accounts.collectAsStateWithLifecycle()
    val recentAccounts by viewModel.recentAccounts.collectAsStateWithLifecycle()

    var navigationStack by remember { mutableStateOf<List<CatalogDestination>>(listOf(CatalogDestination.Classes)) }
    val currentDestination = navigationStack.lastOrNull() ?: CatalogDestination.Classes

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    // Handle system back button for drill-down navigation and search
    BackHandler(enabled = searchQuery.isNotEmpty() || navigationStack.size > 1) {
        if (searchQuery.isNotEmpty()) {
            viewModel.onSearchQueryChanged("")
        } else if (navigationStack.size > 1) {
            navigationStack = navigationStack.dropLast(1)
        }
    }

    val quickSearchTags = listOf(
        "1105 Caja",
        "1110 Bancos",
        "1305 Clientes",
        "1435 Mercancías",
        "2205 Proveedores",
        "2365 Retefuente",
        "2408 IVA",
        "3105 Capital",
        "4135 Ventas",
        "5105 Nómina"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CleanPaperBackground)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // Ultra-compact Global Search Bar
        Surface(
            color = CleanPaperSurface,
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFFC8E6C9),
                    shape = RoundedCornerShape(12.dp)
                )
                .testTag("catalog_search_bar")
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = {
                    Text(
                        text = "Buscar por código (ej: 1105) o concepto...",
                        fontSize = 12.sp,
                        color = SoftCharcoalTextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = MintGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.onSearchQueryChanged("") },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar",
                                tint = SoftCharcoalTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (searchQuery.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Resultados de búsqueda (${flatSearchAccounts.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftCharcoalTextSecondary
                )
                TextButton(
                    onClick = { viewModel.onSearchQueryChanged("") },
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Text("Volver al Catálogo", fontSize = 11.sp, color = MintGreenPrimary)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(flatSearchAccounts, key = { it.code }) { acc ->
                    val theme = getPucClassTheme(acc.code)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val crumbs = viewModel.getBreadcrumbsForAccount(acc)
                                viewModel.selectAccountForDetail(acc)
                                navigationStack = navigationStack + CatalogDestination.Detail(acc, crumbs)
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanPaperSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, theme.borderColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = theme.accentColor,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(width = 50.dp, height = 32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = acc.code,
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
                                    text = acc.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = SoftCharcoalText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Nivel: ${acc.level.name}",
                                        fontSize = 11.sp,
                                        color = SoftCharcoalTextMuted
                                    )
                                    NatureBadge(nature = acc.nature)
                                }
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                                contentDescription = null,
                                tint = SoftCharcoalTextMuted
                            )
                        }
                    }
                }
            }
        } else {
            // Compact Quick Search Tags & Recent Accounts in a single ultra-slim scrollable row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (recentAccounts.isNotEmpty()) {
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, MintGreenPrimary)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable { viewModel.selectAccountForDetail(recentAccounts.first()) }
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.History, contentDescription = null, modifier = Modifier.size(12.dp), tint = MintGreenPrimary)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Reciente: ${recentAccounts.first().code}",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintGreenPrimary
                            )
                        }
                    }
                }

                quickSearchTags.forEach { tag ->
                    val codeOnly = tag.split(" ").first()
                    Surface(
                        color = Color(0xFFF6F8F6),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE0E6E2)),
                        modifier = Modifier.clickable {
                            viewModel.onSearchQueryChanged(codeOnly)
                        }
                    ) {
                        Text(
                            text = tag,
                            fontSize = 10.5.sp,
                            color = SoftCharcoalTextSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Animated full-screen drill-down page container
            AnimatedContent(
                targetState = currentDestination,
                transitionSpec = {
                    fadeIn() + slideInHorizontally { it / 4 } togetherWith fadeOut() + slideOutHorizontally { -it / 4 }
                },
                label = "catalogNavigation"
            ) { dest ->
                when (dest) {
                    is CatalogDestination.Classes -> {
                        val classes = remember(allAccounts) { viewModel.getClasses() }
                        CatalogClassesPage(
                            classes = classes,
                            viewModel = viewModel,
                            onSelectClass = { classAccount ->
                                navigationStack = navigationStack + CatalogDestination.Groups(classAccount)
                            }
                        )
                    }
                    is CatalogDestination.Groups -> {
                        val groups = remember(allAccounts, dest.classAccount.code) { viewModel.getGroupsForClass(dest.classAccount.code) }
                        CatalogGroupsPage(
                            classAccount = dest.classAccount,
                            groups = groups,
                            viewModel = viewModel,
                            onBack = {
                                if (navigationStack.size > 1) {
                                    navigationStack = navigationStack.dropLast(1)
                                }
                            },
                            onSelectGroup = { groupAccount ->
                                navigationStack = navigationStack + CatalogDestination.Accounts(groupAccount, dest.classAccount)
                            }
                        )
                    }
                    is CatalogDestination.Accounts -> {
                        val accounts = remember(allAccounts, dest.groupAccount.code) { viewModel.getAccountsForGroup(dest.groupAccount.code) }
                        CatalogAccountsPage(
                            groupAccount = dest.groupAccount,
                            classAccount = dest.classAccount,
                            accounts = accounts,
                            viewModel = viewModel,
                            onBack = {
                                if (navigationStack.size > 1) {
                                    navigationStack = navigationStack.dropLast(1)
                                }
                            },
                            onSelectAccount = { account ->
                                val crumbs = viewModel.getBreadcrumbsForAccount(account)
                                viewModel.selectAccountForDetail(account)
                                navigationStack = navigationStack + CatalogDestination.Detail(account, crumbs)
                            }
                        )
                    }
                    is CatalogDestination.Detail -> {
                        val subaccounts = remember(allAccounts, dest.account.code) { viewModel.getSubaccountsForAccount(dest.account.code) }
                        CatalogAccountDetailPage(
                            account = dest.account,
                            subaccounts = subaccounts,
                            breadcrumbPath = dest.breadcrumbPath,
                            onBack = {
                                if (navigationStack.size > 1) {
                                    navigationStack = navigationStack.dropLast(1)
                                }
                            },
                            onJumpToLevel = { targetIndex ->
                                if (targetIndex >= 0 && targetIndex < navigationStack.size) {
                                    navigationStack = navigationStack.take(targetIndex + 1)
                                }
                            },
                            onSelectSubaccount = { subAcc ->
                                val crumbs = viewModel.getBreadcrumbsForAccount(subAcc)
                                viewModel.selectAccountForDetail(subAcc)
                                navigationStack = navigationStack + CatalogDestination.Detail(subAcc, crumbs)
                            },
                            onCopyCode = { code ->
                                clipboardManager.setText(AnnotatedString(code))
                                Toast.makeText(context, "Código $code copiado", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Page 1: Classes (Root) with Custom Icons & Polished UI
// -------------------------------------------------------------
@Composable
fun CatalogClassesPage(
    classes: List<PucAccount>,
    viewModel: PucViewModel,
    onSelectClass: (PucAccount) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(classes, key = { it.code }) { classAccount ->
                val theme = getPucClassTheme(classAccount.code)
                val microGuide = remember(classAccount.code) {
                    viewModel.getMicroGuide(classAccount.code, classAccount.name, classAccount.nature)
                }
                val groupCount = viewModel.getGroupsForClass(classAccount.code).size
                val classIcon = getClassIcon(classAccount.code)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectClass(classAccount) }
                        .testTag("class_card_${classAccount.code}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = theme.backgroundColor),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, theme.borderColor),
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
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = classIcon,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    color = theme.accentColor.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "Clase ${classAccount.code}",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.5.sp,
                                        color = theme.accentColor,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                                NatureBadge(nature = classAccount.nature)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = classAccount.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
                                color = SoftCharcoalText
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = "$groupCount grupos • ${microGuide.take(55)}...",
                                fontSize = 11.sp,
                                color = SoftCharcoalTextSecondary,
                                maxLines = 1
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                            contentDescription = "Abrir",
                            tint = theme.accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Page 2: Groups with Polished Header & Cards
// -------------------------------------------------------------
@Composable
fun CatalogGroupsPage(
    classAccount: PucAccount,
    groups: List<PucAccount>,
    viewModel: PucViewModel,
    onBack: () -> Unit,
    onSelectGroup: (PucAccount) -> Unit
) {
    val theme = getPucClassTheme(classAccount.code)
    val classIcon = getClassIcon(classAccount.code)

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = theme.backgroundColor,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, theme.borderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = theme.accentColor
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(imageVector = classIcon, contentDescription = null, tint = theme.accentColor, modifier = Modifier.size(15.dp))
                        Text(
                            text = "Clase ${classAccount.code} • ${classAccount.name}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = theme.accentColor
                        )
                    }
                    Text(
                        text = "Seleccione un Grupo Contable",
                        fontSize = 10.5.sp,
                        color = SoftCharcoalTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(groups, key = { it.code }) { groupAccount ->
                val microGuide = remember(groupAccount.code) {
                    viewModel.getMicroGuide(groupAccount.code, groupAccount.name, groupAccount.nature)
                }
                val accCount = viewModel.getAccountsForGroup(groupAccount.code).size

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectGroup(groupAccount) }
                        .testTag("group_card_${groupAccount.code}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanPaperSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CleanPaperBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = theme.accentColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(width = 46.dp, height = 32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = groupAccount.code,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = theme.accentColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = groupAccount.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = SoftCharcoalText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$accCount cuentas • $microGuide",
                                fontSize = 11.sp,
                                color = SoftCharcoalTextSecondary,
                                maxLines = 1
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                            contentDescription = null,
                            tint = SoftCharcoalTextMuted
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Page 3: Accounts (4 Digits)
// -------------------------------------------------------------
@Composable
fun CatalogAccountsPage(
    groupAccount: PucAccount,
    classAccount: PucAccount,
    accounts: List<PucAccount>,
    viewModel: PucViewModel,
    onBack: () -> Unit,
    onSelectAccount: (PucAccount) -> Unit
) {
    val theme = getPucClassTheme(classAccount.code)

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = CleanPaperSurface,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CleanPaperBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = theme.accentColor
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${classAccount.name} > ${groupAccount.code} ${groupAccount.name}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.accentColor,
                        maxLines = 1
                    )
                    Text(
                        text = "Seleccione una Cuenta (4 dígitos)",
                        fontSize = 10.5.sp,
                        color = SoftCharcoalTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(accounts, key = { it.code }) { account ->
                val microGuide = remember(account.code) {
                    viewModel.getMicroGuide(account.code, account.name, account.nature)
                }
                val subCount = viewModel.getSubaccountsForAccount(account.code).size

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectAccount(account) }
                        .testTag("account_card_${account.code}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanPaperCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4EAE5)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(width = 50.dp, height = 32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = account.code,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MintGreenPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = account.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = SoftCharcoalText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$subCount subcuentas • $microGuide",
                                fontSize = 11.sp,
                                color = SoftCharcoalTextSecondary,
                                maxLines = 1
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                            contentDescription = null,
                            tint = SoftCharcoalTextMuted
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Page 4: Account Detail & Subaccounts with Interactive Breadcrumbs
// -------------------------------------------------------------
@Composable
fun CatalogAccountDetailPage(
    account: PucAccount,
    subaccounts: List<PucAccount>,
    breadcrumbPath: List<Pair<String, String>>,
    onBack: () -> Unit,
    onJumpToLevel: (Int) -> Unit,
    onSelectSubaccount: (PucAccount) -> Unit,
    onCopyCode: (String) -> Unit
) {
    val theme = getPucClassTheme(account.code)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Interactive Breadcrumb Header
        Surface(
            color = CleanPaperSurface,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CleanPaperBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(30.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = theme.accentColor
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    breadcrumbPath.forEachIndexed { index, (code, _) ->
                        val isLast = index == breadcrumbPath.size - 1
                        TextButton(
                            onClick = { if (!isLast) onJumpToLevel(index) },
                            enabled = !isLast,
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = code,
                                fontSize = 11.sp,
                                fontWeight = if (isLast) FontWeight.Bold else FontWeight.Medium,
                                color = if (isLast) theme.accentColor else SoftCharcoalTextSecondary,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        if (!isLast) {
                            Text(">", fontSize = 10.sp, color = SoftCharcoalTextMuted)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CleanPaperSurface),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, theme.borderColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = theme.accentColor,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = account.code,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    NatureBadge(nature = account.nature)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = account.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SoftCharcoalText
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = CleanPaperBorder)
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Descripción Oficial (Decreto 2650 de 1993)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftCharcoalTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = account.description.ifBlank { "Sin descripción oficial registrada para esta cuenta." },
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = SoftCharcoalText
                )

                if (account.debitDynamic.isNotBlank() || account.creditDynamic.isNotBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    if (account.debitDynamic.isNotBlank()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DynamicDebitBlockBg),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DynamicDebitBlockBorder)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Dinámica Débito",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DynamicDebitBlockTitle
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = account.debitDynamic,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp,
                                    color = SoftCharcoalText
                                )
                            }
                        }
                    }
                    if (account.creditDynamic.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DynamicCreditBlockBg),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DynamicCreditBlockBorder)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Dinámica Crédito",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DynamicCreditBlockTitle
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = account.creditDynamic,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp,
                                    color = SoftCharcoalText
                                )
                            }
                        }
                    }
                }

                if (subaccounts.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = CleanPaperBorder)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Subcuentas y Auxiliares (${subaccounts.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SoftCharcoalTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    subaccounts.forEach { sub ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onSelectSubaccount(sub) },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = CleanPaperCard),
                            border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE4EAE5))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = sub.code,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    color = MintGreenPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = sub.name,
                                    fontSize = 12.sp,
                                    color = SoftCharcoalText,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                                    contentDescription = null,
                                    tint = SoftCharcoalTextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    onClick = { onCopyCode(account.code) },
                    color = MintGreenPrimary,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Copiar Código (${account.code})",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NatureBadge(nature: PucNature) {
    val isDebit = nature == PucNature.DEBITO
    val bg = if (isDebit) NatureDebitBg else NatureCreditBg
    val textCol = if (isDebit) NatureDebitText else NatureCreditText
    Surface(
        color = bg,
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = if (isDebit) "Naturaleza: Débito" else "Naturaleza: Crédito",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textCol,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}
