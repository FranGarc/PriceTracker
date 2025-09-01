# Guía de Contribución [WIP]

¡Gracias por tu interés en contribuir a la app PriceTracker! Toda ayuda es bienvenida. Para mantener la calidad y la coherencia del proyecto, te pedimos que sigas las directrices de este documento.

Este proyecto sigue una serie de principios de diseño y arquitectura que se detallan en los Architecture Decision Records (ADRs)[Enlazar]. Esta guía explica cómo aplicarlos en la práctica.

## Principios Fundamentales

Antes de empezar, es importante que entiendas las decisiones clave que guían nuestro desarrollo:

- Arquitectura Principal: (definir y enlazar al ADR correspondiente)
- Modelado de Dominio: (definir y enlazar al ADR correspondiente)
- Estrategia de Pruebas: (definir y enlazar al ADR correspondiente)
- Lenguaje: (definir y enlazar al ADR correspondiente)

## Cómo Proponer Cambios Significativos

Cualquier cambio que tenga un impacto significativo en la arquitectura, las dependencias o el flujo de trabajo del proyecto debe ser propuesto y discutido a través de un Architecture Decision Record (ADR).

Un cambio se considera "significativo" si implica, por ejemplo:

  - Añadir una nueva librería o framework.
  - Actualizar una versión mayor de una dependencia clave.
  - Introducir un nuevo patrón de diseño.

El proceso es simple: crea un nuevo ADR con el Estado: Propuesto y ábrelo en una Pull Request para iniciar la discusión.

## Lenguaje y Estilo de Código

Kotlin [version]

El proyecto utiliza Kotlin [version]. Se espera que los contribuidores aprovechen las características modernas del lenguaje para escribir un código más limpio, seguro y eficiente. En particular:

  [Listar elementos y patrones específicos] 


## Guía de Estilo
El código debe seguir la Guía de Estilo de Kotlin de Google. Se recomienda configurar el formateador automático del IDE para asegurar la consistencia.

## Flujo de Desarrollo

Sigue nuestro flujo "de afuera hacia adentro":
Paso 1: Definir el Comportamiento (BDD)

  - Escribe un escenario .feature con Gherkin. https://www.youtube.com/watch?v=rkAi_j3lHWk
  - Ejecuta la prueba para verla fallar.

Paso 2: Implementar la Lógica (TDD)

  - Empieza en el Dominio: Usa el ciclo "Red-Green-Refactor" para construir la lógica de negocio de forma aislada.
  - Implementa los Adaptadores: Una vez el dominio es sólido, implementa los componentes de la capa de datos/infraestructura.

## Estructura del Proyecto

  - domain: El núcleo del negocio. Sin dependencias de frameworks.
  - [en android se suelen llevar juntas modelo ed negocio y casos de uso?] application: Los casos de uso que orquestan el dominio.
  - infrastructure/data: [definir]
  - presentation: UI

## Proceso de Pull Request (PR)
1. Trabaja en una rama: No hagas push directamente a main.
2. Asegura que los tests pasan: Ejecuta toda la suite de tests localmente.
3. Mantén la cobertura: No reduzcas el porcentaje de cobertura de código.
4. Abre la PR: Utiliza un título claro y describe los cambios.
5. Espera la revisión: Se requiere al menos una aprobación para poder hacer merge.


