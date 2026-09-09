package com.example.model

/**
 * Provides 1-2 line plain-language micro-guides for Colombian PUC accounts,
 * explaining in simple words what the account represents and how it moves.
 */
object PucExplanationHelper {

    fun getMicroGuide(code: String, name: String, nature: PucNature): String {
        // Specific well-known subaccounts and accounts
        return when (code) {
            // Clases
            "1" -> "Todos los bienes, dinero y derechos que posee la empresa. Aumenta con compras y cobros, disminuye con ventas y pagos."
            "2" -> "Todas las deudas y compromisos con bancos, proveedores y el Estado. Aumenta al endeudarse, disminuye al pagar."
            "3" -> "El patrimonio neto de los dueños: aportes iniciales, reservas y utilidades retenidas."
            "4" -> "Dinero y ventas que entran por el giro ordinario del negocio. Incrementa el patrimonio."
            "5" -> "Gastos operativos indispensables para funcionar (sueldos, servicios, arriendos). No se recuperan directamente."
            "6" -> "Lo que costó adquirir o producir las mercancías que ya fueron vendidas al cliente."
            "7" -> "Costos directos de materias primas y mano de obra para fabricar productos."
            "8" -> "Cuentas de control para contingencias, litigios o compromisos que no afectan de inmediato el balance."
            "9" -> "Control de garantías otorgadas, contratos y responsabilidades contingentes frente a terceros."

            // Grupos Activo
            "11" -> "Dinero disponible de inmediato en efectivo, bancos o monedas extranjeras."
            "12" -> "Inversiones temporales o permanentes en acciones, CDT o bonos para obtener rentabilidad."
            "13" -> "Cuentas por cobrar a clientes, empleados, socios y anticipos de impuestos."
            "14" -> "Mercancías, materias primas y productos listos para comercializar."
            "15" -> "Bienes duraderos usados en la operación: inmuebles, vehículos, computadores y maquinaria."
            "16" -> "Bienes no físicos con valor económico: marcas, patentes, software y derechos de autor."
            "17" -> "Gastos pagados por anticipado pendientes de amortizar mes a mes (ej. pólizas de seguro)."

            // Grupos Pasivo
            "21" -> "Préstamos bancarios y pagarés firmados con entidades financieras."
            "22" -> "Facturas pendientes de pago a quienes nos suministran insumos o mercancías."
            "23" -> "Gastos ya causados y retenciones en la fuente pendientes de transferir a la DIAN o municipios."
            "24" -> "Impuestos a favor del Estado: IVA por pagar, renta e impuesto al consumo."
            "25" -> "Deudas laborales con los trabajadores: salarios, primas, cesantías y vacaciones."
            "26" -> "Provisiones estimadas para cubrir futuros litigios, demandas o garantías."

            // Grupos Patrimonio
            "31" -> "Capital aportado por los socios o accionistas al constituir la empresa."
            "32" -> "Recursos adicionales aportados como prima en colocación de acciones o donaciones."
            "33" -> "Utilidades retenidas por mandato legal o estatutario para blindar a la empresa."
            "36" -> "Ganancia o pérdida neta acumulada al cierre del año contable."

            // Grupos Ingresos, Gastos y Costos
            "41" -> "Ventas comerciales y prestación de servicios del objeto social principal."
            "42" -> "Ingresos secundarios no operativos (intereses ganados, arrendamientos ocasionales)."
            "51" -> "Gastos fijos de la oficina y gerencia: nómina administrativa, asesorías y papelería."
            "52" -> "Gastos directos para vender: publicidad, comisiones de vendedores y fletes."
            "53" -> "Gastos no operativos como comisiones bancarias o intereses de créditos."
            "61" -> "Costo de compra o fabricación de los productos entregados a los clientes."
            "71" -> "Materia prima directa consumida en la planta de producción."

            // Cuentas populares (4 y 6 dígitos)
            "1105", "110505" -> "Efectivo disponible. Sube cuando entra dinero físico o cheques, baja cuando pagas o consignas en el banco."
            "110510" -> "Fondo fijo en efectivo para pagos menores y gastos diarios de la oficina."
            "1110", "111005" -> "Cuentas corrientes bancarias. Sube con transferencias y consignaciones, baja con giros y pagos a terceros."
            "1120", "112005" -> "Cuentas de ahorro bancarias. Generan intereses y permiten retiros inmediatos."
            "1305", "130505" -> "Facturas comerciales por cobrar a clientes. Sube al vender fiado, baja cuando el cliente abona o paga."
            "1355", "135515" -> "Anticipo de renta retenido por clientes. Se resta como saldo a favor en la declaración anual ante la DIAN."
            "135517" -> "Retención de IVA practicada por Grandes Contribuyentes a favor de la empresa."
            "135518" -> "Retención de ICA practicada por clientes que disminuye el impuesto de industria y comercio municipal."
            "1435", "143501" -> "Mercancías compradas listas para la venta. Sube al abastecer inventario, baja cuando se despacha al comprador."
            "1524", "152405" -> "Muebles, escritorios, sillas y estanterías de las oficinas para el uso laboral."
            "1528", "152805" -> "Computadores, laptops y servidores de la empresa. Se deprecian periódicamente."
            "1540", "154005" -> "Vehículos, camionetas y camiones para transporte de personas o distribución de mercancías."
            "1592", "159205" -> "Desgaste acumulado de los activos fijos. Resta el valor comercial del activo en el balance."
            "2105", "210510" -> "Crédito bancario comercial. Sube cuando el banco desembolsa, baja con cada cuota de capital pagada."
            "2205", "220505" -> "Deudas con proveedores de mercancías. Sube al comprar fiado, baja al transferir el pago."
            "2335", "233525" -> "Honorarios causados pendientes de giro al profesional o asesor independiente."
            "233540" -> "Arrendamientos causados del mes pendientes de pago al propietario."
            "233550" -> "Facturas de servicios públicos (luz, agua, internet) causadas pendientes de pagar."
            "2365", "236540" -> "Retención en renta del 2.5% practicada en compras. Es un pasivo que se entrega mensualmente a la DIAN."
            "236515" -> "Retención en la fuente sobre honorarios profesionales (10% o 11%) que se traslada a la DIAN."
            "236525" -> "Retención en la fuente sobre contratos de servicios técnicos y generales."
            "236530" -> "Retención en la fuente del 3.5% sobre cánones de arrendamiento de inmuebles."
            "2368", "236801" -> "ReteICA retenido a proveedores que debe transferirse al municipio o distrito."
            "2408", "240801" -> "IVA generado (19%) facturado a clientes. Es una deuda tributaria que se debe declarar a la DIAN."
            "240802" -> "IVA descontable pagado en compras a proveedores. Disminuye el saldo de IVA a pagar a la DIAN."
            "2505", "250505" -> "Neto a pagar a los empleados por concepto de nómina al finalizar el mes o quincena."
            "2510", "251005" -> "Cesantías causadas a favor del trabajador (un mes de salario por año laborado)."
            "2515", "251505" -> "Intereses sobre cesantías (12% anual) pagaderos directamente al empleado en enero."
            "2520", "252005" -> "Prima de servicios legal semestral acumulada para desembolso en junio y diciembre."
            "2525", "252505" -> "Días de descanso remunerado acumulados a favor de los empleados."
            "3105", "310505" -> "Capital con el que los dueños fundaron la sociedad. Respalda financieramente la empresa."
            "4135", "413501" -> "Ingresos operacionales por ventas comerciales. Sube al emitir facturas de venta."
            "5105", "510506" -> "Sueldo básico de los empleados de gerencia y administración. Es un gasto del período."
            "5110", "511035" -> "Honorarios por asesoría contable, auditoría, tributaria o jurídica de la empresa."
            "5120", "512010" -> "Canon de arrendamiento de las oficinas, locales o bodegas de la empresa."
            "5135", "513525" -> "Gasto en servicio de energía eléctrica de las instalaciones."
            "513535" -> "Gasto de servicio de internet, telefonía móvil y fija."
            "5305", "530520" -> "Intereses bancarios cobrados por créditos financieros."
            "6135", "613501" -> "Costo de las mercancías que fueron vendidas según el kárdex de inventarios."

            else -> {
                // Heuristic based on code prefix and nature
                when {
                    code.startsWith("11") -> "Recurso financiero líquido disponible en caja o cuentas bancarias."
                    code.startsWith("13") -> "Derecho de cobro a favor de la empresa frente a un tercero o anticipo fiscal."
                    code.startsWith("14") -> "Inventario de bienes para fabricar o vender en el curso ordinario del negocio."
                    code.startsWith("15") -> "Bien tangible duradero de propiedad de la empresa para su funcionamiento."
                    code.startsWith("21") -> "Obligación financiera con bancos y corporaciones de crédito."
                    code.startsWith("22") -> "Cuenta comercial por pagar a proveedores de bienes o insumos."
                    code.startsWith("23") -> "Pasivo por costos causados o retenciones tributarias pendientes de pago."
                    code.startsWith("24") -> "Impuesto fiscal o tributo municipal adeudado a la administración tributaria."
                    code.startsWith("25") -> "Obligación laboral pendiente con los empleados de la empresa."
                    code.startsWith("3") -> "Rubro patrimonial que representa el valor neto perteneciente a los socios."
                    code.startsWith("4") -> "Ingreso generado que incrementa las ganancias operacionales del negocio."
                    code.startsWith("5") -> "Gasto operacional administrativo o de ventas para la marcha de la empresa."
                    code.startsWith("6") -> "Costo de ventas asociado directamente a los bienes o servicios facturados."
                    code.startsWith("7") -> "Costo incurrido en el proceso de producción o transformación manufacturera."
                    code.startsWith("8") || code.startsWith("9") -> "Cuenta de control para contingencias o compromisos contractuales."
                    nature == PucNature.DEBITO -> "Cuenta de naturaleza débito: sube por el Debe al recibir recursos o causar gastos, baja por el Haber."
                    else -> "Cuenta de naturaleza crédito: sube por el Haber al contraer obligaciones o generar ingresos, baja por el Debe."
                }
            }
        }
    }
}
