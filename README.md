# PUC Colombia

Aplicación Android (Jetpack Compose) para consultar el **Plan Único de Cuentas para comerciantes en Colombia (Decreto 2650 de 1993)** con:

- Catálogo jerárquico navegable por clases, grupos, cuentas y subcuentas.
- Búsqueda por código y texto.
- Vista de detalle con naturaleza, dinámica débito/crédito y ruta contable.
- Guía de retenciones e impuestos frecuentes (Retefuente, IVA, ReteICA).
- Persistencia local con Room y funcionamiento offline.

## Estado y alcance actual

Este repositorio ya incluye una implementación funcional del catálogo PUC y guía tributaria.
No incluye todavía documentación funcional externa oficial (historias de usuario, criterios de aceptación o actas de alcance), por lo que el alcance aquí documentado corresponde al comportamiento implementado en código.

## Requisitos

- Android Studio (versión reciente)
- JDK 11
- SDK de Android con `compileSdk` 36

## Configuración local

1. Clona el repositorio y ábrelo en Android Studio.
2. Crea un archivo `.env` en la raíz del proyecto usando `.env.example` como referencia.
3. Sincroniza Gradle y ejecuta la app en emulador o dispositivo.

> Nota: la app funciona en modo local con los datos incluidos en `app/src/main/assets/puc.json`.

## Variables de entorno

Se gestionan con `secrets-gradle-plugin`:

- `GEMINI_API_KEY` (opcional; actualmente no requerido por los flujos principales de la app).
- `KEYSTORE_PATH`, `STORE_PASSWORD`, `KEY_PASSWORD` (solo para firmado release si aplica).

## Arquitectura (resumen)

- **UI:** Jetpack Compose (`app/src/main/java/com/example/ui`)
- **Estado:** `PucViewModel`
- **Datos:** `PucRepository`
- **Persistencia:** Room (`PucDatabase`, `PucDao`, entidades + FTS)
- **Contenido base:** `assets/puc.json` y respaldo en `PucCatalog`

## Estructura principal

- `app/src/main/java/com/example/ui/screens/CatalogScreen.kt`: navegación y búsqueda del catálogo.
- `app/src/main/java/com/example/ui/screens/TaxGuideScreen.kt`: guía de retenciones/impuestos.
- `app/src/main/java/com/example/data/local/`: capa de base de datos local.
- `app/src/main/assets/puc.json`: dataset principal del PUC.

## Comandos útiles

Desde la raíz del repositorio:

- Ejecutar pruebas unitarias: `./gradlew test`
- Ejecutar pruebas instrumentadas (requiere entorno Android): `./gradlew connectedAndroidTest`
- Ejecutar lint: `./gradlew lint`

## Limitaciones actuales

- Las tarifas tributarias son de referencia y deben validarse contra normativa vigente antes de uso productivo.
- No hay sincronización remota ni autenticación de usuarios.
- No reemplaza asesoría contable o tributaria profesional.
