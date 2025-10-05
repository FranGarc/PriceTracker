# ADR-003: Estrategia de Pruebas de Software

---

- **Fecha:** 2025-10-03
- **Estado:** Aceptado

## Contexto

Necesitamos definir un enfoque consistente y de alta calidad para las pruebas de software en el proyecto. La estrategia elegida influirá directamente en la calidad del código, la facilidad de mantenimiento y la velocidad de desarrollo a largo plazo. Este documento explora las alternativas para tomar
una decisión informada.

---

## Opciones Consideradas

---

### 1. Desarrollo Tradicional (Test-Last)

- **Descripción:** Se escribe el código de producción primero y las pruebas se añaden después para validar que funciona.
- **Ventajas:** Flujo de trabajo más intuitivo para muchos desarrolladores, percepción de mayor velocidad inicial.
- **Inconvenientes:** El código no siempre se diseña para ser "testeable", las pruebas pueden ser un añadido de última hora y cubrir solo los "happy paths", riesgo de que no se escriban si hay presión de tiempo.

### 2. Test-Driven Development (TDD)

- **Descripción:** Se escribe una prueba que falla primero, luego el código de producción mínimo para que pase, y finalmente se refactoriza.
- **Ventajas:** Produce un diseño más limpio y desacoplado, garantiza una alta cobertura de código, las pruebas sirven como documentación viva del comportamiento.
- **Inconvenientes:** Curva de aprendizaje más alta, puede sentirse más lento al principio.

### 3. Behavior-Driven Development (BDD)

- **Descripción:** Una evolución de TDD donde las pruebas se escriben en un lenguaje natural (Gherkin: Given/When/Then) que describe el comportamiento del sistema.
- **Ventajas:** Las pruebas son comprensibles por perfiles no técnicos, fomenta la colaboración con Product/QA.
- **Inconvenientes:** Puede ser más verboso, riesgo de acoplar las pruebas a detalles de la UI si no se hace bien.

## Decisión

---
Se propone adoptar una combinación de **Behavior-Driven Development (BDD)** y **Test-Driven Development (TDD)** como la estrategia principal.

**Justificación** Utilizaremos BDD para definir los casos de uso y describir el comportamiento externo del sistema, mientras que utilizaremos TDD para garantizar las pruebas unitarias y de integración y la cobertura del código. Esta estrategia nos permite utilizar las pruebas escritas en Gherkin
como manual de uso del sistema y descripción mapeable incluso a nuestros diagramas. Dado que el proyecto usa Arquitectura Hexagonal y DDD, TDD es el complemento natural para construir un núcleo de dominio bien diseñado, aislado y completamente probado desde el inicio. Se considera que el beneficio en mantenibilidad y
calidad del diseño a largo plazo supera el coste de la curva de aprendizaje inicial.

### Consecuencias

--- 

- **Doble Enfoque:** El equipo deberá manejar dos ciclos de desarrollo:
    - **Ciclo Externo (BDD):** Escribir un escenario de comportamiento en Gherkin (Given/When/Then), verlo fallar, e implementar el código necesario para que pase.
    - **Ciclo Interno (TDD):** Dentro de la implementación de un paso del ciclo BDD, usar el ciclo "Red-Green-Refactor" para construir los componentes y la lógica de dominio.
- **Nuevas Herramientas:** Se necesitará un framework para BDD (como Cucumber) además de las librerías habituales para TDD (como JUnit).
- **Documentación Viva:** El conjunto de ficheros ```.feature``` de BDD se convierte en una especificación ejecutable y una documentación fiable del comportamiento del sistema.
- **Colaboración:** La redacción de los escenarios BDD es una oportunidad para la colaboración entre diferentes roles (desarrollo, producto, QA).
- **Mayor Rigor:** Requiere más disciplina para mantener ambos niveles de pruebas alineados, pero resulta en un sistema más robusto y mejor documentado.