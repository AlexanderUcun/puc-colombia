package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PucDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<PucAccountEntity>)

    @Query("SELECT COUNT(*) FROM puc_accounts")
    suspend fun getAccountCount(): Int

    @Query("SELECT * FROM puc_accounts ORDER BY CAST(code AS INTEGER), code ASC")
    fun getAllAccounts(): Flow<List<PucAccountEntity>>

    @Query("SELECT * FROM puc_accounts WHERE code = :code LIMIT 1")
    suspend fun getAccountByCode(code: String): PucAccountEntity?

    @Query("SELECT * FROM puc_accounts WHERE LENGTH(code) = 1 ORDER BY CAST(code AS INTEGER), code ASC")
    fun getClasses(): Flow<List<PucAccountEntity>>

    @Query("SELECT * FROM puc_accounts WHERE LENGTH(code) = 2 AND code LIKE :classCode || '%' ORDER BY CAST(code AS INTEGER), code ASC")
    fun getGroupsForClass(classCode: String): Flow<List<PucAccountEntity>>

    @Query("SELECT * FROM puc_accounts WHERE LENGTH(code) = 4 AND code LIKE :groupCode || '%' ORDER BY CAST(code AS INTEGER), code ASC")
    fun getAccountsForGroup(groupCode: String): Flow<List<PucAccountEntity>>

    @Query("SELECT * FROM puc_accounts WHERE LENGTH(code) >= 6 AND code LIKE :accountCode || '%' ORDER BY CAST(code AS INTEGER), code ASC")
    fun getSubaccountsForAccount(accountCode: String): Flow<List<PucAccountEntity>>

    @Query("""
        SELECT * FROM puc_accounts 
        WHERE code LIKE :query || '%' 
           OR name LIKE '%' || :query || '%'
           OR description LIKE '%' || :query || '%'
        ORDER BY CAST(code AS INTEGER), code ASC
    """)
    fun searchAccounts(query: String): Flow<List<PucAccountEntity>>

    @Query("""
        SELECT puc_accounts.* FROM puc_accounts
        JOIN puc_accounts_fts ON puc_accounts.rowid = puc_accounts_fts.rowid
        WHERE puc_accounts_fts MATCH :matchQuery
        ORDER BY CAST(puc_accounts.code AS INTEGER), puc_accounts.code ASC
    """)
    fun searchAccountsFts(matchQuery: String): Flow<List<PucAccountEntity>>

    @Query("""
        SELECT * FROM puc_accounts 
        WHERE code LIKE :classDigit || '%'
        ORDER BY CAST(code AS INTEGER), code ASC
    """)
    fun getAccountsByClass(classDigit: String): Flow<List<PucAccountEntity>>
}
