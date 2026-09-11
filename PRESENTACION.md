# Presentación del proyecto: PUC Colombia

## Diapositiva 1 — Portada
- **PUC Colombia**
- Aplicación Android para consultar el Plan Único de Cuentas para comerciantes en Colombia.
- Presentado por: **[Tu nombre]**
- Curso: **[Tu curso]**
- Fecha: **[Tu fecha]**

## Diapositiva 2 — Problema
- Consultar el PUC y reglas tributarias suele ser lento y disperso.
- La información normalmente está fragmentada entre documentos y fuentes distintas.
- Se necesita una herramienta rápida, clara y usable sin conexión.

## Diapositiva 3 — Solución
- App móvil desarrollada con **Jetpack Compose**.
- Catálogo jerárquico del PUC con búsqueda por código y texto.
- Vista de detalle contable y guía de retenciones/impuestos.

## Diapositiva 4 — Funciones clave
- Navegación por clases, grupos, cuentas y subcuentas.
- Búsqueda inteligente (ejemplo: `1105` o concepto).
- Favoritos y cuentas recientes.
- Guía de Retefuente, IVA y ReteICA.

## Diapositiva 5 — Arquitectura (alto nivel)
- **UI:** Jetpack Compose.
- **Estado:** `PucViewModel`.
- **Datos/Persistencia:** `PucRepository` + Room.
- **Contenido base:** `assets/puc.json`.
- Funcionamiento offline con datos locales.

## Diapositiva 6 — Demo (flujo 2–3 min)
1. Abrir la app y entrar al catálogo.
2. Buscar una cuenta y abrir su detalle.
3. Guardar una cuenta en favoritos.
4. Abrir la pestaña de retenciones.

## Diapositiva 7 — Valor del proyecto
- Acceso rápido a información contable clave.
- Apoyo académico y operativo para consulta diaria.
- Mejora de productividad para estudiantes y comerciantes.

## Diapositiva 8 — Limitaciones actuales
- Tarifas tributarias de referencia (validar normativa vigente).
- Sin autenticación de usuarios.
- Sin sincronización remota.
- No reemplaza asesoría contable o tributaria profesional.

## Diapositiva 9 — Mejoras futuras
- Actualización automática de cambios normativos.
- Exportación y compartición de consultas.
- Perfil de usuario e historial en nube.

## Diapositiva 10 — Cierre
- PUC Colombia centraliza consulta contable y tributaria en móvil.
- Reduce fricción en el acceso al catálogo y reglas frecuentes.
- **Pregunta final:** ¿Qué módulo priorizarían en la siguiente versión?

---

## Guion hablado por diapositiva (versión 5 minutos)

### 1. Portada
“Hoy les presento PUC Colombia, una app Android pensada para consultar el Plan Único de Cuentas de forma rápida y ordenada.”

### 2. Problema
“El principal problema es que la consulta contable y tributaria suele estar dispersa; eso hace perder tiempo y aumenta errores de interpretación.”

### 3. Solución
“La solución propuesta es una app en Jetpack Compose que integra catálogo PUC, búsqueda y guía tributaria en un solo lugar.”

### 4. Funciones clave
“Incluye navegación jerárquica, búsqueda por código o concepto, favoritos y una guía práctica de retenciones e impuestos frecuentes.”

### 5. Arquitectura
“A nivel técnico, usamos Compose para UI, ViewModel para estado, Room para persistencia local y un dataset base en `puc.json`.”

### 6. Demo
“En la demo mostraré cómo abrir catálogo, buscar una cuenta, revisar su detalle, guardarla en favoritos y consultar retenciones.”

### 7. Valor
“El valor principal es la rapidez de acceso a información relevante para estudio y operación contable diaria.”

### 8. Limitaciones
“Como limitaciones, las tarifas son de referencia y deben validarse; además no hay autenticación ni sincronización remota.”

### 9. Futuro
“Las siguientes mejoras incluyen actualización normativa automática, exportación y perfil de usuario con historial.”

### 10. Cierre
“En resumen, PUC Colombia simplifica la consulta contable. Me gustaría cerrar preguntando qué módulo priorizarían para la próxima versión.”

---

## Guion hablado por diapositiva (versión 10 minutos)

### 1. Portada
“Buenos días. Soy [tu nombre] y hoy presentaré PUC Colombia, una aplicación Android orientada a la consulta del Plan Único de Cuentas para comerciantes en Colombia.”

### 2. Problema
“En el trabajo académico y operativo, consultar cuentas y retenciones implica revisar múltiples fuentes. Esta fragmentación ralentiza procesos y dificulta estandarizar criterios.”

### 3. Solución
“PUC Colombia integra en una sola app un catálogo navegable, herramientas de búsqueda y una guía tributaria práctica. La idea central es reducir tiempo de consulta.”

### 4. Funciones clave
“La app permite recorrer la estructura del PUC desde clase hasta subcuenta, buscar por código o texto, guardar favoritos y consultar retenciones comunes como Retefuente, IVA y ReteICA.”

### 5. Arquitectura
“El diseño técnico separa claramente UI, estado y datos. Compose resuelve la interfaz, `PucViewModel` maneja estado y lógica de interacción, y Room permite operar localmente sin depender de internet.”

### 6. Demo
“La demostración seguirá un flujo corto: abrir catálogo, hacer una búsqueda real, entrar al detalle de una cuenta, marcar favorito y finalizar en la sección de retenciones.”

### 7. Valor
“Esto aporta velocidad, centralización y facilidad de uso, especialmente para estudiantes y pequeños comercios que requieren referencia rápida en su trabajo diario.”

### 8. Limitaciones
“Es importante señalar que la app no reemplaza asesoría profesional y que las tarifas deben validarse frente a normativa vigente antes de aplicarse en escenarios productivos.”

### 9. Mejoras futuras
“A futuro, el proyecto puede crecer con sincronización en nube, historial por usuario y mecanismos automáticos de actualización normativa para mantener vigencia.”

### 10. Cierre
“Como conclusión, PUC Colombia demuestra una solución práctica, usable y extensible para consulta contable móvil. Quedo atento a preguntas y sugerencias de priorización funcional.”
