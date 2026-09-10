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

    private val _recentAccounts = MutableStateFlow<List<PucAccount>>(emptyList())
    val recentAccounts: StateFlow<List<PucAccount>> = _recentAccounts.asStateFlow()

    // All accounts flow for instant in-memory filtering
    val allAccountsState: StateFlow<List<PucAccount>> = repository.getAllAccounts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    // Flat accounts stream for global search
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

    // High-performance direct drill-down getters (O(N) in-memory filtering without heavy tree generation)
    fun getClasses(): List<PucAccount> {
        val all = allAccountsState.value
        return (1..9).map { i ->
            val code = i.toString()
            all.firstOrNull { it.code == code } ?: PucAccount(
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
        return allAccountsState.value.filter { it.code.length == 2 && it.code.startsWith(classCode) }.sortedBy { it.code }
    }

    fun getAccountsForGroup(groupCode: String): List<PucAccount> {
        return allAccountsState.value.filter { it.code.length == 4 && it.code.startsWith(groupCode) }.sortedBy { it.code }
    }

    fun getSubaccountsForAccount(accountCode: String): List<PucAccount> {
        return allAccountsState.value.filter { it.code.length >= 6 && it.code.startsWith(accountCode) }.sortedBy { it.code }
    }

    fun getMicroGuide(code: String, name: String, nature: PucNature): String {
        return PucExplanationHelper.getMicroGuide(code, name, nature)
    }

    /**
     * Resolves the hierarchical breadcrumb path for a given account code.
     */
    fun getBreadcrumbsForAccount(account: PucAccount): List<Pair<String, String>> {
        val all = allAccountsState.value
        val code = account.code
        val crumbs = mutableListOf<Pair<String, String>>()

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

        if (code.length >= 2) {
            val groupCode = code.take(2)
            val groupAcc = all.firstOrNull { it.code == groupCode }
            val groupName = groupAcc?.name ?: "Grupo $groupCode"
            crumbs.add(Pair(groupCode, groupName))
        }

        if (code.length >= 4) {
            val accCode = code.take(4)
            val acc = all.firstOrNull { it.code == accCode }
            val accName = acc?.name ?: "Cuenta $accCode"
            crumbs.add(Pair(accCode, accName))
        }

        if (code.length >= 6) {
            crumbs.add(Pair(account.code, account.name))
        }

        return crumbs
    }
}
