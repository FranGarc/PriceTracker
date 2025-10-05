# ADR-008: Elección del stack de Pruebas

---

- **Fecha:** 2025-10-03
- **Estado:** Aceptado

## Contexto

---

Para implementar de manera efectiva la estrategia de pruebas BDD+TDD definida en el ADR-003, es necesario seleccionar un conjunto de librerías y frameworks que trabajen de forma cohesionada. Este "stack de pruebas" debe proporcionarnos las herramientas para ejecutar pruebas, describir el comportamiento del sistema, verificar los resultados, aislar componentes y generar datos de prueba de forma eficiente.

## Opciones y Decisión por Componente

---



### 1. Motor de Ejecución
- **Opciones Consideradas:** JUnit 5, TestNG, Spock.
- **Decisión:** Se elige **JUnit 5**.
- **Reseña:** JUnit 5 es el estándar de facto en el ecosistema Java. Su arquitectura modular (Jupiter, Platform, Vintage) y su robusto modelo de extensiones lo convierten en la base perfecta sobre la que construir nuestro stack, garantizando la máxima compatibilidad con otras herramientas y el propio build tool (Gradle).

### 2. Framework BDD
- **Opciones Consideradas:** Cucumber, JBehave, Karate.
- **Decisión:** Se elige **Cucumber**.
- **Reseña:** Cucumber es la implementación más popular y con mayor soporte para ejecutar especificaciones Gherkin (.feature) en la JVM. Su integración con JUnit 5 es directa y nos permite escribir pruebas de aceptación que sirven como documentación viva y ejecutable.

### 3. Librería de Aserciones
- **Opciones Consideradas:** AssertJ, Hamcrest, Aserciones Nativas de JUnit.
- **Decisión:** Se elige **AssertJ**.
- **Reseña:** Aunque JUnit provee aserciones básicas, la API fluida de AssertJ (assertThat(resultado).isEqualTo(esperado)) mejora drásticamente la legibilidad del código de prueba y proporciona mensajes de error mucho más descriptivos cuando una aserción falla.

### 4. Librería de Mocks
- **Opciones Consideradas:** Mockito, EasyMock, JMockit.
- **Decisión:** Se elige **Mockito**.
- **Reseña:** Mockito es el estándar de la industria para crear dobles de prueba (mocks, stubs, spies) en Java. Su API es limpia, potente y muy intuitiva, lo que nos permite aislar fácilmente los componentes que estamos probando de sus dependencias.

### 5. Librería de Generación de Datos
- **Opciones Consideradas:** Instancio, Datafaker, Patrón Test Data Builder.
- **Decisión:** Se elige **Instancio**.
- **Reseña:** Para evitar la creación de datos de forma manual y repetitiva, es crucial usar una librería de generación. Se elige Instancio por su enfoque en la creación de grafos de objetos complejos y completamente poblados, lo cual es ideal para generar las entidades de nuestro dominio (DDD) de forma rápida y fiable.

### 6. Librería de Verificación de la Arquitectura
- **Opciones Consideradas:** ArchUnit, JDepend.
- **Decisión:** Se elige **ArchUnit**.
- **Reseña:** ArchUnit permite comprobar dependencias entre paquetes y clases, capas, buscar comprobar referencias cíclicas y más.

## Justificación General de la Decisión

La combinación de **JUnit 5** + **Cucumber** + **AssertJ** + **Mockito** + **Instancio** representa un stack de pruebas moderno, robusto y altamente productivo. Este conjunto de herramientas nos permite implementar nuestra estrategia BDD+TDD de manera efectiva:

- **Cucumber** y **AssertJ** nos ayudan a definir y verificar el "qué" (el comportamiento externo).
- **JUnit 5**, **Mockito** e **Instancio** nos dan el poder para construir y probar de forma aislada el "cómo" (la implementación interna de cada componente).

Esta selección nos proporciona las mejores herramientas de su clase para cada una de las tareas del ciclo de pruebas, asegurando una alta calidad de software y un código bien documentado y mantenible.

Además, ArchUnit permite escribir tests que verifiquen que nuestro código cumple las reglas que hemos definido. Con ello se obtiene una estructura de proyecto robusta y, lo más importante, un sistema automático que vigila que nadie se salte las reglas. 

## Consecuencias


- El fichero ```build.gradle.kts``` se configurará para incluir las dependencias de estas cinco librerías y asegurar su correcta integración (ej: el plugin de Cucumber).
- El flujo de trabajo descrito en ```CONTRIBUTING.md``` queda respaldado por un conjunto de herramientas concreto.
- El equipo deberá estar familiarizado con las APIs de estas librerías para maximizar su productividad.
- La base de código contendrá una suite de pruebas completa, incluyendo tests de aceptación (```.feature```), unitarios y de integración.
