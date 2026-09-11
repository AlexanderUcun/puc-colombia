package com.example.data.local

import android.content.Context
import com.example.model.PucAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PucRepository(
    private val context: Context,
    private val scope: CoroutineScope
) {
    private val database = PucDatabase.getDatabase(context, scope)
    private val dao = database.pucDao()

    companion object {
        private const val CURRENT_DATA_VERSION = 5
        private const val PREFS_NAME = "puc_preferences"
        private const val KEY_DATA_VERSION = "puc_data_version"
    }

    init {
        // Guarantee database is populated and updated to the latest PUC standard
        scope.launch(Dispatchers.IO) {
            checkAndPopulate()
        }
    }

    private suspend fun checkAndPopulate() = withContext(Dispatchers.IO) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedVersion = prefs.getInt(KEY_DATA_VERSION, 0)
        val count = dao.getAccountCount()
        val group11 = dao.getAccountByCode("11")

        // Trigger reload if version is older, table is empty, or old CGN data is detected
        val needsUpdate = savedVersion < CURRENT_DATA_VERSION || count == 0 || group11?.name != "DISPONIBLE"
        if (needsUpdate) {
            PucDatabase.populateDatabase(context, dao)
            prefs.edit().putInt(KEY_DATA_VERSION, CURRENT_DATA_VERSION).apply()
        }
    }

    suspend fun ensurePopulated() = withContext(Dispatchers.IO) {
        checkAndPopulate()
    }

    fun getAllAccounts(): Flow<List<PucAccount>> {
        return dao.getAllAccounts()
            .map { list -> list.distinctBy { it.code }.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    fun getGroupsForClass(classCode: String): Flow<List<PucAccount>> {
        return dao.getGroupsForClass(classCode)
            .map { list -> list.distinctBy { it.code }.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    fun getAccountsForGroup(groupCode: String): Flow<List<PucAccount>> {
        return dao.getAccountsForGroup(groupCode)
            .map { list -> list.distinctBy { it.code }.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    fun getSubaccountsForAccount(accountCode: String): Flow<List<PucAccount>> {
        return dao.getSubaccountsForAccount(accountCode)
            .map { list -> list.distinctBy { it.code }.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    fun search(query: String, classFilter: String = "ALL"): Flow<List<PucAccount>> {
        val trimmed = query.trim()
        val sourceFlow = if (trimmed.isEmpty()) {
            if (classFilter == "ALL") {
                dao.getAllAccounts()
            } else {
                dao.getAccountsByClass(classFilter)
            }
        } else {
            // Check if FTS can be leveraged for multi-word or text terms
            val sanitized = trimmed.replace(Regex("[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]"), "").trim()
            if (sanitized.isNotEmpty() && !sanitized.all { it.isDigit() }) {
                // Use FTS search with prefix token matching
                val ftsQuery = sanitized.split("\\s+".toRegex()).joinToString(" ") { "$it*" }
                try {
                    dao.searchAccountsFts(ftsQuery)
                } catch (e: Exception) {
                    dao.searchAccounts(trimmed)
                }
            } else {
                dao.searchAccounts(trimmed)
            }
        }

        return sourceFlow.map { list ->
            var domainList = list.distinctBy { it.code }.map { it.toDomain() }
            if (classFilter != "ALL") {
                domainList = domainList.filter { it.code.startsWith(classFilter) }
            }
            domainList
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAccountByCode(code: String): PucAccount? = withContext(Dispatchers.IO) {
        dao.getAccountByCode(code)?.toDomain()
    }
}
