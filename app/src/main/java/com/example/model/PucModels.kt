package com.example.model

enum class PucNature(val displayName: String) {
    DEBITO("Débito"),
    CREDITO("Crédito")
}

enum class PucLevel(val displayName: String) {
    CLASE("Clase (1 dígito)"),
    GRUPO("Grupo (2 dígitos)"),
    CUENTA("Cuenta (4 dígitos)"),
    SUBCUENTA("Subcuenta (6 dígitos)"),
    AUXILIAR("Auxiliar (8 dígitos)")
}

data class PucAccount(
    val code: String,
    val name: String,
    val level: PucLevel,
    val nature: PucNature,
    val description: String,
    val debitDynamic: String = "",
    val creditDynamic: String = "",
    val parentCode: String? = null,
    val isFavorite: Boolean = false
)

data class JournalEntryLine(
    val code: String,
    val accountName: String,
    val debit: Double,
    val credit: Double,
    val natureReason: String = ""
)

data class AccountingTransactionResult(
    val rawText: String = "",
    val summary: String = "",
    val entries: List<JournalEntryLine> = emptyList(),
    val dynamicsJustification: String = "",
    val taxesAndWithholdings: String = "",
    val taxNotes: String = "",
    val totalDebit: Double = 0.0,
    val totalCredit: Double = 0.0,
    val isBalanced: Boolean = true,
    val isOfflineGenerated: Boolean = false
)

data class WithholdingRule(
    val concept: String,
    val baseUvt: Double,
    val baseCopApprox: Long,
    val rateDeclarer: Double,
    val rateNonDeclarer: Double,
    val pucCodeDeclarer: String,
    val pucCodeNonDeclarer: String,
    val notes: String = ""
)
