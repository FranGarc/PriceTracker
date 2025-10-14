# Guía de Contribución

---

¡Gracias por tu interés en contribuir a la app Price Tracker! Toda ayuda es bienvenida. Para mantener la calidad y la coherencia del proyecto, te pedimos que sigas las directrices de este documento.

Este proyecto sigue una serie de principios de diseño y arquitectura que se detallan en los Architecture Decision Records (ADRs). Esta guía explica cómo aplicarlos en la práctica.

---

## Principios Fundamentales

---
Antes de empezar, es importante que entiendas las decisiones clave que guían nuestro desarrollo:

- **Arquitectura Principal**: Usamos . (Ver ADR-001)
- **Modelado de Dominio**: Aplicamos Domain-Driven Design (DDD). (Ver ADR-002)
- **Estrategia de Pruebas**: Combinamos BDD y TDD. (Ver ADR-003)
- **Lenguaje**: Usamos Kotlin. (Ver ADR-004)

---

## Cómo Proponer Cambios Significativos

---
Cualquier cambio que tenga un impacto significativo en la arquitectura, las dependencias o el flujo de trabajo del proyecto debe ser propuesto y discutido a través de un **Architecture Decision Record (ADR)**.

Un cambio se considera "significativo" si implica, por ejemplo:

- Añadir una nueva librería o framework. 
- Actualizar una versión mayor de una dependencia clave.
- Introducir un nuevo patrón de diseño.

El proceso es simple: **crea un nuevo ADR con el ```Estado: Propuesto```** y ábrelo en una Pull Request para iniciar la discusión.

---

## Lenguaje  
<!--- y Estilo de Código --->

### Kotlin 2.0

<!--- ### Guía de estilo --->

---

## Flujo del Desarrollo

---

Sigue nuestro flujo "de afuera hacia adentro":

### Paso 1: Definir el Comportamiento (BDD)

1. Escribe un escenario ```.feature``` con Gherkin.
2. Ejecuta la prueba para verla fallar.


### Paso 2: Implementar la Lógica (TDD)

1. Empieza en el Dominio: Usa el ciclo "Red-Green-Refactor" para construir la lógica de negocio de forma aislada. 
2. Implementa los Adaptadores: Una vez el dominio es sólido, implementa los componentes de la capa de datos.

---

## Estructura del Proyecto

---

- ```domain```: El núcleo del negocio. Sin dependencias de frameworks.
- ```presentation```: UI.
- ```data```: Los adaptadores (repositorios, datasources, base de datos, etc.).


---

## Proceso de Pull Request (PR)

---

1. Trabaja en una rama: No hagas push directamente a main.
2. Asegura que los tests pasan: Ejecuta toda la suite de tests localmente.
3. Mantén la cobertura: No reduzcas el porcentaje de cobertura de código.
4. Abre la PR: Utiliza un título claro y describe los cambios.
5. Espera la revisión: Se requiere al menos una aprobación para poder hacer merge.


