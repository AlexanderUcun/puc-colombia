package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Base Clean Light Theme Colors (Mint / Sage subtle paper palette)
val MintGreenPrimary = Color(0xFF4E8752)
val MintGreenOnPrimary = Color(0xFFFFFFFF)
val MintGreenPrimaryContainer = Color(0xFFE8F5E9)
val MintGreenOnPrimaryContainer = Color(0xFF1B4D20)

val SageSecondary = Color(0xFF5A9E60)
val SageOnSecondary = Color(0xFFFFFFFF)
val SageSecondaryContainer = Color(0xFFEDF7EE)
val SageOnSecondaryContainer = Color(0xFF133817)

val SoftCharcoalText = Color(0xFF1C2826)
val SoftCharcoalTextSecondary = Color(0xFF5A6A65)
val SoftCharcoalTextMuted = Color(0xFF7D8C86)

val CleanPaperBackground = Color(0xFFFBFDFB)
val CleanPaperSurface = Color(0xFFFFFFFF)
val CleanPaperCard = Color(0xFFFDFEFC)
val CleanPaperBorder = Color(0xFFE2E8E4)

// Class-specific Almost-White Pastel Colors for Hierarchy
val Class1AssetBg = Color(0xFFF0F9F2) // Activo - Menta pastel casi blanco
val Class1AssetAccent = Color(0xFF3D8B4D)
val Class1AssetBorder = Color(0xFFD6EED9)

val Class2LiabilityBg = Color(0xFFFDF5F2) // Pasivo - Durazno/coral pastel casi blanco
val Class2LiabilityAccent = Color(0xFFC25B42)
val Class2LiabilityBorder = Color(0xFFF8DDD4)

val Class3EquityBg = Color(0xFFF7F4FA) // Patrimonio - Lavanda pastel casi blanco
val Class3EquityAccent = Color(0xFF7B5EA7)
val Class3EquityBorder = Color(0xFFE9E2F1)

val Class4RevenueBg = Color(0xFFFDF9F0) // Ingresos - Ámbar/miel pastel casi blanco
val Class4RevenueAccent = Color(0xFFB88222)
val Class4RevenueBorder = Color(0xFFF8ECD2)

val Class5ExpenseBg = Color(0xFFF2F7FA) // Gastos - Azul hielo pastel casi blanco
val Class5ExpenseAccent = Color(0xFF3A7CA5)
val Class5ExpenseBorder = Color(0xFFD8E7F0)

val Class6CostSalesBg = Color(0xFFF6F7F8) // Costos Ventas - Gris perla pastel casi blanco
val Class6CostSalesAccent = Color(0xFF5A6B7C)
val Class6CostSalesBorder = Color(0xFFE2E5E8)

val Class7CostProdBg = Color(0xFFF9F8F5) // Costos Producción - Arena pastel casi blanco
val Class7CostProdAccent = Color(0xFF7D705C)
val Class7CostProdBorder = Color(0xFFEAE7DF)

val Class8DebitOrderBg = Color(0xFFF4F7F5) // Cuentas Orden Deudoras - Salvia casi blanco
val Class8DebitOrderAccent = Color(0xFF507567)
val Class8DebitOrderBorder = Color(0xFFDDE6E0)

val Class9CreditOrderBg = Color(0xFFF8F6F4) // Cuentas Orden Acreedoras - Lino casi blanco
val Class9CreditOrderAccent = Color(0xFF7A685D)
val Class9CreditOrderBorder = Color(0xFFE9E4DF)

// Nature Badges (User-specified)
val NatureDebitBg = Color(0xFFE6F4EA)
val NatureDebitText = Color(0xFF1E6B37)
val NatureCreditBg = Color(0xFFE8F0FE)
val NatureCreditText = Color(0xFF1967D2)

// Dynamics Blocks Styling (Subtle Pastel)
val DynamicDebitBlockBg = Color(0xFFF2F9F3) // Verde pastel sutil
val DynamicDebitBlockBorder = Color(0xFFCEE8D3)
val DynamicDebitBlockTitle = Color(0xFF1E6B37)

val DynamicCreditBlockBg = Color(0xFFFDF6F2) // Durazno/coral pastel sutil
val DynamicCreditBlockBorder = Color(0xFFF7D9CC)
val DynamicCreditBlockTitle = Color(0xFFB84A2A)

// Tree connection line color
val TreeLineColor = Color(0xFFD3DCD6)

// Accounting Status
val AccountingDebit = Color(0xFF1E6B37)
val AccountingCredit = Color(0xFF1967D2)
val AccountingBalanced = Color(0xFF2E7D32)
val AccountingUnbalanced = Color(0xFFD32F2F)

val ColombiaYellow = Color(0xFFFCD116)
val ColombiaBlue = Color(0xFF003893)
val ColombiaRed = Color(0xFFCE1126)

data class PucClassStyle(
    val title: String,
    val backgroundColor: Color,
    val accentColor: Color,
    val borderColor: Color
)

fun getPucClassTheme(codeOrClass: String): PucClassStyle {
    return when (codeOrClass.take(1)) {
        "1" -> PucClassStyle("1. Activo", Class1AssetBg, Class1AssetAccent, Class1AssetBorder)
        "2" -> PucClassStyle("2. Pasivo", Class2LiabilityBg, Class2LiabilityAccent, Class2LiabilityBorder)
        "3" -> PucClassStyle("3. Patrimonio", Class3EquityBg, Class3EquityAccent, Class3EquityBorder)
        "4" -> PucClassStyle("4. Ingresos", Class4RevenueBg, Class4RevenueAccent, Class4RevenueBorder)
        "5" -> PucClassStyle("5. Gastos", Class5ExpenseBg, Class5ExpenseAccent, Class5ExpenseBorder)
        "6" -> PucClassStyle("6. Costos de Ventas", Class6CostSalesBg, Class6CostSalesAccent, Class6CostSalesBorder)
        "7" -> PucClassStyle("7. Costos de Producción", Class7CostProdBg, Class7CostProdAccent, Class7CostProdBorder)
        "8" -> PucClassStyle("8. Cuentas de Orden Deudoras", Class8DebitOrderBg, Class8DebitOrderAccent, Class8DebitOrderBorder)
        "9" -> PucClassStyle("9. Cuentas de Orden Acreedoras", Class9CreditOrderBg, Class9CreditOrderAccent, Class9CreditOrderBorder)
        else -> PucClassStyle("Cuentas", CleanPaperSurface, MintGreenPrimary, CleanPaperBorder)
    }
}
