package com.example.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.PucCatalog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Database(
    entities = [PucAccountEntity::class, PucAccountFts::class],
    version = 7,
    exportSchema = false
)
abstract class PucDatabase : RoomDatabase() {

    abstract fun pucDao(): PucDao

    companion object {
        private const val TAG = "PucDatabase"
        private val populateMutex = Mutex()

        @Volatile
        private var INSTANCE: PucDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): PucDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PucDatabase::class.java,
                    "puc_colombia.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(PucDatabaseCallback(context.applicationContext, scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class PucDatabaseCallback(
            private val context: Context,
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(context, database.pucDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(context: Context, dao: PucDao) {
            populateMutex.withLock {
                try {
                    val existingCodes = mutableSetOf<String>()
                    val accountsToInsert = mutableListOf<PucAccountEntity>()

                    // 1. Load from assets/puc.json
                    try {
                        val jsonString = context.assets.open("puc.json").bufferedReader().use { it.readText() }
                        val jsonArray = JSONArray(jsonString)
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val code = obj.getString("code")
                            if (!existingCodes.contains(code)) {
                                existingCodes.add(code)
                                val desc = obj.optString("description", "").let { if (it.isNullOrBlank() || it == "null") "" else it }
                                val debit = obj.optString("debitDynamic", "").let { if (it.isNullOrBlank() || it == "null") "" else it }
                                val credit = obj.optString("creditDynamic", "").let { if (it.isNullOrBlank() || it == "null") "" else it }
                                accountsToInsert.add(
                                    PucAccountEntity(
                                        code = code,
                                        name = obj.getString("name"),
                                        level = obj.getString("level"),
                                        nature = obj.getString("nature"),
                                        description = desc,
                                        debitDynamic = debit,
                                        creditDynamic = credit,
                                        parentCode = if (obj.has("parentCode") && !obj.isNull("parentCode")) obj.getString("parentCode") else null
                                    )
                                )
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error reading assets/puc.json", e)
                    }

                    // 2. Also ensure any accounts from PucCatalog are merged
                    try {
                        for (acc in PucCatalog.accounts) {
                            if (!existingCodes.contains(acc.code)) {
                                existingCodes.add(acc.code)
                                accountsToInsert.add(
                                    PucAccountEntity(
                                        code = acc.code,
                                        name = acc.name,
                                        level = acc.level.name,
                                        nature = acc.nature.name,
                                        description = acc.description,
                                        debitDynamic = acc.debitDynamic,
                                        creditDynamic = acc.creditDynamic,
                                        parentCode = acc.parentCode
                                    )
                                )
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error loading fallback catalog accounts", e)
                    }

                    if (accountsToInsert.isNotEmpty()) {
                        dao.deleteAll()
                        dao.insertAll(accountsToInsert)
                        Log.d(TAG, "Replaced database with ${accountsToInsert.size} accounts from assets/puc.json.")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to populate database", e)
                }
            }
        }
    }
}
