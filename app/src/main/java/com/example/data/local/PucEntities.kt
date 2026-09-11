package com.example.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.model.PucAccount
import com.example.model.PucExplanationHelper
import com.example.model.PucLevel
import com.example.model.PucNature

@Entity(
    tableName = "puc_accounts",
    indices = [
        Index(value = ["code"], unique = true),
        Index(value = ["parent_code"]),
        Index(value = ["level"])
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
    val parentCode: String? = null
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

        val cleanDesc = if (description.isBlank() || description == "null") {
            PucExplanationHelper.getMicroGuide(code, name, parsedNature)
        } else {
            description
        }

        val cleanDebit = if (debitDynamic.isBlank() || debitDynamic == "null") "" else debitDynamic
        val cleanCredit = if (creditDynamic.isBlank() || creditDynamic == "null") "" else creditDynamic

        return PucAccount(
            code = code,
            name = name,
            level = parsedLevel,
            nature = parsedNature,
            description = cleanDesc,
            debitDynamic = cleanDebit,
            creditDynamic = cleanCredit,
            parentCode = parentCode
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
