# ADR-007: Elección del Stack de Presentación

---

- **Fecha:** 2025-10-03
- **Estado:** Aceptado

## Contexto

---
Tras haber seleccionado Kotlin como lenguaje de programación (ADR-004), es necesario tomar unas decisiones para la capa de presentación: **UI Toolkit**  y **Patrón de Presentación**. La elección debe equilibrar la productividad, el rendimiento y la alineación con los objetivos del proyecto.

## Opciones Consideradas

---

### UI Toolkit

1. **Android Views:** Piedra angular de la IU de Android. Emplea layouts XML que se inflan en Activities y/o Fragments. Es un sistema estable y bien documentado con el que hay mucha familiaridad. En contra tiene que es muy verboso, puede dar problemas de rendimiento al inflar la interfaz, y la gestión de interfaz es difícil.
2. **Jetpack Compose:** UI Toolkit moderno. Usa una sintaxis declarativa en Kotlin, emplea menos código, y está unificado en todas las plataformas (Sobremesa/Web con Compose Multiplatform). En contra tiene que es una tecnología más reciente, la comunidad puede ser más pequeña - aunque crece rápidamente -, tiene una curva de aprendizaje más alta, y hay features que siguen en desarrollo.  

### Patrón de Presentación

1. **MVP:** Tiene un flujo de datos doble, es altamente testable y fácil de entender. Pero requiere boilerplate y suele precisar manejo manual del ciclo de vida para evitar leaks de memoria. Bueno para Android Views.
2. **MVVM:** Su flujo de datos es Reactivo (observable), se integra de forma excelente con las API Android modernas (Live Data, Flow, Compose State), y tiene menos boilerplate que MVP. Sin embargo, el ViewModel puede volverse enorme, y testar la Vista sigue siendo difícil. Excelente para Compose.
3. **MVI:** De flujo de datos unidireccional, es predecible, debugable y es fácil de razonar (el estado es la única fuente de verdad), el estado queda representado explicitamente por una ```sealed class```. Su boilerplate inicial es más elevado (definir Intents, Resultados, y el Estado como objeto único). Excelente para Compose con estados complejos. 


## Decisión

---

Se ha decidido utilizar **Jetpack Compose** como UI Kit. 

**Justificación:** La elección de Jetpack Compose se debe a los siguientes argumentos:

- **Alineación con Objetivos Profesionales:** un objetivo principal de este proyecto es el desarrollo profesional y la adquisición de nuevos conocimientos dentro del ecosistema Kotlin y Android. Adoptar Jetpack Compose como stack de desarrollo es una oportunidad para profundizar y ganar experiencia en una tecnología moderna y ampliamente utilizada en el ecosistema Android, lo cual es un activo valioso.
- **Rendimiento Mejorado:** Compose está optimizado para los dispositivos modernos, ofreciendo un mejor rendimiento al renderizar IUs complejas.


Se ha decidido utilizar **MVI** como Patrón de Presentación.

**Justificación:** La elección de MVI se debe a los siguientes argumentos:

- **Excelente para el UI toolkit elegido:** Tanto MVVM como MVI son ideales para trabajar con UIs en Compose.
- **Alineación con Objetivos Profesionales:** un objetivo principal de este proyecto es el desarrollo profesional y la adquisición de nuevos conocimientos dentro del ecosistema Kotlin y Android. Ya hay experiencia con MVVM, pero MVI aborda el problema del ViewModel enorme, en especial usando el Reducer. 
