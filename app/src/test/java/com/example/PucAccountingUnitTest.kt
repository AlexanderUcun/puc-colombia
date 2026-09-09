package com.example

import com.example.data.PucCatalog
import com.example.data.TaxData
import com.example.model.PucLevel
import com.example.model.PucNature
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PucAccountingUnitTest {

    @Test
    fun `verify essential accounts exist in PUC catalog`() {
        val caja = PucCatalog.findByCode("110505")
        assertNotNull(caja)
        assertEquals("Caja general", caja?.name)
        assertEquals(PucNature.DEBITO, caja?.nature)
        assertEquals(PucLevel.SUBCUENTA, caja?.level)

        val bancos = PucCatalog.findByCode("111005")
        assertNotNull(bancos)
        assertEquals(PucNature.DEBITO, bancos?.nature)

        val ivaGenerado = PucCatalog.findByCode("240801")
        assertNotNull(ivaGenerado)
        assertEquals(PucNature.CREDITO, ivaGenerado?.nature)

        val ivaDescontable = PucCatalog.findByCode("240802")
        assertNotNull(ivaDescontable)
        assertEquals(PucNature.DEBITO, ivaDescontable?.nature)

        val retencionCompras = PucCatalog.findByCode("236540")
        assertNotNull(retencionCompras)
        assertEquals(PucNature.CREDITO, retencionCompras?.nature)
    }

    @Test
    fun `verify search finds accounts by name and code`() {
        val ivaResults = PucCatalog.search("IVA")
        assertTrue(ivaResults.isNotEmpty())
        assertTrue(ivaResults.any { it.code.startsWith("2408") })

        val class1Results = PucCatalog.search("", "1")
        assertTrue(class1Results.all { it.code.startsWith("1") })
    }

    @Test
    fun `verify withholding tax rules data`() {
        val rules = TaxData.withholdingRules
        assertTrue(rules.isNotEmpty())

        val comprasDeclarante = rules.firstOrNull { it.concept.contains("Compras generales", ignoreCase = true) }
        assertNotNull(comprasDeclarante)
        assertEquals(2.5, comprasDeclarante?.rateDeclarer ?: 0.0, 0.01)
        assertEquals("236540", comprasDeclarante?.pucCodeDeclarer)
    }
}
