package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.PucRepository
import com.example.model.PucAccount
import com.example.model.PucExplanationHelper
import com.example.model.PucLevel
import com.example.model.PucNature
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PucViewModel(application: Application) : AndroidViewModel(application) {

    val repository = PucRepository(application.applicationContext, viewModelScope)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedClassFilter = MutableStateFlow("ALL")
    val selectedClassFilter: StateFlow<String> = _selectedClassFilter.asStateFlow()

    private val _selectedAccountForDetail = MutableStateFlow<PucAccount?>(null)
    val selectedAccountForDetail: StateFlow<PucAccount?> = _selectedAccountForDetail.asStateFlow()

    private val _recentAccounts = MutableStateFlow<List<PucAccount>>(emptyList())
    val recentAccounts: StateFlow<List<PucAccount>> = _recentAccounts.asStateFlow()

    // All accounts map flow for fast O(1) breadcrumb lookup and caching
    val allAccountsMapState: StateFlow<Map<String, PucAccount>> = repository.getAllAccounts()
        .map { list -> list.distinctBy { it.code }.associateBy { it.code } }
        .flowOn(kotlinx.coroutines.Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyMap()
        )

    val allAccountsState: StateFlow<List<PucAccount>> = repository.getAllAccounts()
        .flowOn(kotlinx.coroutines.Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val isDataLoaded: StateFlow<Boolean> = allAccountsState
        .map { it.isNotEmpty() }
        .flowOn(kotlinx.coroutines.Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    val favoriteAccounts: StateFlow<List<PucAccount>> = repository.getFavoriteAccounts()
        .flowOn(kotlinx.coroutines.Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavorite(account: PucAccount) {
        viewModelScope.launch {
            repository.toggleFavorite(account.code, !account.isFavorite)
        }
    }

    fun getAccountFlow(code: String): Flow<PucAccount?> {
        return allAccountsState.map { list -> list.find { it.code == code } }
    }

    fun openAccountByCode(code: String, onNavigate: (PucAccount) -> Unit) {
        viewModelScope.launch {
            var acc = repository.getAccountByCode(code)
            if (acc == null) {
                val common = com.example.model.CommonAccountsHelper.commonAccountsList.find { it.code == code }
                if (common != null) {
                    acc = PucAccount(
                        code = common.code,
                        name = common.name,
                        level = when(common.code.length) {
                            1 -> PucLevel.CLASE
                            2 -> PucLevel.GRUPO
                            4 -> PucLevel.CUENTA
                            6 -> PucLevel.SUBCUENTA
                            else -> PucLevel.AUXILIAR
                        },
                        nature = common.nature,
                        description = "${common.description}\n\nDinámica Débito:\n${common.debitDynamic}\n\nDinámica Crédito:\n${common.creditDynamic}\n\nEjemplo Práctico:\n${common.practicalExample}",
                        debitDynamic = common.debitDynamic,
                        creditDynamic = common.creditDynamic
                    )
                }
            } else {
                val common = com.example.model.CommonAccountsHelper.commonAccountsList.find { it.code == code }
                if (common != null && acc.debitDynamic.isBlank()) {
                    acc = acc.copy(
                        description = "${acc.description}\n\nDinámica Débito:\n${common.debitDynamic}\n\nDinámica Crédito:\n${common.creditDynamic}\n\nEjemplo Práctico:\n${common.practicalExample}",
                        debitDynamic = common.debitDynamic,
                        creditDynamic = common.creditDynamic
                    )
                }
            }
            if (acc != null) {
                selectAccountForDetail(acc)
                onNavigate(acc)
            }
        }
    }

    // Flat accounts stream for global search
    @OptIn(ExperimentalCoroutinesApi::class)
    val accounts: StateFlow<List<PucAccount>> = combine(
        _searchQuery,
        _selectedClassFilter
    ) { query, classFilter ->
        Pair(query, classFilter)
    }.flatMapLatest { (query, classFilter) ->
        repository.search(query, classFilter)
    }.flowOn(kotlinx.coroutines.Dispatchers.IO)
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onClassFilterChanged(newClass: String) {
        _selectedClassFilter.value = newClass
    }

    fun selectAccountForDetail(account: PucAccount?) {
        _selectedAccountForDetail.value = account
        if (account != null) {
            val currentList = _recentAccounts.value.toMutableList()
            currentList.removeAll { it.code == account.code }
            currentList.add(0, account)
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

    // High-performance reactive database query flows
    fun getClasses(): List<PucAccount> {
        val map = allAccountsMapState.value
        return (1..9).map { i ->
            val code = i.toString()
            map[code] ?: PucAccount(
                code = code,
                name = when (code) {
                    "1" -> "ACTIVO"
                    "2" -> "PASIVO"
                    "3" -> "PATRIMONIO"
                    "4" -> "INGRESOS"
                    "5" -> "GASTOS"
                    "6" -> "COSTOS DE VENTAS"
                    "7" -> "COSTOS DE PRODUCCIÓN O DE OPERACIÓN"
                    "8" -> "CUENTAS DE ORDEN DEUDORAS"
                    "9" -> "CUENTAS DE ORDEN ACREEDORAS"
                    else -> "CLASE $code"
                },
                level = PucLevel.CLASE,
                nature = if (code in listOf("1", "5", "6", "7", "8")) PucNature.DEBITO else PucNature.CREDITO,
                description = "Clase oficial del Plan Único de Cuentas."
            )
        }
    }

    fun getGroupsForClass(classCode: String): List<PucAccount> {
        val all = allAccountsState.value
        return all.filter { it.code.startsWith(classCode) && it.code.length > 1 && it.code.length <= 3 }.distinctBy { it.code }.sortedBy { it.code }
    }

    fun getAccountsForGroup(groupCode: String): List<PucAccount> {
        val all = allAccountsState.value
        return all.filter { it.code.startsWith(groupCode) && it.code != groupCode && it.code.length <= 5 }.distinctBy { it.code }.sortedBy { it.code }
    }

    fun getSubaccountsForAccount(accountCode: String): List<PucAccount> {
        val all = allAccountsState.value
        return all.filter { it.code.startsWith(accountCode) && it.code != accountCode }.distinctBy { it.code }.sortedBy { it.code }
    }

    fun getGroupsForClassFlow(classCode: String): Flow<List<PucAccount>> {
        return repository.getGroupsForClass(classCode)
    }

    fun getAccountsForGroupFlow(groupCode: String): Flow<List<PucAccount>> {
        return repository.getAccountsForGroup(groupCode)
    }

    fun getSubaccountsForAccountFlow(accountCode: String): Flow<List<PucAccount>> {
        return repository.getSubaccountsForAccount(accountCode)
    }

    fun getMicroGuide(code: String, name: String, nature: PucNature): String {
        return PucExplanationHelper.getMicroGuide(code, name, nature)
    }

    /**
     * Resolves the hierarchical breadcrumb path for a given account code in O(1) time.
     */
    fun getBreadcrumbsForAccount(account: PucAccount): List<Pair<String, String>> {
        val map = allAccountsMapState.value
        val code = account.code
        val crumbs = mutableListOf<Pair<String, String>>()

        if (code.isNotEmpty()) {
            val classCode = code.take(1)
            val className = map[classCode]?.name ?: when (classCode) {
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

        if (code.length >= 2) {
            val groupCode = code.take(2)
            val groupName = map[groupCode]?.name ?: "Grupo $groupCode"
            crumbs.add(Pair(groupCode, groupName))
        }

        if (code.length >= 4) {
            val accCode = code.take(4)
            val accName = map[accCode]?.name ?: "Cuenta $accCode"
            crumbs.add(Pair(accCode, accName))
        }

        if (code.length >= 6) {
            crumbs.add(Pair(account.code, account.name))
        }

        return crumbs
    }
}
