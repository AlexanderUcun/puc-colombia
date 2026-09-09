package com.example.data

import com.example.model.WithholdingRule

object TaxData {

    // Colombian Tax Rates & Withholding Rules (Estatuto Tributario)
    val withholdingRules: List<WithholdingRule> = listOf(
        WithholdingRule(
            concept = "Compras generales de mercancías",
            baseUvt = 27.0,
            baseCopApprox = 1270000L, // ~27 UVT
            rateDeclarer = 2.5,
            rateNonDeclarer = 3.5,
            pucCodeDeclarer = "236540",
            pucCodeNonDeclarer = "236540",
            notes = "Aplica sobre valor antes de IVA. Persona jurídica compradora retiene a vendedor declarante (2.5%) o no declarante (3.5%)."
        ),
        WithholdingRule(
            concept = "Servicios generales (Mano de obra sin factor intelectual preponderante)",
            baseUvt = 4.0,
            baseCopApprox = 188000L, // ~4 UVT
            rateDeclarer = 4.0,
            rateNonDeclarer = 6.0,
            pucCodeDeclarer = "236525",
            pucCodeNonDeclarer = "236525",
            notes = "Servicios técnicos, aseo, vigilancia, transporte. 4% para declarantes, 6% para no declarantes."
        ),
        WithholdingRule(
            concept = "Honorarios y comisiones (Personas jurídicas o naturales con profesionales)",
            baseUvt = 0.0,
            baseCopApprox = 0L, // 100% de la base
            rateDeclarer = 10.0,
            rateNonDeclarer = 11.0,
            pucCodeDeclarer = "236515",
            pucCodeNonDeclarer = "236515",
            notes = "Servicios calificados donde predomina el factor intelectual (abogados, contadores, médicos, consultores). 10% si declara renta, 11% si no declara."
        ),
        WithholdingRule(
            concept = "Arrendamiento de bienes inmuebles (Locales, bodegas, oficinas)",
            baseUvt = 27.0,
            baseCopApprox = 1270000L,
            rateDeclarer = 3.5,
            rateNonDeclarer = 3.5,
            pucCodeDeclarer = "236530",
            pucCodeNonDeclarer = "236530",
            notes = "Tarifa del 3.5% sobre el canon antes de IVA para inmuebles comerciales."
        ),
        WithholdingRule(
            concept = "Arrendamiento de bienes muebles (Vehículos, maquinaria, equipos)",
            baseUvt = 0.0,
            baseCopApprox = 0L,
            rateDeclarer = 4.0,
            rateNonDeclarer = 4.0,
            pucCodeDeclarer = "236530",
            pucCodeNonDeclarer = "236530",
            notes = "Tarifa del 4% sin base mínima para muebles o maquinaria."
        ),
        WithholdingRule(
            concept = "Impuesto sobre las Ventas (IVA)",
            baseUvt = 0.0,
            baseCopApprox = 0L,
            rateDeclarer = 19.0,
            rateNonDeclarer = 19.0,
            pucCodeDeclarer = "240801",
            pucCodeNonDeclarer = "240802",
            notes = "Tarifa general 19%. Venta = IVA Generado (240801 CR). Compra con IVA descontable = 240802 DB."
        ),
        WithholdingRule(
            concept = "Retención de IVA (ReteIVA)",
            baseUvt = 0.0,
            baseCopApprox = 0L,
            rateDeclarer = 15.0, // 15% del IVA
            rateNonDeclarer = 15.0,
            pucCodeDeclarer = "236701",
            pucCodeNonDeclarer = "135517",
            notes = "Equivale al 15% del valor del IVA facturado. Practicada por Grandes Contribuyentes a Responsables de IVA ordinarios."
        ),
        WithholdingRule(
            concept = "Retención de ICA (ReteICA)",
            baseUvt = 0.0,
            baseCopApprox = 0L,
            rateDeclarer = 0.966, // 9.66 por mil promedio industrial/servicios
            rateNonDeclarer = 0.966,
            pucCodeDeclarer = "236801",
            pucCodeNonDeclarer = "135518",
            notes = "Impuesto municipal liquidado por mil (‰). Varía según el municipio (Bogotá, Medellín, Cali, Barranquilla) y la actividad económica."
        )
    )
}
