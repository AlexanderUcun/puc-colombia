package com.example.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.model.CommonAccountsHelper
import com.example.model.PucAccount
import com.example.model.PucExplanationHelper
import com.example.model.PucLevel
import com.example.model.PucNature

@Entity(
    tableName = "puc_accounts",
    indices = [
        Index(value = ["code"], unique = true),
        Index(value = ["parent_code"]),
        Index(value = ["level"]),
        Index(value = ["is_favorite"])
    ]
)
data class PucAccountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    val rowid: Int = 0,
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "level")
    val level: String,
    @ColumnInfo(name = "nature")
    val nature: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "debit_dynamic")
    val debitDynamic: String = "",
    @ColumnInfo(name = "credit_dynamic")
    val creditDynamic: String = "",
    @ColumnInfo(name = "parent_code")
    val parentCode: String? = null,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
) {
    fun toDomain(): PucAccount {
        val parsedLevel = try {
            PucLevel.valueOf(level)
        } catch (e: Exception) {
            when (code.length) {
                1 -> PucLevel.CLASE
                2 -> PucLevel.GRUPO
                4 -> PucLevel.CUENTA
                6 -> PucLevel.SUBCUENTA
                else -> PucLevel.AUXILIAR
            }
        }
        val parsedNature = if (nature.equals("CREDITO", ignoreCase = true) || nature.equals("Crédito", ignoreCase = true)) {
            PucNature.CREDITO
        } else {
            PucNature.DEBITO
        }

        val commonInfo = CommonAccountsHelper.commonAccountsList.find { it.code == code }
        val finalDesc = if (commonInfo != null) {
            "${commonInfo.description}\n\n📌 ¿Por qué se Debita?\n${commonInfo.debitDynamic}\n\n📌 ¿Por qué se Credita?\n${commonInfo.creditDynamic}\n\n💡 Ejemplo Práctico:\n${commonInfo.practicalExample}"
        } else if (description.isBlank() || description == "null") {
            PucExplanationHelper.getMicroGuide(code, name, parsedNature)
        } else {
            description
        }

        val finalDebit = commonInfo?.debitDynamic ?: (if (debitDynamic.isBlank() || debitDynamic == "null") "" else debitDynamic)
        val finalCredit = commonInfo?.creditDynamic ?: (if (creditDynamic.isBlank() || creditDynamic == "null") "" else creditDynamic)

        return PucAccount(
            code = code,
            name = name,
            level = parsedLevel,
            nature = parsedNature,
            description = finalDesc,
            debitDynamic = finalDebit,
            creditDynamic = finalCredit,
            parentCode = parentCode,
            isFavorite = isFavorite
        )
    }
}

@Entity(tableName = "puc_accounts_fts")
@Fts4(contentEntity = PucAccountEntity::class)
data class PucAccountFts(
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String
)
