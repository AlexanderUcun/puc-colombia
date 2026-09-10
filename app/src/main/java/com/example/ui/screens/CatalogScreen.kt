package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
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
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Global Search Bar
        Surface(
            color = CleanPaperSurface,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.2.dp,
                    color = Color(0xFFC8E6C9),
                    shape = RoundedCornerShape(16.dp)
                )
                .testTag("catalog_search_bar")
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = {
                    Text(
                        text = "Buscar por código (ej: 1105) o concepto...",
                        fontSize = 13.sp,
                        color = SoftCharcoalTextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = MintGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar",
                                tint = SoftCharcoalTextMuted,
                                modifier = Modifier.size(18.dp)
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
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

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
                TextButton(onClick = { viewModel.onSearchQueryChanged("") }) {
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
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanPaperSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, theme.borderColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = theme.accentColor,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.size(width = 46.dp, height = 30.dp)
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
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = acc.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = SoftCharcoalText
                                )
                                Text(
                                    text = "Nivel: ${acc.level.name}",
                                    fontSize = 11.sp,
                                    color = SoftCharcoalTextMuted
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
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                quickSearchTags.forEach { tag ->
                    val codeOnly = tag.split(" ").first()
                    Surface(
                        color = Color(0xFFF6F8F6),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE0E6E2)),
                        modifier = Modifier.clickable {
                            viewModel.onSearchQueryChanged(codeOnly)
                        }
                    ) {
                        Text(
                            text = tag,
                            fontSize = 11.sp,
                            color = SoftCharcoalTextSecondary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (recentAccounts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = SoftCharcoalTextMuted
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Recientes",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SoftCharcoalTextSecondary
                        )
                    }
                    TextButton(
                        onClick = { viewModel.clearRecentAccounts() },
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) {
                        Text("Borrar", fontSize = 10.sp, color = SoftCharcoalTextMuted)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    recentAccounts.forEach { recentAcc ->
                        val theme = getPucClassTheme(recentAcc.code)
                        Surface(
                            color = CleanPaperSurface,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(0.8.dp, theme.borderColor),
                            modifier = Modifier.clickable {
                                val crumbs = viewModel.getBreadcrumbsForAccount(recentAcc)
                                viewModel.selectAccountForDetail(recentAcc)
                                navigationStack = navigationStack + CatalogDestination.Detail(recentAcc, crumbs)
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = recentAcc.code,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = theme.accentColor
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = recentAcc.name,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    color = SoftCharcoalText
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (val dest = currentDestination) {
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

@Composable
fun CatalogClassesPage(
    classes: List<PucAccount>,
    viewModel: PucViewModel,
    onSelectClass: (PucAccount) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Seleccione una Clase Contable (PUC)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = SoftCharcoalTextSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(classes, key = { it.code }) { classAccount ->
                val theme = getPucClassTheme(classAccount.code)
                val microGuide = remember(classAccount.code) {
                    viewModel.getMicroGuide(classAccount.code, classAccount.name, classAccount.nature)
                }
                val groupCount = viewModel.getGroupsForClass(classAccount.code).size

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
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = theme.accentColor,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = classAccount.code,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = classAccount.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = SoftCharcoalText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$groupCount grupos contables • ${microGuide.take(65)}...",
                                fontSize = 11.5.sp,
                                color = SoftCharcoalTextSecondary,
                                maxLines = 1
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                            contentDescription = "Abrir",
                            tint = theme.accentColor,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CatalogGroupsPage(
    classAccount: PucAccount,
    groups: List<PucAccount>,
    viewModel: PucViewModel,
    onBack: () -> Unit,
    onSelectGroup: (PucAccount) -> Unit
) {
    val theme = getPucClassTheme(classAccount.code)

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = theme.backgroundColor,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, theme.borderColor),
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
                        text = "Clase ${classAccount.code} • ${classAccount.name}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.accentColor
                    )
                    Text(
                        text = "Seleccione un Grupo",
                        fontSize = 11.sp,
                        color = SoftCharcoalTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

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
                    border = androidx.compose.foundation.BorderStroke(1.dp, CleanPaperBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFFF0F4F1),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(width = 46.dp, height = 34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = groupAccount.code,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = theme.accentColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = groupAccount.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
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
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.accentColor,
                        maxLines = 1
                    )
                    Text(
                        text = "Seleccione una Cuenta (4 dígitos)",
                        fontSize = 11.sp,
                        color = SoftCharcoalTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

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
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanPaperCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4EAE5))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.size(width = 52.dp, height = 32.dp)
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

                        Spacer(modifier = Modifier.width(12.dp))

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

@Composable
fun CatalogAccountDetailPage(
    account: PucAccount,
    subaccounts: List<PucAccount>,
    breadcrumbPath: List<Pair<String, String>>,
    onBack: () -> Unit,
    onSelectSubaccount: (PucAccount) -> Unit,
    onCopyCode: (String) -> Unit
) {
    val theme = getPucClassTheme(account.code)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
                        text = breadcrumbPath.joinToString(" > ") { it.second },
                        fontSize = 10.5.sp,
                        color = SoftCharcoalTextMuted,
                        maxLines = 1
                    )
                    Text(
                        text = "${account.code} - ${account.name}",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = SoftCharcoalText,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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
                        Spacer(modifier = Modifier.width(6.dp))
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
