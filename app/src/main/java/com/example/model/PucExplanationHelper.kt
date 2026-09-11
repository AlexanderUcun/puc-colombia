package com.example.model

/**
 * Provides 1-2 line plain-language micro-guides for Colombian PUC accounts,
 * explaining in simple words what the account represents and how it moves.
 */
object PucExplanationHelper {

    fun getMicroGuide(code: String, name: String, nature: PucNature): String {
        return when (code) {
            // Clases (1 dígito)
            "1" -> "Representa todos los bienes, dinero y derechos que posee la empresa. Aumenta con compras, cobros y recaudos; disminuye con pagos y ventas."
            "2" -> "Agrupa todas las deudas y obligaciones contraídas con bancos, proveedores, empleados y el Estado. Aumenta al endeudarse, disminuye al pagar."
            "3" -> "El patrimonio neto de los dueños o accionistas: capital aportado, reservas acumuladas y utilidades del ejercicio."
            "4" -> "Los ingresos ordinarios y extraordinarios generados por la venta de bienes o prestación de servicios del negocio."
            "5" -> "Los gastos operacionales de administración y ventas necesarios para el funcionamiento diario de la empresa."
            "6" -> "El costo de adquisición o fabricación de las mercancías y bienes vendidos a los clientes."
            "7" -> "Los costos directos e indirectos incurridos en los procesos de producción o manufactura."
            "8" -> "Cuentas de orden deudoras: derechos contingentes o compromisos que no afectan directamente el balance general."
            "9" -> "Cuentas de orden acreedoras: responsabilidades contingentes o bienes recibidos de terceros en custodia."

            // Grupos Activo (2 dígitos)
            "11" -> "Disponible: Dinero en efectivo, depósitos bancarios y equivalentes de liquidez inmediata."
            "12" -> "Inversiones: Acciones, bonos, CDT y títulos valores adquiridos para rentabilidad o control."
            "13" -> "Deudores: Cuentas por cobrar a clientes, anticipos de impuestos, empleados y socios."
            "14" -> "Inventarios: Mercancías, materias primas y productos en proceso listos para comercializar."
            "15" -> "Propiedades, Planta y Equipo: Bienes duraderos tangibles usados en la operación (terrenos, equipos, vehículos)."
            "16" -> "Intangibles: Bienes no físicos con valor económico comercial (marcas, patentes, software, franquicias)."
            "17" -> "Diferidos: Gastos pagados por anticipado pendientes de amortizar (seguros, intereses, arriendos)."
            "18" -> "Otros Activos: Bienes de arte, cultura, depósitos dados en garantía y activos diversos."
            "19" -> "Valorizaciones: Incrementos en el valor comercial de inversiones o propiedades frente a su costo en libros."

            // Grupos Pasivo (2 dígitos)
            "21" -> "Obligaciones Financieras: Pagarés, préstamos y sobregiros con bancos e instituciones de crédito."
            "22" -> "Proveedores: Facturas y cuentas por pagar a los suministradores de materias primas e insumos."
            "23" -> "Cuentas por Pagar: Costos, gastos, retenciones en la fuente y acreedores comerciales varios."
            "24" -> "Impuestos, Gravámenes y Tasas: Deudas fiscales por IVA, impuesto de renta, industria y comercio."
            "25" -> "Obligaciones Laborales: Salarios, cesantías, primas y vacaciones adeudadas a los trabajadores."
            "26" -> "Pasivos Estimados y Provisiones: Reservas para contingencias, litigios, garantías y demandas."
            "27" -> "Diferidos: Ingresos recibidos por anticipado pendientes de causar en futuros periodos."
            "28" -> "Otros Pasivos: Anticipos de clientes, depósitos recibidos y fondos de terceros."
            "29" -> "Bonos y Pagarés Emitidos: Títulos de deuda corporativa colocados en el mercado de capitales."

            // Grupos Patrimonio (2 dígitos)
            "31" -> "Capital Social: Aportes iniciales y suscritos por los socios o accionistas de la empresa."
            "32" -> "Superávit de Capital: Primas en colocación de acciones, donaciones y capital adicional."
            "33" -> "Reservas: Utilidades retenidas por mandato legal, estatutario o decisiones de la asamblea."
            "34" -> "Revalorización del Patrimonio: Ajustes por inflación acumulados del patrimonio histórico."
            "35" -> "Dividendos o Participaciones Decretados en Acciones: Acciones propias reemitidas."
            "36" -> "Resultados del Ejercicio: Utilidad o pérdida neta obtenida en el periodo contable actual."
            "37" -> "Resultados de Ejercicios Anteriores: Utilidades acumuladas o pérdidas de años pasados por distribuir."
            "38" -> "Superávit por Valorizaciones: Valorizaciones netas de inversiones y activos fijos."

            // Grupos Ingresos (2 dígitos)
            "41" -> "Operacionales: Ingresos principales por ventas y prestación de servicios del objeto social."
            "42" -> "No Operacionales: Ingresos secundarios por intereses, arriendos, comisiones o venta de activos."

            // Grupos Gastos (2 dígitos)
            "51" -> "Operacionales de Administración: Gastos de personal gerencial, papelería, servicios y arriendos de oficina."
            "52" -> "Operacionales de Ventas: Gastos directos de comercialización, publicidad, comisiones y fletes."
            "53" -> "No Operacionales: Gastos financieros, comisiones bancarias, intereses y pérdidas en baja de activos."
            "54" -> "Impuesto de Renta y Complementarios: Provisión del impuesto sobre la renta del ejercicio."
            "59" -> "Ganancias y Pérdidas: Cuenta transitoria para el cierre de ingresos, costos y gastos al final del año."

            // Grupos Costos (2 dígitos)
            "61" -> "Costo de Ventas: Valor de adquisición o producción de los bienes y servicios vendidos."
            "62" -> "Compras: Adquisiciones de materias primas, repuestos o mercancías para la venta."
            "71" -> "Materia Prima: Costo de insumos directos utilizados en la fabricación de productos."
            "72" -> "Mano de Obra Directa: Salarios y prestaciones del personal de planta en producción."
            "73" -> "Costos Indirectos: Insumos, energía, depreciaciones y servicios de la planta fabril."
            "74" -> "Contratos de Servicios: Servicios externos de fabricación o maquila."

            // Cuentas populares específicas (4 y 6 dígitos)
            "1105", "110505" -> "Caja general en efectivo. Sube al recibir pagos físicos de clientes, baja al consignar en bancos o pagar gastos menores."
            "110510" -> "Caja menor: Fondo fijo en efectivo destinado a sufragar gastos urgentes y menores de oficina."
            "1110", "111005" -> "Bancos cuenta corriente. Sube con consignaciones y transferencias recibidas, baja con cheques y pagos electrónicos."
            "1120", "112005" -> "Cuentas de ahorros bancarias. Depósitos rentables con disponibilidad inmediata de retiro."
            "1305", "130505" -> "Clientes nacionales. Cuentas comerciales por cobrar por ventas a crédito; sube al facturar, baja al recaudar."
            "1355", "135515" -> "Anticipo de impuesto de renta retenido. Saldo a favor descontable en la declaración anual de renta."
            "1435", "143501" -> "Mercancías no fabricadas por la empresa. Inventario disponible para la comercialización directa."
            "1524", "152405" -> "Muebles y enseres de oficina. Escritorios, sillas, archivos y estanterías de uso administrativo."
            "1528", "152805" -> "Equipo de computación y comunicación. Servidores, computadores, impresoras y redes."
            "1540", "154005" -> "Flota y equipo de transporte. Vehículos automotores para despacho de mercancías o gerencia."
            "2105", "210510" -> "Pagarés bancarios. Préstamos comerciales a corto o largo plazo contraídos con entidades financieras."
            "2205", "220505" -> "Proveedores nacionales. Deudas comerciales por compra de mercancías e insumos a crédito."
            "2335", "233525" -> "Costos y gastos por pagar: Honorarios profesionales causados pendientes de giro."
            "2365", "236540" -> "Retefuente en compras (2.5%). Impuesto retenido a proveedores que se traslada mensualmente a la DIAN."
            "2408", "240801" -> "IVA generado (19%). Impuesto facturado en ventas que constituye un pasivo exigible ante la DIAN."
            "240802" -> "IVA descontable. Impuesto pagado en compras que reduce el saldo neto de IVA a pagar a la DIAN."
            "2505", "250505" -> "Salarios por pagar. Remuneración neta adeudada a los trabajadores al cierre del periodo."
            "2510", "251005" -> "Cesantías consolidadas. Prestación social acumulada a favor del empleado (un mes por año)."
            "2520", "252005" -> "Prima de servicios. Prestación semestral acumulada para pago en junio y diciembre."
            "3105", "310505" -> "Capital suscrito y pagado. Aportes patrimoniales iniciales o ampliaciones de capital de los socios."
            "4135", "413501" -> "Comercio al por mayor y al por menor. Ingresos operacionales por la venta de mercancías."
            "5105", "510506" -> "Sueldos del personal administrativo. Gasto fijo por remuneración al equipo de gerencia y oficina."
            "5110", "511035" -> "Honorarios por asesorías especializadas contables, jurídicas o tributarias."
            "5120", "512010" -> "Arrendamientos de locales, oficinas o bodegas para la operación administrativa."
            "5305", "530520" -> "Gastos financieros por intereses bancarios de créditos y obligaciones."
            "6135", "613501" -> "Costo de mercancías vendidas. Valor de salida del inventario por despachos a clientes."

            else -> {
                // Inteligente según el prefijo y naturaleza contable
                when {
                    code.startsWith("11") -> "Disponible líquido: Recursos monetarios de inmediata disponibilidad en caja o bancos."
                    code.startsWith("12") -> "Inversión financiera: Títulos, acciones o valores colocados para generar rentabilidad."
                    code.startsWith("13") -> "Cuenta por cobrar comercial o fiscal: Derecho de exigir el pago a clientes o terceros."
                    code.startsWith("14") -> "Inventario de mercancías o materiales para la venta, proceso o consumo."
                    code.startsWith("15") -> "Activo fijo tangible: Bien raíz, maquinaria o equipo empleado en la actividad social."
                    code.startsWith("16") -> "Activo intangible: Derechos comerciales, marcas o licencias con valor económico."
                    code.startsWith("17") -> "Diferido activo: Erogaciones pagadas por anticipado pendientes de causar."
                    code.startsWith("18") || code.startsWith("19") -> "Activo complementario: Bienes diversos, depósitos o valorizaciones patrimoniales."
                    code.startsWith("21") -> "Obligación financiera: Deuda formalizada con bancos o entidades de crédito."
                    code.startsWith("22") -> "Cuenta por pagar comercial: Deuda con proveedores de bienes o materias primas."
                    code.startsWith("23") -> "Cuenta por pagar general: Acreedores varios, honorarios o retenciones fiscales."
                    code.startsWith("24") -> "Impuesto exigible: Gravamen fiscal adeudado a la administración tributaria."
                    code.startsWith("25") -> "Pasivo laboral: Prestaciones y salarios consolidados a favor de los trabajadores."
                    code.startsWith("26") || code.startsWith("27") -> "Pasivo estimado o diferido: Provisiones por contingencias o ingresos anticipados."
                    code.startsWith("28") || code.startsWith("29") -> "Pasivo diverso: Fondos de terceros, depósitos o bonos corporativos emitidos."
                    code.startsWith("3") -> "Cuenta patrimonial: Representa la participación residual de los propietarios en los activos."
                    code.startsWith("4") -> "Cuenta de ingresos: Incrementos en los beneficios económicos operacionales o financieros."
                    code.startsWith("5") -> "Cuenta de gastos: Erogaciones operacionales administrativas o de ventas del periodo."
                    code.startsWith("6") -> "Cuenta de costos: Erogación directa vinculada a la adquisición o venta de bienes."
                    code.startsWith("7") -> "Cuenta de costos de producción: Insumos y mano de obra aplicados a la manufactura."
                    code.startsWith("8") || code.startsWith("9") -> "Cuenta de orden: Control interno de derechos, contingencias o responsabilidades."
                    nature == PucNature.DEBITO -> "Cuenta de naturaleza Débito: Incrementa su saldo por el Debe y disminuye por el Haber."
                    else -> "Cuenta de naturaleza Crédito: Incrementa su saldo por el Haber y disminuye por el Debe."
                }
            }
        }
    }
}
