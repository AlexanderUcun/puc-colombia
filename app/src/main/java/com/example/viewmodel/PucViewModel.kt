package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.PucRepository
import com.example.model.PucAccount
import com.example.model.PucClassNode
import com.example.model.PucTreeBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class PucViewModel(application: Application) : AndroidViewModel(application) {

    val repository = PucRepository(application.applicationContext, viewModelScope)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedClassFilter = MutableStateFlow("ALL")
    val selectedClassFilter: StateFlow<String> = _selectedClassFilter.asStateFlow()

    private val _selectedAccountForDetail = MutableStateFlow<PucAccount?>(null)
    val selectedAccountForDetail: StateFlow<PucAccount?> = _selectedAccountForDetail.asStateFlow()

    private val _expandedOverrides = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val expandedOverrides: StateFlow<Map<String, Boolean>> = _expandedOverrides.asStateFlow()

    private val _recentAccounts = MutableStateFlow<List<PucAccount>>(emptyList())
    val recentAccounts: StateFlow<List<PucAccount>> = _recentAccounts.asStateFlow()

    // All accounts flow for tree building and breadcrumb lookup
    val allAccountsState: StateFlow<List<PucAccount>> = repository.getAllAccounts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    // Flat accounts stream
    @OptIn(ExperimentalCoroutinesApi::class)
    val accounts: StateFlow<List<PucAccount>> = combine(
        _searchQuery,
        _selectedClassFilter
    ) { query, classFilter ->
        Pair(query, classFilter)
    }.flatMapLatest { (query, classFilter) ->
        repository.search(query, classFilter)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Reactive hierarchical Tree Nodes for Accordion View
    val treeNodes: StateFlow<List<PucClassNode>> = combine(
        allAccountsState,
        _searchQuery,
        _expandedOverrides,
        _selectedClassFilter
    ) { allAccounts, query, overrides, classFilter ->
        PucTreeBuilder.buildTree(allAccounts, query, overrides, classFilter)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        // When query changes, reset manual overrides so auto-expansion logic takes over smoothly
        if (newQuery.isNotEmpty()) {
            _expandedOverrides.value = emptyMap()
        }
    }

    fun onClassFilterChanged(newClass: String) {
        _selectedClassFilter.value = newClass
    }

    fun toggleNode(code: String, currentlyExpanded: Boolean) {
        val current = _expandedOverrides.value.toMutableMap()
        current[code] = !currentlyExpanded
        _expandedOverrides.value = current
    }

    fun expandAll() {
        val current = _expandedOverrides.value.toMutableMap()
        for (i in 1..9) {
            current[i.toString()] = true
        }
        _expandedOverrides.value = current
    }

    fun collapseAll() {
        _expandedOverrides.value = emptyMap()
    }

    fun selectAccountForDetail(account: PucAccount?) {
        _selectedAccountForDetail.value = account
        if (account != null) {
            val currentList = _recentAccounts.value.toMutableList()
            // Remove if existing to put it at the front (most recent)
            currentList.removeAll { it.code == account.code }
            currentList.add(0, account)
            // Keep at most 8 recent accounts
            if (currentList.size > 8) {
                _recentAccounts.value = currentList.take(8)
            } else {
                _recentAccounts.value = currentList
            }
        }
    }

    fun clearRecentAccounts() {
        _recentAccounts.value = emptyList()
    }

    /**
     * Resolves the hierarchical breadcrumb path for a given account code.
     * Returns list of pairs: (code, name) for [Clase, Grupo, Cuenta, Subcuenta]
     */
    fun getBreadcrumbsForAccount(account: PucAccount): List<Pair<String, String>> {
        val all = allAccountsState.value
        val code = account.code
        val crumbs = mutableListOf<Pair<String, String>>()

        // 1. Clase (1 digit)
        if (code.isNotEmpty()) {
            val classCode = code.take(1)
            val classAcc = all.firstOrNull { it.code == classCode }
            val className = classAcc?.name ?: when (classCode) {
                "1" -> "ACTIVO"
                "2" -> "PASIVO"
                "3" -> "PATRIMONIO"
                "4" -> "INGRESOS"
                "5" -> "GASTOS"
                "6" -> "COSTOS DE VENTAS"
                "7" -> "COSTOS DE PRODUCCIÓN"
                "8" -> "CTAS. ORDEN DEUDORAS"
                "9" -> "CTAS. ORDEN ACREEDORAS"
                else -> "CLASE $classCode"
            }
            crumbs.add(Pair(classCode, className))
        }

        // 2. Grupo (2 digits)
        if (code.length >= 2) {
            val groupCode = code.take(2)
            val groupAcc = all.firstOrNull { it.code == groupCode }
            val groupName = groupAcc?.name ?: "Grupo $groupCode"
            crumbs.add(Pair(groupCode, groupName))
        }

        // 3. Cuenta (4 digits)
        if (code.length >= 4) {
            val accCode = code.take(4)
            val acc = all.firstOrNull { it.code == accCode }
            val accName = acc?.name ?: "Cuenta $accCode"
            crumbs.add(Pair(accCode, accName))
        }

        // 4. Subcuenta (6+ digits)
        if (code.length >= 6) {
            crumbs.add(Pair(account.code, account.name))
        }

        return crumbs
    }
}
