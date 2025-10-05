# ADR-006: Elección de la Solución de Persistencia de Datos

---

- **Fecha:** 2025-10-03
- **Estado:** Aceptado

## Contexto

---

La app requiere una solución de persistencia para almacenar los datos de compras, principalmente la relación entre un producto y su histórico de precios. Esta elección es fundamental, ya que impacta directamente en el rendimiento de lectura/escritura, la consistencia de los datos, la complejidad
operativa y la capacidad de evolucionar el modelo de datos en el futuro. La solución debe integrarse bien con el ecosistema Kotlin/Android.
<!--- y cumplir con nuestros NFRs --->

## Opciones Consideradas

---

1. **Room:** Librería de abstracción de SQLite recomendada por Google. Ideal para apps offline-first como PriceTracker, en las que la integridad y consistencia de los datos es esencial, incluso sin conexión a internet.

2. **SQLite:** Motor ligero de base de datos construido directamente sobre la plataforma Android. Permite control completo de las operaciones de la base de datos y consultas SQL custom.

3. **DataStore:** Reemplazo moderno para ```SharedPreferences```. Diseñado para pequeños juegos de datos sencillos (pares clave-valor u objetos estructurados) de forma asíncrona y segura.

4. **SharedPreferences:** API básica para almacenar datos primitivos (booleanos, Strings, enteros, etc) en pares clave-valor, típicamente persistidos en un fichero XML.

## Decisión

---

Se ha decidido utilizar **Room** como la solución de persistencia de datos principal para la app.

**Justificación:** La elección de Room Database, como parte fundamental de Jetpack, proporciona una capa de persistencia local que capitaliza la seguridad, la asincronía, y la reducción de código repetitivo necesarias para una aplicación moderna de Android que gestiona datos estructurados.

- **Verificación exhaustiva de las consultas SQL** en tiempo de compilación. Esto significa que cualquier error en un ```@Query``` dentro de los DAOs (Data Access Objects) es detectado antes de que la aplicación se ejecute, garantizando la integridad del código. Además, Room obliga a trabajar con
  objetos (Entities) tipados en Kotlin, lo que elimina el mapeo manual de Cursors y reduce drásticamente los errores de tipo.
- **Soporta herramientas modernas de concurrencia de Kotlin** como  ```Coroutines``` y ```Flow```. Esto permite que las operaciones pesadas de inserción y lectura de datos (como obtener el historial de precios para un gráfico) se ejecuten de manera eficiente fuera del hilo principal. El soporte
  nativo para ```Flow``` facilita la creación de patrones reactivos para actualizar automáticamente la UI.
- **Centralización y facilidad para la administración del modelo de datos** al definir el esquema a través de clases **Entity**. Si necesitas cambiar la estructura (por ejemplo, añadir un campo), Room ofrece una API de **Migración** robusta que asegura que los datos históricos del usuario se
  conserven de forma segura a través de las actualizaciones de la aplicación. 