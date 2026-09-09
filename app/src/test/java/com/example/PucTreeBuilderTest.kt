package com.example

import com.example.model.PucAccount
import com.example.model.PucLevel
import com.example.model.PucNature
import com.example.model.PucTreeBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PucTreeBuilderTest {

    private val sampleAccounts = listOf(
        PucAccount("1", "ACTIVO", PucLevel.CLASE, PucNature.DEBITO, "Bienes"),
        PucAccount("11", "DISPONIBLE", PucLevel.GRUPO, PucNature.DEBITO, "Efectivo"),
        PucAccount("1105", "Caja", PucLevel.CUENTA, PucNature.DEBITO, "Caja física"),
        PucAccount("110505", "Caja general", PucLevel.SUBCUENTA, PucNature.DEBITO, "Efectivo general"),
        PucAccount("2", "PASIVO", PucLevel.CLASE, PucNature.CREDITO, "Obligaciones"),
        PucAccount("22", "PROVEEDORES", PucLevel.GRUPO, PucNature.CREDITO, "Deudas con proveedores"),
        PucAccount("2205", "Proveedores nacionales", PucLevel.CUENTA, PucNature.CREDITO, "Deudas comerciales"),
        PucAccount("220505", "Nacionales", PucLevel.SUBCUENTA, PucNature.CREDITO, "Proveedores país")
    )

    @Test
    fun testBuildTreeStructure() {
        val tree = PucTreeBuilder.buildTree(sampleAccounts, "", emptyMap())
        assertEquals(9, tree.size) // Classes 1 through 9

        val class1 = tree.first { it.account.code == "1" }
        assertEquals(1, class1.groups.size)

        val group11 = class1.groups.first()
        assertEquals("11", group11.account.code)
        assertEquals(1, group11.accounts.size)

        val acc1105 = group11.accounts.first()
        assertEquals("1105", acc1105.account.code)
        assertEquals(1, acc1105.subaccounts.size)
        assertEquals("110505", acc1105.subaccounts.first().account.code)
    }

    @Test
    fun testSearchAutoExpansion() {
        val tree = PucTreeBuilder.buildTree(sampleAccounts, "caja", emptyMap())

        // Class 1 contains Caja, so it must be present and auto-expanded
        val class1 = tree.firstOrNull { it.account.code == "1" }
        assertTrue(class1 != null)
        assertTrue(class1!!.isExpanded)
        assertTrue(class1.hasMatch)

        val group11 = class1.groups.first()
        assertTrue(group11.isExpanded)

        val acc1105 = group11.accounts.first()
        assertTrue(acc1105.hasMatch)
    }

    @Test
    fun testSearchNumericCode() {
        val tree = PucTreeBuilder.buildTree(sampleAccounts, "2205", emptyMap())

        val class2 = tree.firstOrNull { it.account.code == "2" }
        assertTrue(class2 != null)
        assertTrue(class2!!.isExpanded)

        val group22 = class2.groups.first()
        assertTrue(group22.isExpanded)

        val acc2205 = group22.accounts.first()
        assertEquals("2205", acc2205.account.code)
        assertTrue(acc2205.hasMatch)
    }
}
