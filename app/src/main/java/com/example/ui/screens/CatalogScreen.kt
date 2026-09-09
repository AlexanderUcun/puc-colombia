package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.PucAccount
import com.example.model.PucAccountNode
import com.example.model.PucClassNode
import com.example.model.PucGroupNode
import com.example.model.PucNature
import com.example.model.PucSubaccountNode
import com.example.model.PucTreeBuilder
import com.example.ui.theme.AccountingCredit
import com.example.ui.theme.AccountingDebit
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
import com.example.ui.theme.TreeLineColor
import com.example.ui.theme.getPucClassTheme
import com.example.viewmodel.PucViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: PucViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val treeNodes by viewModel.treeNodes.collectAsStateWithLifecycle()
    val selectedAccountForDetail by viewModel.selectedAccountForDetail.collectAsStateWithLifecycle()

    val selectedClassFilter by viewModel.selectedClassFilter.collectAsStateWithLifecycle()
    val recentAccounts by viewModel.recentAccounts.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val sheetState = rememberModalBottomSheetState()

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

    val classFilterChips = listOf(
        Pair("ALL", "Todas"),
        Pair("1", "1 Activo"),
        Pair("2", "2 Pasivo"),
        Pair("3", "3 Patrimonio"),
        Pair("4", "4 Ingresos"),
        Pair("5", "5 Gastos"),
        Pair("6", "6 Costo Ventas"),
        Pair("7", "7 Costo Prod."),
        Pair("8", "8 Orden Deud."),
        Pair("9", "9 Orden Acreed.")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CleanPaperBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Minimalist Search Bar with pure white background & mint green border
        Surface(
            color = CleanPaperSurface,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.2.dp,
                    color = Color(0xFFC8E6C9), // Light mint green border
                    shape = RoundedCornerShape(16.dp)
                )
                .testTag("catalog_search_bar")
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = {
                    Text(
                        text = "Buscar por código (ej: 1105) o concepto (caja, iva, banco)...",
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
                                contentDescription = "Limpiar búsqueda",
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

        // Horizontal Row of Quick Filter Chips (Clases del PUC)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .testTag("class_filter_chips_row"),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            classFilterChips.forEach { (code, label) ->
                val isSelected = selectedClassFilter.equals(code, ignoreCase = true)
                val chipTheme = if (code == "ALL") null else getPucClassTheme(code)
                val activeColor = chipTheme?.accentColor ?: MintGreenPrimary

                Surface(
                    color = if (isSelected) (chipTheme?.backgroundColor ?: Color(0xFFE8F5E9)) else CleanPaperSurface,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 1.2.dp else 0.8.dp,
                        color = if (isSelected) activeColor else Color(0xFFE2E7E3)
                    ),
                    modifier = Modifier
                        .clickable {
                            val newFilter = if (isSelected && code != "ALL") "ALL" else code
                            viewModel.onClassFilterChanged(newFilter)
                        }
                        .testTag("filter_chip_$code")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        if (code != "ALL") {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(activeColor, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                        Text(
                            text = label,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) activeColor else SoftCharcoalTextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Quick Search Suggestions Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            quickSearchTags.forEach { tag ->
                val codeOnly = tag.split(" ").first()
                val isSelected = searchQuery.contains(codeOnly)
                Surface(
                    color = if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF6F8F6),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 0.8.dp,
                        color = if (isSelected) MintGreenPrimary else Color(0xFFE0E6E2)
                    ),
                    modifier = Modifier.clickable {
                        viewModel.onSearchQueryChanged(if (isSelected) "" else codeOnly)
                    }
                ) {
                    Text(
                        text = tag,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MintGreenPrimary else SoftCharcoalTextSecondary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Section: "Consultadas recientemente" (Visible if user has opened accounts)
        if (recentAccounts.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
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
                        text = "Consultadas recientemente",
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
                        modifier = Modifier
                            .clickable { viewModel.selectAccountForDetail(recentAcc) }
                            .testTag("recent_account_${recentAcc.code}")
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

        // Header controls: Tree overview status + Expand/Collapse All
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val filterLabel = if (selectedClassFilter != "ALL") " • Clase $selectedClassFilter" else ""
            Text(
                text = if (searchQuery.isBlank()) "Estructura Oficial del PUC$filterLabel" else "Resultados de Búsqueda Jerárquica$filterLabel",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = SoftCharcoalTextSecondary
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { viewModel.expandAll() }) {
                    Icon(Icons.Default.UnfoldMore, contentDescription = null, modifier = Modifier.size(15.dp), tint = MintGreenPrimary)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Expandir", fontSize = 11.sp, color = MintGreenPrimary)
                }
                TextButton(onClick = { viewModel.collapseAll() }) {
                    Icon(Icons.Default.UnfoldLess, contentDescription = null, modifier = Modifier.size(15.dp), tint = SoftCharcoalTextMuted)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Colapsar", fontSize = 11.sp, color = SoftCharcoalTextMuted)
                }
            }
        }

        // Hierarchical Accordion Tree List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(treeNodes, key = { it.account.code }) { classNode ->
                ClassTreeNodeCard(
                    classNode = classNode,
                    searchQuery = searchQuery,
                    onToggleClass = { viewModel.toggleNode(classNode.account.code, classNode.isExpanded) },
                    onToggleGroup = { gCode, isExp -> viewModel.toggleNode(gCode, isExp) },
                    onToggleAccount = { aCode, isExp -> viewModel.toggleNode(aCode, isExp) },
                    onSelectForDetail = { acc -> viewModel.selectAccountForDetail(acc) },
                    onCopyCode = { code ->
                        clipboardManager.setText(AnnotatedString(code))
                        Toast.makeText(context, "Código $code copiado", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    // Detail Bottom Sheet for Accounting Dynamics (Decreto 2650 de 1993)
    selectedAccountForDetail?.let { acc ->
        val breadcrumbs = remember(acc.code) { viewModel.getBreadcrumbsForAccount(acc) }
        ModalBottomSheet(
            onDismissRequest = { viewModel.selectAccountForDetail(null) },
            sheetState = sheetState,
            containerColor = CleanPaperSurface
        ) {
            AccountDetailSheetContent(
                account = acc,
                breadcrumbs = breadcrumbs,
                onCopyCode = {
                    clipboardManager.setText(AnnotatedString(acc.code))
                    Toast.makeText(context, "Código ${acc.code} copiado al portapapeles", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

// -------------------------------------------------------------
// Tree Node: 1. CLASE (1 Dígito)
// -------------------------------------------------------------
@Composable
fun ClassTreeNodeCard(
    classNode: PucClassNode,
    searchQuery: String,
    onToggleClass: () -> Unit,
    onToggleGroup: (String, Boolean) -> Unit,
    onToggleAccount: (String, Boolean) -> Unit,
    onSelectForDetail: (PucAccount) -> Unit,
    onCopyCode: (String) -> Unit
) {
    val theme = getPucClassTheme(classNode.account.code)
    val chevronRotation by animateFloatAsState(targetValue = if (classNode.isExpanded) 180f else 0f, label = "chevron")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("class_card_${classNode.account.code}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = theme.backgroundColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, theme.borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Class Header Row (Clickable accordion toggle)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleClass() }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Class Code Circle Pill
                Surface(
                    color = theme.accentColor,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = classNode.account.code,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = highlightText(classNode.account.name, searchQuery, theme.accentColor),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = SoftCharcoalText
                        )
                        NatureBadge(nature = classNode.account.nature)
                    }

                    Text(
                        text = "${classNode.groups.size} grupos disponibles",
                        fontSize = 11.sp,
                        color = SoftCharcoalTextSecondary
                    )
                }

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = if (classNode.isExpanded) "Colapsar" else "Expandir",
                    tint = theme.accentColor,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(chevronRotation)
                )
            }

            // Plain-Language Micro-Guide for the Class
            Text(
                text = classNode.microGuide,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp,
                    color = SoftCharcoalTextSecondary
                ),
                modifier = Modifier.padding(start = 42.dp, top = 2.dp, bottom = 6.dp)
            )

            // Expanded Groups List with subtle vertical connector line
            AnimatedVisibility(
                visible = classNode.isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 8.dp)
                ) {
                    // Subtle vertical line representing the branch of the class
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .fillMaxHeight()
                            .background(theme.borderColor, RoundedCornerShape(1.dp))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        classNode.groups.forEach { groupNode ->
                            GroupTreeNodeCard(
                                groupNode = groupNode,
                                parentAccentColor = theme.accentColor,
                                searchQuery = searchQuery,
                                onToggleGroup = { onToggleGroup(groupNode.account.code, groupNode.isExpanded) },
                                onToggleAccount = onToggleAccount,
                                onSelectForDetail = onSelectForDetail,
                                onCopyCode = onCopyCode
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Tree Node: 2. GRUPO (2 Dígitos)
// -------------------------------------------------------------
@Composable
fun GroupTreeNodeCard(
    groupNode: PucGroupNode,
    parentAccentColor: Color,
    searchQuery: String,
    onToggleGroup: () -> Unit,
    onToggleAccount: (String, Boolean) -> Unit,
    onSelectForDetail: (PucAccount) -> Unit,
    onCopyCode: (String) -> Unit
) {
    val chevronRotation by animateFloatAsState(targetValue = if (groupNode.isExpanded) 180f else 0f, label = "chevronGroup")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
            .testTag("group_card_${groupNode.account.code}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CleanPaperSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CleanPaperBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleGroup() }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Connecting line indicator
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(24.dp)
                        .background(parentAccentColor.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Group Code
                Surface(
                    color = Color(0xFFF2F4F2),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = groupNode.account.code,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = parentAccentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = highlightText(groupNode.account.name, searchQuery, parentAccentColor),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = SoftCharcoalText
                    )
                    Text(
                        text = "${groupNode.accounts.size} cuentas a 4 dígitos",
                        fontSize = 10.5.sp,
                        color = SoftCharcoalTextMuted
                    )
                }

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = SoftCharcoalTextSecondary,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(chevronRotation)
                )
            }

            // Group Plain-Language Micro-Guide
            Text(
                text = groupNode.microGuide,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = SoftCharcoalTextSecondary
                ),
                modifier = Modifier.padding(start = 20.dp, top = 2.dp, bottom = 4.dp)
            )

            // Expanded Accounts List with subtle vertical connector line
            AnimatedVisibility(
                visible = groupNode.isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, start = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(1.5.dp)
                            .fillMaxHeight()
                            .background(parentAccentColor.copy(alpha = 0.25f), RoundedCornerShape(1.dp))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        groupNode.accounts.forEach { accountNode ->
                            AccountTreeNodeCard(
                                accountNode = accountNode,
                                parentAccentColor = parentAccentColor,
                                searchQuery = searchQuery,
                                onToggleAccount = { onToggleAccount(accountNode.account.code, accountNode.isExpanded) },
                                onSelectForDetail = onSelectForDetail,
                                onCopyCode = onCopyCode
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Tree Node: 3. CUENTA (4 Dígitos)
// -------------------------------------------------------------
@Composable
fun AccountTreeNodeCard(
    accountNode: PucAccountNode,
    parentAccentColor: Color,
    searchQuery: String,
    onToggleAccount: () -> Unit,
    onSelectForDetail: (PucAccount) -> Unit,
    onCopyCode: (String) -> Unit
) {
    val chevronRotation by animateFloatAsState(targetValue = if (accountNode.isExpanded) 180f else 0f, label = "chevronAcc")
    val hasSubaccounts = accountNode.subaccounts.isNotEmpty()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
            .testTag("account_card_${accountNode.account.code}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = CleanPaperCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4EAE5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Subtle line connector
                Text(
                    text = "├─",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = SoftCharcoalTextMuted,
                    modifier = Modifier.padding(end = 4.dp)
                )

                // Code
                Surface(
                    color = Color(0xFFEBF3EC),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = highlightText(accountNode.account.code, searchQuery, parentAccentColor),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = parentAccentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = highlightText(accountNode.account.name, searchQuery, parentAccentColor),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.5.sp,
                            color = SoftCharcoalText
                        )
                        NatureBadge(nature = accountNode.account.nature)
                    }
                }

                // Action buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Copy code button
                    IconButton(
                        onClick = { onCopyCode(accountNode.account.code) },
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("copy_code_${accountNode.account.code}")
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copiar código",
                            tint = SoftCharcoalTextMuted,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Dinámica info button
                    IconButton(
                        onClick = { onSelectForDetail(accountNode.account) },
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("info_${accountNode.account.code}")
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Ver dinámica",
                            tint = MintGreenPrimary,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    // Expand subaccounts chevron if available
                    if (hasSubaccounts) {
                        IconButton(
                            onClick = { onToggleAccount() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = SoftCharcoalTextMuted,
                                modifier = Modifier
                                    .size(18.dp)
                                    .rotate(chevronRotation)
                            )
                        }
                    }
                }
            }

            // Micro-Guide 1-2 lines in plain language
            Text(
                text = accountNode.microGuide,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = SoftCharcoalTextSecondary
                ),
                modifier = Modifier.padding(start = 22.dp, top = 2.dp, bottom = 4.dp)
            )

            // Expanded Subaccounts (6 Dígitos) with subtle vertical connector line
            if (hasSubaccounts) {
                AnimatedVisibility(
                    visible = accountNode.isExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, start = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(1.5.dp)
                                .fillMaxHeight()
                                .background(Color(0xFFD4DDD5), RoundedCornerShape(1.dp))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            accountNode.subaccounts.forEach { subNode ->
                                SubaccountTreeNodeCard(
                                    subNode = subNode,
                                    parentAccentColor = parentAccentColor,
                                    searchQuery = searchQuery,
                                    onSelectForDetail = onSelectForDetail,
                                    onCopyCode = onCopyCode
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Tree Node: 4. SUBCUENTA (6 Dígitos)
// -------------------------------------------------------------
@Composable
fun SubaccountTreeNodeCard(
    subNode: PucSubaccountNode,
    parentAccentColor: Color,
    searchQuery: String,
    onSelectForDetail: (PucAccount) -> Unit,
    onCopyCode: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp)
            .testTag("subaccount_row_${subNode.account.code}"),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFAFBF9),
        border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE8ECE7))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Connecting indentation symbol
                Text(
                    text = "└── ",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = SoftCharcoalTextMuted,
                    modifier = Modifier.padding(end = 2.dp)
                )

                // 6-digit Code
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(0.6.dp, Color(0xFFD4DDD5)),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = highlightText(subNode.account.code, searchQuery, parentAccentColor),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = parentAccentColor,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = highlightText(subNode.account.name, searchQuery, parentAccentColor),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = SoftCharcoalText
                        )
                        NatureBadge(nature = subNode.account.nature)
                    }
                }

                // Action buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onCopyCode(subNode.account.code) },
                        modifier = Modifier
                            .size(26.dp)
                            .testTag("copy_code_${subNode.account.code}")
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copiar código",
                            tint = SoftCharcoalTextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    IconButton(
                        onClick = { onSelectForDetail(subNode.account) },
                        modifier = Modifier
                            .size(26.dp)
                            .testTag("info_${subNode.account.code}")
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Dinámica",
                            tint = MintGreenPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            // Micro-Guide for subaccount
            Text(
                text = subNode.microGuide,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.5.sp,
                    lineHeight = 14.sp,
                    color = SoftCharcoalTextSecondary
                ),
                modifier = Modifier.padding(start = 24.dp, top = 2.dp)
            )
        }
    }
}

// -------------------------------------------------------------
// Minimalist Nature Badge: DÉBITO (Verde pastel) / CRÉDITO (Azul pastel)
// -------------------------------------------------------------
@Composable
fun NatureBadge(nature: PucNature, modifier: Modifier = Modifier) {
    val isDebit = nature == PucNature.DEBITO
    Surface(
        color = if (isDebit) NatureDebitBg else NatureCreditBg,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        Text(
            text = if (isDebit) "DÉBITO" else "CRÉDITO",
            color = if (isDebit) NatureDebitText else NatureCreditText,
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            letterSpacing = 0.4.sp,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
        )
    }
}

// -------------------------------------------------------------
// Accounting Dynamics Bottom Sheet (Decreto 2650 de 1993)
// -------------------------------------------------------------
@Composable
fun AccountDetailSheetContent(
    account: PucAccount,
    breadcrumbs: List<Pair<String, String>> = emptyList(),
    onCopyCode: () -> Unit
) {
    val theme = getPucClassTheme(account.code)
    var isCopied by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CleanPaperSurface)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Hierarchical Breadcrumbs (Clase > Grupo > Cuenta > Subcuenta)
        if (breadcrumbs.isNotEmpty()) {
            Surface(
                color = Color(0xFFF7FAF8),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE2EBE4)),
                modifier = Modifier.fillMaxWidth().testTag("breadcrumbs_container")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    breadcrumbs.forEachIndexed { index, (crumbCode, crumbName) ->
                        val isLast = index == breadcrumbs.lastIndex
                        val crumbTheme = getPucClassTheme(crumbCode)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = crumbCode,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (isLast) FontWeight.Bold else FontWeight.SemiBold,
                                fontSize = 11.sp,
                                color = if (isLast) crumbTheme.accentColor else SoftCharcoalTextMuted
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = crumbName,
                                fontSize = 11.sp,
                                fontWeight = if (isLast) FontWeight.Bold else FontWeight.Normal,
                                color = if (isLast) SoftCharcoalText else SoftCharcoalTextSecondary
                            )

                            if (!isLast) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(horizontal = 2.dp),
                                    tint = SoftCharcoalTextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        // Header with Code, Title and Interactive Copy Button Feedback
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = account.code,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = theme.accentColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    NatureBadge(nature = account.nature)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SoftCharcoalText
                )
                Text(
                    text = "${account.level.displayName} • Plan Único de Cuentas (Decreto 2650)",
                    fontSize = 11.sp,
                    color = SoftCharcoalTextSecondary
                )
            }

            // Visual feedback "Copiar código" Button
            Surface(
                color = if (isCopied) Color(0xFFE8F5E9) else Color(0xFFF2F4F2),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (isCopied) NatureDebitText else Color(0xFFDCE2DD)
                ),
                modifier = Modifier
                    .clickable {
                        onCopyCode()
                        isCopied = true
                        coroutineScope.launch {
                            delay(1800)
                            isCopied = false
                        }
                    }
                    .testTag("copy_code_detail_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = if (isCopied) "Copiado" else "Copiar código",
                        tint = if (isCopied) NatureDebitText else SoftCharcoalTextSecondary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isCopied) "Copiado" else "Copiar",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCopied) NatureDebitText else SoftCharcoalTextSecondary
                    )
                }
            }
        }

        HorizontalDivider(color = CleanPaperBorder)

        // Plain-Language Micro-Guide Box
        Surface(
            color = Color(0xFFF6FAF7),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCEADF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Resumen en Lenguaje Sencillo:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MintGreenPrimary
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = com.example.model.PucExplanationHelper.getMicroGuide(account.code, account.name, account.nature),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 12.5.sp,
                        color = SoftCharcoalText
                    )
                )
            }
        }

        // Official Description
        Text(
            text = "Descripción Reglamentaria:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = SoftCharcoalText
        )
        Text(
            text = account.description.ifBlank { "Cuenta oficial del Plan Único de Cuentas para comerciantes de Colombia." },
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = SoftCharcoalTextSecondary
            )
        )

        // Dinámica Contable: Bloque 1 "Se debita por" (Fondo verde pastel sutil)
        Surface(
            color = DynamicDebitBlockBg,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DynamicDebitBlockBorder),
            modifier = Modifier.fillMaxWidth().testTag("debit_dynamic_block")
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(DynamicDebitBlockTitle, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Se debita por (DB):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        color = DynamicDebitBlockTitle
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = if (account.debitDynamic.isNotBlank()) {
                        account.debitDynamic
                    } else if (account.nature == PucNature.DEBITO) {
                        "Por el valor inicial, compras, cobros, adquisición de bienes o aumento de derechos y recursos de la empresa."
                    } else {
                        "Por cancelaciones parciales o totales de obligaciones, pagos efectivos o disminución de pasivos/ingresos."
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = SoftCharcoalText
                    )
                )
            }
        }

        // Dinámica Contable: Bloque 2 "Se acredita por" (Fondo durazno/coral pastel sutil)
        Surface(
            color = DynamicCreditBlockBg,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DynamicCreditBlockBorder),
            modifier = Modifier.fillMaxWidth().testTag("credit_dynamic_block")
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(DynamicCreditBlockTitle, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Se acredita por (CR):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        color = DynamicCreditBlockTitle
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = if (account.creditDynamic.isNotBlank()) {
                        account.creditDynamic
                    } else if (account.nature == PucNature.CREDITO) {
                        "Por el valor de las obligaciones contraídas, facturas de proveedores, ventas generadas o aumentos de capital/ingresos."
                    } else {
                        "Por salidas de dinero, mermas, ventas de activos o cancelaciones al cierre del periodo contable."
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = SoftCharcoalText
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// -------------------------------------------------------------
// Search Term Highlighting
// -------------------------------------------------------------
fun highlightText(fullText: String, query: String, accentColor: Color): AnnotatedString {
    val trimmed = query.trim()
    if (trimmed.isEmpty()) return AnnotatedString(fullText)

    val normalizedFull = PucTreeBuilder.normalize(fullText)
    val normalizedQuery = PucTreeBuilder.normalize(trimmed)

    val startIndex = normalizedFull.indexOf(normalizedQuery)
    if (startIndex == -1) return AnnotatedString(fullText)

    val endIndex = (startIndex + normalizedQuery.length).coerceAtMost(fullText.length)

    return buildAnnotatedString {
        append(fullText.substring(0, startIndex))
        pushStyle(
            SpanStyle(
                background = Color(0xFFE8F5E9),
                color = NatureDebitText,
                fontWeight = FontWeight.ExtraBold
            )
        )
        append(fullText.substring(startIndex, endIndex))
        pop()
        append(fullText.substring(endIndex))
    }
}
