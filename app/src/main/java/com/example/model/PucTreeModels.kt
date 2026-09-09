package com.example.model

data class PucSubaccountNode(
    val account: PucAccount,
    val microGuide: String,
    val hasMatch: Boolean = false
)

data class PucAccountNode(
    val account: PucAccount,
    val microGuide: String,
    val subaccounts: List<PucSubaccountNode> = emptyList(),
    val isExpanded: Boolean = false,
    val hasMatch: Boolean = false
)

data class PucGroupNode(
    val account: PucAccount,
    val microGuide: String,
    val accounts: List<PucAccountNode> = emptyList(),
    val isExpanded: Boolean = false,
    val hasMatch: Boolean = false
)

data class PucClassNode(
    val account: PucAccount,
    val microGuide: String,
    val groups: List<PucGroupNode> = emptyList(),
    val isExpanded: Boolean = false,
    val hasMatch: Boolean = false
)
