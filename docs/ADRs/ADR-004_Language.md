# ADR-004: Elección del Lenguaje de Programación

---

- **Fecha:** 2025-10-03
- **Estado:** Aceptado

## Contexto

---
Es necesario seleccionar un lenguaje de programación principal para la implementación de la app "PriceTracker". Esta es una de las decisiones de tecnología más fundamentales, ya que impactará el ecosistema de librerías, el framework, el rendimiento, y el flujo de trabajo general. La elección debe
alinearse no solo con los requisitos técnicos del proyecto, sino también con sus objetivos de desarrollo profesional.

## Opciones Consideradas

---

1. **Java:** Un lenguaje orientado a objetos, de alto rendimiento y con uno de los ecosistemas más grandes y maduros del mundo.
2. **Kotlin:** Una alternativa moderna en la JVM. Es más conciso y seguro que Java, con el que es 100% interoperable.

## Decisión

---

Se ha decidido usar **Kotlin** como el lenguaje principal para el desarrollo de la app.

**Justificación:** La decisión se basa en los siguientes criterios:

1. **Alineación con Objetivos Profesionales:** un objetivo principal de este proyecto es el desarrollo profesional y la adquisición de nuevos conocimientos dentro del ecosistema Kotlin y Android. Adoptar Kotlin permite centrar todos los esfuerzos en aplicar patrones avanzados (DDD, Arquitectura Hexagonal) y explorar
   tecnologías del ecosistema (Room, Compose, etc.) en profundidad, en lugar de desviar el foco hacia el aprendizaje de la sintaxis y las particularidades de un nuevo lenguaje.
2. **Maximización de la Productividad:** Al utilizar un lenguaje en el que ya se posee una experiencia sólida, se elimina la curva de aprendizaje inicial. Esto permite avanzar directamente a los desafíos arquitectónicos y de diseño, así como integrar nuevas tecnologías que son el verdadero núcleo de
   este proyecto.
3. **Ecosistema a prueba de futuro:** Las herramientas de desarrollo modernas de Kotlin y el procesado de anotación más eficiente (KSP) **pueden llevar a ciclos de compilación más rápidos**. Además, para tareas comunes se reduce sensiblemente el código necesario, reduciendo el *boilerplate* y
   permitiendo centrarse en la lógica de negocio. Por último, Google ha dejado claras sus intenciones: **Kotlin es el futuro.**

### Consecuencias

--- 

- El proyecto se desarrollará sobre la plataforma Kotlin, utilizando una versión reciente (ej: Kotlin 2.0.x) para aprovechar sus características modernas.

