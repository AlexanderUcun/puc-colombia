package com.example.data

import com.example.model.PucAccount
import com.example.model.PucLevel
import com.example.model.PucNature

object PucCatalog {

    val accounts: List<PucAccount> = listOf(
        // ================= CLASE 1: ACTIVO =================
        PucAccount(
            code = "1",
            name = "ACTIVO",
            level = PucLevel.CLASE,
            nature = PucNature.DEBITO,
            description = "Agrupa el conjunto de las cuentas que representan los bienes y derechos tangibles e intangibles de propiedad del ente económico, que en la medida de su utilización, son fuente potencial de beneficios presentes o futuros.",
            debitDynamic = "Por el valor de los bienes comprados, derechos adquiridos o entradas de recursos a la empresa.",
            creditDynamic = "Por el valor de las salidas de bienes, cobro de derechos o consumo de recursos."
        ),
        PucAccount(
            code = "11",
            name = "DISPONIBLE",
            level = PucLevel.GRUPO,
            nature = PucNature.DEBITO,
            description = "Comprende los fondos de caja, los depósitos en cuentas corrientes y de ahorro en moneda nacional y extranjera con disponibilidad inmediata.",
            parentCode = "1"
        ),
        PucAccount(
            code = "1105",
            name = "Caja",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra la existencia en dinero efectivo o en cheques con que cuenta el ente económico, tanto en moneda nacional como extranjera.",
            debitDynamic = "Por las entradas de dinero en efectivo y los cheques o comprobantes recibidos por cualquier concepto.",
            creditDynamic = "Por el valor de los desembolsos en efectivo, consignaciones en bancos o faltantes en arqueos.",
            parentCode = "11"
        ),
        PucAccount(
            code = "110505",
            name = "Caja general",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Registra las entradas y salidas de dinero en efectivo recaudado por la empresa antes de su consignación bancaria.",
            parentCode = "1105"
        ),
        PucAccount(
            code = "110510",
            name = "Cajas menores",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Fondos fijos asignados para sufragar gastos menores o urgentes que no ameritan giro de cheque o transferencia.",
            parentCode = "1105"
        ),
        PucAccount(
            code = "1110",
            name = "Bancos",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra el valor de los depósitos constituidos por el ente económico en moneda nacional y extranjera, en bancos del país o del exterior.",
            debitDynamic = "Por los depósitos realizados mediante consignaciones en efectivo o cheques, notas crédito bancarias y rendimientos.",
            creditDynamic = "Por el valor de los cheques girados, transferencias electrónicas, notas débito por comisiones, gravamen financiero (4x1000) o cheques devueltos.",
            parentCode = "11"
        ),
        PucAccount(
            code = "111005",
            name = "Moneda nacional",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Depósitos en cuentas corrientes bancarias en moneda legal colombiana.",
            parentCode = "1110"
        ),
        PucAccount(
            code = "1120",
            name = "Cuentas de ahorro",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra la existencia de fondos a la vista o a término en instituciones financieras que generan intereses.",
            debitDynamic = "Por las consignaciones efectuadas y abonos de intereses liquidados por la entidad bancaria.",
            creditDynamic = "Por los retiros efectuados y notas débito por comisiones o transferencias.",
            parentCode = "11"
        ),
        PucAccount(
            code = "112005",
            name = "Bancos (Ahorros)",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Cuentas de ahorro en entidades financieras colombianas.",
            parentCode = "1120"
        ),
        PucAccount(
            code = "13",
            name = "DEUDORES",
            level = PucLevel.GRUPO,
            nature = PucNature.DEBITO,
            description = "Comprende el valor de las deudas a cargo de terceros y a favor del ente económico, derivadas de ventas de bienes y servicios o anticipos.",
            parentCode = "1"
        ),
        PucAccount(
            code = "1305",
            name = "Clientes",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra los valores a favor del ente económico por ventas de bienes o prestación de servicios a crédito relacionados con la actividad ordinaria.",
            debitDynamic = "Por el valor de las facturas de venta de bienes o servicios a crédito.",
            creditDynamic = "Por los pagos totales o parciales recibidos de clientes, devoluciones de mercancías o notas crédito concedidas.",
            parentCode = "13"
        ),
        PucAccount(
            code = "130505",
            name = "Nacionales",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Deudas comerciales de clientes domiciliados en Colombia.",
            parentCode = "1305"
        ),
        PucAccount(
            code = "1355",
            name = "Anticipo de impuestos y contribuciones o saldos a favor",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra los saldos a favor por concepto de anticipos de impuestos, retenciones en la fuente practicadas al ente económico y autorretenciones.",
            debitDynamic = "Por los valores retenidos al ente económico por sus clientes (Retefuente, ReteIVA, ReteICA) y por autorretenciones practicadas.",
            creditDynamic = "Por la aplicación del anticipo en la declaración tributaria respectiva o devolución efectuada por la DIAN o municipio.",
            parentCode = "13"
        ),
        PucAccount(
            code = "135515",
            name = "Retención en la fuente (Anticipo)",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Anticipo de impuesto de renta mediante retención practicada por el comprador o cliente.",
            parentCode = "1355"
        ),
        PucAccount(
            code = "135517",
            name = "Impuesto a las ventas retenido (ReteIVA)",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Anticipo del impuesto sobre las ventas retenido por clientes agentes de retención de IVA.",
            parentCode = "1355"
        ),
        PucAccount(
            code = "135518",
            name = "Impuesto de industria y comercio retenido (ReteICA)",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Anticipo de ICA retenido por compradores en el respectivo municipio.",
            parentCode = "1355"
        ),
        PucAccount(
            code = "14",
            name = "INVENTARIOS",
            level = PucLevel.GRUPO,
            nature = PucNature.DEBITO,
            description = "Comprende los bienes corporales destinados a la venta en el curso normal de los negocios o que se consumen en la producción.",
            parentCode = "1"
        ),
        PucAccount(
            code = "1435",
            name = "Mercancías no fabricadas por la empresa",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra el valor de los bienes adquiridos para su venta que no sufren ningún proceso de transformación o manufactura.",
            debitDynamic = "Por el costo de las mercancías compradas (precio factura más fletes y aranceles no recuperables) bajo sistema permanente.",
            creditDynamic = "Por el costo de las mercancías vendidas, devoluciones a proveedores o castigo por mermas/deterioro.",
            parentCode = "14"
        ),
        PucAccount(
            code = "143501",
            name = "Mercancías para la venta",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Existencias generales de productos comerciales listos para expendio.",
            parentCode = "1435"
        ),
        PucAccount(
            code = "15",
            name = "PROPIEDADES, PLANTA Y EQUIPO",
            level = PucLevel.GRUPO,
            nature = PucNature.DEBITO,
            description = "Comprende los activos tangibles adquiridos o construidos con la intención de emplearlos de forma permanente en la producción, prestación de servicios o administración.",
            parentCode = "1"
        ),
        PucAccount(
            code = "1520",
            name = "Maquinaria y equipo",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra el costo de la maquinaria utilizada en el proceso productivo u operativo.",
            debitDynamic = "Por el costo de adquisición, transporte, seguros e instalación.",
            creditDynamic = "Por venta, retiro o baja en libros.",
            parentCode = "15"
        ),
        PucAccount(
            code = "1524",
            name = "Equipo de oficina",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra el costo de los muebles, enseres y equipos mecánicos o electrónicos de oficina.",
            debitDynamic = "Por el costo de adquisición o mejoras capitalizables.",
            creditDynamic = "Por venta, permuta, retiro o desuso.",
            parentCode = "15"
        ),
        PucAccount(
            code = "152405",
            name = "Muebles y enseres",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Escritorios, sillas, archivadores, mesas y mobiliario general.",
            parentCode = "1524"
        ),
        PucAccount(
            code = "1528",
            name = "Equipo de computación y comunicación",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra el costo de adquisición del equipo de cómputo, procesamiento de datos y telecomunicaciones.",
            debitDynamic = "Por la compra e instalación de servidores, computadores, impresoras y redes.",
            creditDynamic = "Por venta, obsolescencia o retiro por daño irreparable.",
            parentCode = "15"
        ),
        PucAccount(
            code = "152805",
            name = "Equipos de procesamiento de datos",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Computadores portátiles, de escritorio, servidores y tablets de trabajo.",
            parentCode = "1528"
        ),
        PucAccount(
            code = "1540",
            name = "Flota y equipo de transporte",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Vehículos automotores propiedad del ente económico para transporte de mercancías o personal.",
            parentCode = "15"
        ),
        PucAccount(
            code = "1592",
            name = "Depreciación acumulada",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Cuenta de valuación que compensa el valor en libros de las propiedades, planta y equipo por el desgaste u obsolescencia.",
            debitDynamic = "Por la venta, desmantelamiento o baja definitiva del activo depreciado.",
            creditDynamic = "Por las alícuotas periódicas de depreciación calculadas contra gastos de administración o ventas.",
            parentCode = "15"
        ),

        // ================= CLASE 2: PASIVO =================
        PucAccount(
            code = "2",
            name = "PASIVO",
            level = PucLevel.CLASE,
            nature = PucNature.CREDITO,
            description = "Agrupa el conjunto de las cuentas que representan las obligaciones contraídas por el ente económico en desarrollo del giro ordinario de su actividad, pagaderas en dinero, bienes o servicios.",
            debitDynamic = "Por los pagos totales o parciales de las deudas u obligaciones extinguidas.",
            creditDynamic = "Por el nacimiento de la obligación, adquisición de deudas o causación de costos y pasivos."
        ),
        PucAccount(
            code = "21",
            name = "OBLIGACIONES FINANCIERAS",
            level = PucLevel.GRUPO,
            nature = PucNature.CREDITO,
            description = "Comprende las obligaciones contraídas con entidades del sector financiero mediante pagarés, cartas de crédito y préstamos.",
            parentCode = "2"
        ),
        PucAccount(
            code = "2105",
            name = "Bancos nacionales",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Registra los créditos bancarios contraídos con entidades financieras vigiladas por la Superfinanciera.",
            debitDynamic = "Por abonos a capital o amortización del préstamo.",
            creditDynamic = "Por el desembolso del crédito recibido.",
            parentCode = "21"
        ),
        PucAccount(
            code = "22",
            name = "PROVEEDORES",
            level = PucLevel.GRUPO,
            nature = PucNature.CREDITO,
            description = "Obligaciones a cargo del ente económico originadas en la adquisición de bienes o servicios para la venta o producción.",
            parentCode = "2"
        ),
        PucAccount(
            code = "2205",
            name = "Nacionales (Proveedores)",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Registra las deudas comerciales con proveedores nacionales por compras a crédito de materias primas o mercancías.",
            debitDynamic = "Por los pagos totales o abonos a facturas de proveedores, o notas débito por devoluciones de compras.",
            creditDynamic = "Por el valor de las facturas de compra a crédito de mercancías recibidas.",
            parentCode = "22"
        ),
        PucAccount(
            code = "220505",
            name = "Proveedores nacionales",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Subcuenta para registro individual de compras a crédito a proveedores locales.",
            parentCode = "2205"
        ),
        PucAccount(
            code = "23",
            name = "CUENTAS POR PAGAR",
            level = PucLevel.GRUPO,
            nature = PucNature.CREDITO,
            description = "Obligaciones por conceptos distintos de proveedores comerciales, tales como costos y gastos causados, honorarios, servicios y retenciones.",
            parentCode = "2"
        ),
        PucAccount(
            code = "2335",
            name = "Costos y gastos por pagar",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Registra los pasivos contraídos por la causación de costos y gastos devengados que se encuentran pendientes de pago.",
            debitDynamic = "Por el pago efectuado a los beneficiarios.",
            creditDynamic = "Por el valor devengado y causado de cuentas de cobro, contratos o facturas de servicios.",
            parentCode = "23"
        ),
        PucAccount(
            code = "233525",
            name = "Honorarios",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Servicios profesionales y técnicos causados pendientes de giro.",
            parentCode = "2335"
        ),
        PucAccount(
            code = "233540",
            name = "Arrendamientos",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Cánones de arrendamiento de locales, bodegas u oficinas causados.",
            parentCode = "2335"
        ),
        PucAccount(
            code = "233550",
            name = "Servicios públicos",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Facturas de energía, acueducto, telefonía e internet por pagar.",
            parentCode = "2335"
        ),
        PucAccount(
            code = "2365",
            name = "Retención en la fuente (Pasivo)",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Registra los importes recaudados por el ente económico como agente retenedor a título del impuesto sobre la renta.",
            debitDynamic = "Por el pago de la retención mensual a la DIAN mediante formulario oficial.",
            creditDynamic = "Por los valores retenidos a terceros al momento del pago o abono en cuenta por compras, servicios u honorarios.",
            parentCode = "23"
        ),
        PucAccount(
            code = "236515",
            name = "Honorarios (Retención)",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Retención a profesionales declarantes (10%) o no declarantes (11%).",
            parentCode = "2365"
        ),
        PucAccount(
            code = "236525",
            name = "Servicios (Retención)",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Retención por servicios generales a declarantes (4%) o no declarantes (6%).",
            parentCode = "2365"
        ),
        PucAccount(
            code = "236530",
            name = "Arrendamientos (Retención)",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Retención por arrendamiento de bienes muebles (4%) o inmuebles (3.5%).",
            parentCode = "2365"
        ),
        PucAccount(
            code = "236540",
            name = "Compras (Retención)",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Retención general por compras a personas jurídicas/declarantes (2.5%) o naturales no declarantes (3.5%).",
            parentCode = "2365"
        ),
        PucAccount(
            code = "2367",
            name = "Impuesto a las ventas retenido (ReteIVA pasivo)",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Registra el valor del IVA retenido a proveedores por agentes retenedores de IVA (15% del impuesto generado).",
            debitDynamic = "Por el pago de la retención en la declaración bimestral a la DIAN.",
            creditDynamic = "Por la retención efectuada al proveedor sobre el IVA facturado.",
            parentCode = "23"
        ),
        PucAccount(
            code = "236701",
            name = "ReteIVA a proveedores",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Retención del 15% del IVA facturado en compras o servicios.",
            parentCode = "2367"
        ),
        PucAccount(
            code = "2368",
            name = "Impuesto de industria y comercio retenido (ReteICA pasivo)",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Registra los valores retenidos a proveedores por concepto del impuesto municipal de Industria y Comercio.",
            debitDynamic = "Por el pago bimestral al municipio o Secretaría de Hacienda distrital.",
            creditDynamic = "Por la retención practicada sobre la base de la compra o servicio según tarifa municipal.",
            parentCode = "23"
        ),
        PucAccount(
            code = "236801",
            name = "ReteICA compras y servicios",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Retención municipal de ICA (ej. 4.14‰ a 11.04‰ según actividad económica).",
            parentCode = "2368"
        ),
        PucAccount(
            code = "2370",
            name = "Retenciones y aportes de nómina",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Obligaciones por aportes del trabajador deducidos en nómina (Salud 4%, Pensión 4%, libranzas).",
            debitDynamic = "Por el pago a los operadores de PILA (Planilla Integrada de Liquidación de Aportes).",
            creditDynamic = "Por el descuento realizado a los trabajadores en la liquidación de nómina.",
            parentCode = "23"
        ),
        PucAccount(
            code = "237005",
            name = "Aportes a entidades promotoras de salud EPS",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Aportes a salud retenidos al empleado (4%) y a cargo de la empresa cuando aplique.",
            parentCode = "2370"
        ),
        PucAccount(
            code = "237006",
            name = "Aportes a administradoras de fondos de pensiones AFP",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Aportes a pensión del empleado (4%) y del empleador (12%).",
            parentCode = "2370"
        ),
        PucAccount(
            code = "237010",
            name = "Aportes al ICBF, SENA y Cajas de Compensación",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Parafiscales causados por pagar a través de PILA.",
            parentCode = "2370"
        ),
        PucAccount(
            code = "24",
            name = "IMPUESTOS, GRAVÁMENES Y TASAS",
            level = PucLevel.GRUPO,
            nature = PucNature.CREDITO,
            description = "Pasivos a favor del Estado por concepto de impuestos de orden nacional, departamental o municipal liquidados sobre bases gravables de la empresa.",
            parentCode = "2"
        ),
        PucAccount(
            code = "2408",
            name = "Impuesto sobre las ventas por pagar (IVA)",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Registra el IVA generado en ventas de bienes y servicios (crédito) y el IVA descontable pagado en compras y costos (débito).",
            debitDynamic = "Por el IVA pagado o causado en compras de mercancías, materias primas o gastos deducibles (IVA Descontable), y por el saldo a favor al cierre bimestral/cuatrimestral.",
            creditDynamic = "Por el IVA facturado o generado en la venta de bienes y prestación de servicios gravados a la tarifa general (19%) o especial (5%).",
            parentCode = "24"
        ),
        PucAccount(
            code = "240801",
            name = "IVA generado",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Impuesto sobre las ventas cobrado a clientes en operaciones gravadas (19%).",
            parentCode = "2408"
        ),
        PucAccount(
            code = "240802",
            name = "IVA descontable",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Impuesto sobre las ventas pagado a proveedores en compras de inventarios o costos operacionales.",
            parentCode = "2408"
        ),
        PucAccount(
            code = "25",
            name = "OBLIGACIONES LABORALES",
            level = PucLevel.GRUPO,
            nature = PucNature.CREDITO,
            description = "Pasivos por salarios y prestaciones sociales derivadas del contrato de trabajo según el Código Sustantivo del Trabajo.",
            parentCode = "2"
        ),
        PucAccount(
            code = "2505",
            name = "Salarios por pagar",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Registra los salarios devengados por los trabajadores de la empresa pendientes de pago.",
            debitDynamic = "Por la transferencia o entrega en efectivo del salario neto al trabajador.",
            creditDynamic = "Por el valor neto de la nómina liquidada.",
            parentCode = "25"
        ),
        PucAccount(
            code = "250505",
            name = "Salarios netos por pagar",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Sueldos quincenales o mensuales pendientes de desembolso.",
            parentCode = "2505"
        ),
        PucAccount(
            code = "2510",
            name = "Cesantías consolidadas",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Provisión de cesantías equivalentes a un mes de salario por año trabajado (8.33%).",
            parentCode = "25"
        ),
        PucAccount(
            code = "2515",
            name = "Intereses sobre cesantías",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Intereses del 12% anual sobre el saldo de cesantías pagaderos en enero al trabajador.",
            parentCode = "25"
        ),
        PucAccount(
            code = "2520",
            name = "Prima de servicios",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Provisión de prima legal semestral (8.33%) pagadera en junio y diciembre.",
            parentCode = "25"
        ),
        PucAccount(
            code = "2525",
            name = "Vacaciones consolidadas",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Provisión de 15 días hábiles de descanso remunerado (4.17%).",
            parentCode = "25"
        ),

        // ================= CLASE 3: PATRIMONIO =================
        PucAccount(
            code = "3",
            name = "PATRIMONIO",
            level = PucLevel.CLASE,
            nature = PucNature.CREDITO,
            description = "Agrupa las cuentas que representan el valor residual de comparar el activo total con el pasivo externo, producto de los recursos netos de los socios.",
            debitDynamic = "Por disminuciones de capital, decretos de dividendos o pérdidas del ejercicio.",
            creditDynamic = "Por aportes iniciales, aumentos de capital, utilidades netas y reservas."
        ),
        PucAccount(
            code = "31",
            name = "CAPITAL SOCIAL",
            level = PucLevel.GRUPO,
            nature = PucNature.CREDITO,
            description = "Comprende el valor total de los aportes de capital que los socios han pagado o se han comprometido a pagar.",
            parentCode = "3"
        ),
        PucAccount(
            code = "3105",
            name = "Capital suscrito y pagado",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Registra el ingreso de capital aportado por los accionistas en sociedades por acciones (S.A., S.A.S.).",
            parentCode = "31"
        ),
        PucAccount(
            code = "3115",
            name = "Aportes sociales",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Aportes de socios en sociedades de responsabilidad limitada o asimiladas.",
            parentCode = "31"
        ),
        PucAccount(
            code = "36",
            name = "RESULTADOS DEL EJERCICIO",
            level = PucLevel.GRUPO,
            nature = PucNature.CREDITO,
            description = "Utilidades o pérdidas netas generadas en el período contable tras el cierre de ingresos y gastos.",
            parentCode = "3"
        ),
        PucAccount(
            code = "3605",
            name = "Utilidad del ejercicio",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Beneficio neto obtenido en el año o período fiscal.",
            parentCode = "36"
        ),
        PucAccount(
            code = "3610",
            name = "Pérdida del ejercicio",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Saldo negativo resultante cuando los costos y gastos superan los ingresos del período.",
            parentCode = "36"
        ),

        // ================= CLASE 4: INGRESOS =================
        PucAccount(
            code = "4",
            name = "INGRESOS",
            level = PucLevel.CLASE,
            nature = PucNature.CREDITO,
            description = "Agrupa las cuentas que representan los beneficios operativos y financieros que percibe el ente económico en el desarrollo del giro normal de su actividad comercial en un ejercicio determinado.",
            debitDynamic = "Por cancelaciones de saldos al cierre del ejercicio contra la cuenta de Ganancias y Pérdidas (5905).",
            creditDynamic = "Por el valor de las ventas de bienes, prestación de servicios, honorarios devengados y rendimientos financieros generados."
        ),
        PucAccount(
            code = "41",
            name = "OPERACIONALES",
            level = PucLevel.GRUPO,
            nature = PucNature.CREDITO,
            description = "Comprende los valores devengados directamente en el objeto social principal de la empresa.",
            parentCode = "4"
        ),
        PucAccount(
            code = "4135",
            name = "Comercio al por mayor y al por menor",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Registra el valor de los ingresos obtenidos por el ente económico en las actividades de compra, venta y reparación de bienes.",
            debitDynamic = "Por cancelaciones al cierre anual contra Ganancias y Pérdidas.",
            creditDynamic = "Por el valor facturado de las ventas de mercancías a precio de venta.",
            parentCode = "41"
        ),
        PucAccount(
            code = "413501",
            name = "Venta de mercancías",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Ingresos brutos por comercialización de productos.",
            parentCode = "4135"
        ),
        PucAccount(
            code = "413554",
            name = "Venta de partes, piezas y accesorios",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Comercio de repuestos, accesorios de cómputo y suministros.",
            parentCode = "4135"
        ),
        PucAccount(
            code = "4155",
            name = "Actividades inmobiliarias, empresariales y de alquiler",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Ingresos operacionales por consultoría, servicios profesionales, asesoría y alquiler.",
            debitDynamic = "Por cierre de ejercicio contable.",
            creditDynamic = "Por el valor facturado por prestación de asesoría o consultoría técnica.",
            parentCode = "41"
        ),
        PucAccount(
            code = "415505",
            name = "Servicios de consultoría y asesoría",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.CREDITO,
            description = "Honorarios y contratos de asesoría empresarial.",
            parentCode = "4155"
        ),
        PucAccount(
            code = "4175",
            name = "Devoluciones en ventas (DB)",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Cuenta de naturaleza débito que registra las devoluciones, rebajas o descuentos comerciales concedidos sobre ventas facturadas.",
            debitDynamic = "Por el valor de las mercancías devueltas por clientes a precio de venta.",
            creditDynamic = "Por el cierre de fin de año contra Ganancias y Pérdidas.",
            parentCode = "41"
        ),
        PucAccount(
            code = "42",
            name = "NO OPERACIONALES",
            level = PucLevel.GRUPO,
            nature = PucNature.CREDITO,
            description = "Ingresos obtenidos por transacciones ajenas al objeto social principal (intereses ganados, dividendos, aprovechamientos).",
            parentCode = "4"
        ),
        PucAccount(
            code = "4210",
            name = "Financieros",
            level = PucLevel.CUENTA,
            nature = PucNature.CREDITO,
            description = "Registra los ingresos causados por rendimientos financieros generados en cuentas de ahorro, CDT o inversiones.",
            parentCode = "42"
        ),

        // ================= CLASE 5: GASTOS =================
        PucAccount(
            code = "5",
            name = "GASTOS",
            level = PucLevel.CLASE,
            nature = PucNature.DEBITO,
            description = "Agrupa las cuentas que representan los cargos operativos y financieros en que incurre el ente económico en el desarrollo del giro normal de su actividad en un ejercicio económico determinado.",
            debitDynamic = "Por el valor de los gastos causados o devengados en el período (nómina, arriendos, servicios, mantenimiento, seguros).",
            creditDynamic = "Por la cancelación de saldos al cierre del ejercicio contable contra Ganancias y Pérdidas (5905)."
        ),
        PucAccount(
            code = "51",
            name = "OPERACIONALES DE ADMINISTRACIÓN",
            level = PucLevel.GRUPO,
            nature = PucNature.DEBITO,
            description = "Gastos ocasionados en el desarrollo de la gestión administrativa de la empresa.",
            parentCode = "5"
        ),
        PucAccount(
            code = "5105",
            name = "Gastos de personal (Administración)",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra los gastos ocasionados por concepto de la relación laboral con el personal administrativo.",
            debitDynamic = "Por los sueldos devengados, auxilio de transporte, horas extras, aportes de seguridad social, parafiscales y prestaciones causadas.",
            creditDynamic = "Por cancelación al cierre del ejercicio contable.",
            parentCode = "51"
        ),
        PucAccount(
            code = "510506",
            name = "Sueldos (Administración)",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Sueldo básico del personal administrativo devengado en el período.",
            parentCode = "5105"
        ),
        PucAccount(
            code = "510527",
            name = "Auxilio de transporte",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Auxilio legal de transporte para trabajadores que devenguen hasta dos salarios mínimos legales vigentes.",
            parentCode = "5105"
        ),
        PucAccount(
            code = "510568",
            name = "Aportes a ARL",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Aportes del empleador a la Administradora de Riesgos Laborales según clase de riesgo.",
            parentCode = "5105"
        ),
        PucAccount(
            code = "510569",
            name = "Aportes a EPS (Salud empleador)",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Aporte patronal a salud cuando no aplique la exoneración del artículo 114-1 del E.T.",
            parentCode = "5105"
        ),
        PucAccount(
            code = "510570",
            name = "Aportes a fondos de pensiones (Empleador)",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Aporte patronal del 12% a pensión a favor de los fondos de pensiones.",
            parentCode = "5105"
        ),
        PucAccount(
            code = "510572",
            name = "Aportes a cajas de compensación familiar",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Aporte del 4% sobre la nómina devengada a la Caja de Compensación Familiar.",
            parentCode = "5105"
        ),
        PucAccount(
            code = "5110",
            name = "Honorarios (Administración)",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra los gastos por servicios calificados prestados por profesionales independientes (abogados, contadores, revisores fiscales).",
            debitDynamic = "Por el valor de las cuentas de cobro o facturas de profesionales.",
            creditDynamic = "Por cancelación a fin de año.",
            parentCode = "51"
        ),
        PucAccount(
            code = "511010",
            name = "Revisoría fiscal",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Honorarios causados por el dictamen de revisoría fiscal.",
            parentCode = "5110"
        ),
        PucAccount(
            code = "511030",
            name = "Asesoría jurídica",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Honorarios por representación o asesoría legal.",
            parentCode = "5110"
        ),
        PucAccount(
            code = "511035",
            name = "Asesoría contable y tributaria",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Honorarios de contador público o consultor tributario.",
            parentCode = "5110"
        ),
        PucAccount(
            code = "5120",
            name = "Arrendamientos (Administración)",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Gastos causados por el uso y goce de bienes muebles o inmuebles ajenos utilizados en la administración.",
            debitDynamic = "Por el valor del canon de arrendamiento causado periódicamente.",
            creditDynamic = "Por cierre de ejercicio.",
            parentCode = "51"
        ),
        PucAccount(
            code = "512010",
            name = "Construcciones y edificaciones (Arriendo)",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Canon de arrendamiento de oficinas, sedes o locales administrativos.",
            parentCode = "5120"
        ),
        PucAccount(
            code = "5135",
            name = "Servicios (Administración)",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Gastos por servicios públicos (agua, luz, teléfono), vigilancia, aseo, mantenimiento y transporte.",
            debitDynamic = "Por el valor de las facturas de servicios devengados.",
            creditDynamic = "Por cancelación al cierre del período.",
            parentCode = "51"
        ),
        PucAccount(
            code = "513525",
            name = "Acueducto y alcantarillado",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Factura de agua potable y saneamiento básico.",
            parentCode = "5135"
        ),
        PucAccount(
            code = "513530",
            name = "Energía eléctrica",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Factura de consumo de energía eléctrica de la sede.",
            parentCode = "5135"
        ),
        PucAccount(
            code = "513535",
            name = "Teléfono e internet",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Factura de telecomunicaciones, enlaces y conectividad.",
            parentCode = "5135"
        ),
        PucAccount(
            code = "5160",
            name = "Depreciaciones",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra el gasto periódico resultante del desgaste u obsolescencia de los activos fijos de administración.",
            debitDynamic = "Por la cuota mensual calculada de depreciación contra la 1592.",
            creditDynamic = "Por cancelación al cierre del período fiscal.",
            parentCode = "51"
        ),
        PucAccount(
            code = "52",
            name = "OPERACIONALES DE VENTAS",
            level = PucLevel.GRUPO,
            nature = PucNature.DEBITO,
            description = "Gastos originados en la gestión comercial, mercadeo, almacenamiento y distribución de productos.",
            parentCode = "5"
        ),
        PucAccount(
            code = "5205",
            name = "Gastos de personal (Ventas)",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Sueldos, comisiones y prestaciones de asesores comerciales y vendedores.",
            parentCode = "52"
        ),
        PucAccount(
            code = "53",
            name = "NO OPERACIONALES (Gastos)",
            level = PucLevel.GRUPO,
            nature = PucNature.DEBITO,
            description = "Gastos financieros, intereses bancarios, gravamen a los movimientos financieros (4x1000) y multas.",
            parentCode = "5"
        ),
        PucAccount(
            code = "5305",
            name = "Financieros (Gastos)",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Intereses pagados por préstamos bancarios, comisiones financieras y GMF 4x1000.",
            parentCode = "53"
        ),
        PucAccount(
            code = "530505",
            name = "Gastos bancarios",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Comisiones por transferencias, cuotas de manejo y chequeras.",
            parentCode = "5305"
        ),
        PucAccount(
            code = "530520",
            name = "Intereses corrientes y de mora",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Intereses causados por obligaciones con entidades financieras.",
            parentCode = "5305"
        ),

        // ================= CLASE 6: COSTOS DE VENTAS =================
        PucAccount(
            code = "6",
            name = "COSTOS DE VENTAS",
            level = PucLevel.CLASE,
            nature = PucNature.DEBITO,
            description = "Agrupa las cuentas que representan la acumulación de los costos directos e indirectos del servicio o de la compra de mercancías para la venta.",
            debitDynamic = "Por el costo de las mercancías vendidas o los insumos utilizados en la prestación del servicio.",
            creditDynamic = "Por la cancelación del saldo al cierre del ejercicio contable contra Ganancias y Pérdidas."
        ),
        PucAccount(
            code = "61",
            name = "COSTO DE VENTAS Y DE PRESTACIÓN DE SERVICIOS",
            level = PucLevel.GRUPO,
            nature = PucNature.DEBITO,
            description = "Costo directo imputable a los bienes enajenados o servicios suministrados en el período.",
            parentCode = "6"
        ),
        PucAccount(
            code = "6135",
            name = "Comercio al por mayor y al por menor (Costo)",
            level = PucLevel.CUENTA,
            nature = PucNature.DEBITO,
            description = "Registra el costo de adquisición de las mercancías vendidas en el sistema de inventario permanente (contra la 1435).",
            debitDynamic = "Por el costo de las mercancías entregadas al cliente en la venta efectuada.",
            creditDynamic = "Por el costo de las mercancías devueltas por clientes y por cierre anual.",
            parentCode = "61"
        ),
        PucAccount(
            code = "613501",
            name = "Costo de mercancías vendidas",
            level = PucLevel.SUBCUENTA,
            nature = PucNature.DEBITO,
            description = "Costo salida de inventario al momento de facturar la venta comercial.",
            parentCode = "6135"
        ),

        // ================= CLASE 7: COSTOS DE PRODUCCIÓN =================
        PucAccount(
            code = "7",
            name = "COSTOS DE PRODUCCIÓN O DE OPERACIÓN",
            level = PucLevel.CLASE,
            nature = PucNature.DEBITO,
            description = "Agrupa el conjunto de las cuentas que representan las erogaciones y cargos asociados clara y directamente con la elaboración de bienes o prestación de servicios.",
            debitDynamic = "Por el consumo de materia prima, mano de obra directa y costos indirectos de fabricación.",
            creditDynamic = "Por el traslado a los inventarios de productos en proceso o terminados."
        ),
        PucAccount(
            code = "71",
            name = "MATERIA PRIMA",
            level = PucLevel.GRUPO,
            nature = PucNature.DEBITO,
            description = "Registra el valor de los materiales e insumos directos utilizados en el proceso productivo.",
            parentCode = "7"
        ),
        PucAccount(
            code = "72",
            name = "MANO DE OBRA DIRECTA",
            level = PucLevel.GRUPO,
            nature = PucNature.DEBITO,
            description = "Costos laborales de los operarios vinculados directamente a la transformación del producto.",
            parentCode = "7"
        ),
        PucAccount(
            code = "73",
            name = "COSTOS INDIRECTOS",
            level = PucLevel.GRUPO,
            nature = PucNature.DEBITO,
            description = "Materiales indirectos, mano de obra indirecta, servicios de planta y depreciación de maquinaria fabril.",
            parentCode = "7"
        ),

        // ================= CLASE 8 & 9: CUENTAS DE ORDEN =================
        PucAccount(
            code = "8",
            name = "CUENTAS DE ORDEN DEUDORAS",
            level = PucLevel.CLASE,
            nature = PucNature.DEBITO,
            description = "Agrupa las cuentas que reflejan hechos o circunstancias de los cuales se pueden generar derechos para el ente económico o sirven para efectos de control.",
            debitDynamic = "Por el valor de los derechos contingentes, bienes recibidos en custodia o litigios a favor.",
            creditDynamic = "Por la extinción del derecho contingente o devolución del bien custodiado."
        ),
        PucAccount(
            code = "9",
            name = "CUENTAS DE ORDEN ACREEDORAS",
            level = PucLevel.CLASE,
            nature = PucNature.CREDITO,
            description = "Agrupa las cuentas que reflejan compromisos o contratos que representan obligaciones posibles o que sirven para control contable.",
            debitDynamic = "Por la extinción de la contingencia o liquidación del compromiso.",
            creditDynamic = "Por el valor de las responsabilidades contingentes, fianzas otorgadas o contratos firmados."
        )
    )

    fun search(query: String, classFilter: String? = null): List<PucAccount> {
        val q = query.trim().lowercase()
        return accounts.filter { acc ->
            val matchesClass = if (classFilter == null || classFilter == "ALL") {
                true
            } else {
                acc.code.startsWith(classFilter)
            }

            val matchesQuery = if (q.isEmpty()) {
                true
            } else {
                acc.code.lowercase().contains(q) || acc.name.lowercase().contains(q) || acc.description.lowercase().contains(q)
            }

            matchesClass && matchesQuery
        }
    }

    fun findByCode(code: String): PucAccount? {
        val clean = code.trim().replace(".", "")
        return accounts.find { it.code == clean }
    }
}
