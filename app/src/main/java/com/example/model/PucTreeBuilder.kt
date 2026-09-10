package com.example.model

import java.text.Normalizer

object PucTreeBuilder {

    private val accentRegex = "\\p{InCombiningDiacriticalMarks}+".toRegex()

    fun normalize(text: String): String {
        val decomposed = Normalizer.normalize(text.lowercase(), Normalizer.Form.NFD)
        return accentRegex.replace(decomposed, "")
    }

    fun buildTree(
        allAccounts: List<PucAccount>,
        query: String,
        expandedOverrides: Map<String, Boolean>,
        classFilter: String = "ALL"
    ): List<PucClassNode> {
        val uniqueAccounts = allAccounts.distinctBy { it.code }
        val trimmedQuery = query.trim()
        val normalizedQuery = normalize(trimmedQuery)
        val isSearching = normalizedQuery.isNotEmpty()

        // Filter by class if specified (e.g. "1", "2", etc.)
        val activeClassCodes = if (classFilter.isBlank() || classFilter.equals("ALL", ignoreCase = true)) {
            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
        } else {
            listOf(classFilter)
        }

        // Group accounts by length / code
        val classesRaw = uniqueAccounts.filter { it.code.length == 1 }.sortedBy { it.code }
        val groupsByClass = uniqueAccounts.filter { it.code.length == 2 }.groupBy { it.code.take(1) }
        val accountsByGroup = uniqueAccounts.filter { it.code.length == 4 }.groupBy { it.code.take(2) }
        val subaccountsByAccount = uniqueAccounts.filter { it.code.length >= 6 }.groupBy { it.code.take(4) }

        fun matchesQuery(account: PucAccount, guide: String): Boolean {
            if (!isSearching) return false
            val normCode = normalize(account.code)
            val normName = normalize(account.name)
            val normGuide = normalize(guide)
            return normCode.contains(normalizedQuery) ||
                   normName.contains(normalizedQuery) ||
                   normGuide.contains(normalizedQuery)
        }

        val resultClasses = mutableListOf<PucClassNode>()

        for (cCode in activeClassCodes) {
            val classAccount = classesRaw.firstOrNull { it.code == cCode } ?: PucAccount(
                code = cCode,
                name = when (cCode) {
                    "1" -> "ACTIVO"
                    "2" -> "PASIVO"
                    "3" -> "PATRIMONIO"
                    "4" -> "INGRESOS"
                    "5" -> "GASTOS"
                    "6" -> "COSTOS DE VENTAS"
                    "7" -> "COSTOS DE PRODUCCIÓN O DE OPERACIÓN"
                    "8" -> "CUENTAS DE ORDEN DEUDORAS"
                    "9" -> "CUENTAS DE ORDEN ACREEDORAS"
                    else -> "CLASE $cCode"
                },
                level = PucLevel.CLASE,
                nature = if (cCode in listOf("1", "5", "6", "7", "8")) PucNature.DEBITO else PucNature.CREDITO,
                description = "Clase oficial del Plan Único de Cuentas."
            )

            val classGuide = PucExplanationHelper.getMicroGuide(classAccount.code, classAccount.name, classAccount.nature)
            val classMatchesSelf = matchesQuery(classAccount, classGuide)

            val rawGroups = groupsByClass[cCode]?.sortedBy { it.code } ?: emptyList()
            val groupNodes = mutableListOf<PucGroupNode>()

            for (groupAcc in rawGroups) {
                val groupGuide = PucExplanationHelper.getMicroGuide(groupAcc.code, groupAcc.name, groupAcc.nature)
                val groupMatchesSelf = matchesQuery(groupAcc, groupGuide)

                val rawAccounts = accountsByGroup[groupAcc.code]?.sortedBy { it.code } ?: emptyList()
                val accountNodes = mutableListOf<PucAccountNode>()

                for (acc in rawAccounts) {
                    val accGuide = PucExplanationHelper.getMicroGuide(acc.code, acc.name, acc.nature)
                    val accMatchesSelf = matchesQuery(acc, accGuide)

                    val rawSubs = subaccountsByAccount[acc.code]?.sortedBy { it.code } ?: emptyList()
                    val subNodes = mutableListOf<PucSubaccountNode>()

                    for (sub in rawSubs) {
                        val subGuide = PucExplanationHelper.getMicroGuide(sub.code, sub.name, sub.nature)
                        val subMatches = matchesQuery(sub, subGuide)
                        if (!isSearching || subMatches || accMatchesSelf || groupMatchesSelf || classMatchesSelf) {
                            subNodes.add(
                                PucSubaccountNode(
                                    account = sub,
                                    microGuide = subGuide,
                                    hasMatch = subMatches
                                )
                            )
                        }
                    }

                    val hasSubMatch = subNodes.any { it.hasMatch }
                    val accountHasMatch = accMatchesSelf || hasSubMatch

                    if (!isSearching || accountHasMatch || groupMatchesSelf || classMatchesSelf) {
                        val autoExpandAccount = isSearching && (hasSubMatch || accMatchesSelf)
                        val isAccExpanded = expandedOverrides[acc.code] ?: autoExpandAccount

                        accountNodes.add(
                            PucAccountNode(
                                account = acc,
                                microGuide = accGuide,
                                subaccounts = subNodes,
                                isExpanded = isAccExpanded,
                                hasMatch = accountHasMatch
                            )
                        )
                    }
                }

                val hasAccountMatch = accountNodes.any { it.hasMatch }
                val groupHasMatch = groupMatchesSelf || hasAccountMatch

                if (!isSearching || groupHasMatch || classMatchesSelf) {
                    val autoExpandGroup = isSearching && (hasAccountMatch || groupMatchesSelf)
                    val isGroupExpanded = expandedOverrides[groupAcc.code] ?: autoExpandGroup

                    groupNodes.add(
                        PucGroupNode(
                            account = groupAcc,
                            microGuide = groupGuide,
                            accounts = accountNodes,
                            isExpanded = isGroupExpanded,
                            hasMatch = groupHasMatch
                        )
                    )
                }
            }

            val hasGroupMatch = groupNodes.any { it.hasMatch }
            val classHasMatch = classMatchesSelf || hasGroupMatch

            if (!isSearching || classHasMatch) {
                val autoExpandClass = isSearching && (hasGroupMatch || classMatchesSelf)
                val isClassExpanded = expandedOverrides[cCode] ?: autoExpandClass

                resultClasses.add(
                    PucClassNode(
                        account = classAccount,
                        microGuide = classGuide,
                        groups = groupNodes,
                        isExpanded = isClassExpanded,
                        hasMatch = classHasMatch
                    )
                )
            }
        }

        return resultClasses
    }
}
