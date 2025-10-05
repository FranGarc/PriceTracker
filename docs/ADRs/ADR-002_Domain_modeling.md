# ADR-002: Elección del enfoque de modelado de dominio

---

- **Fecha:** 2025-10-03
- **Estado:** Aceptado

## Contexto

Habiendo elegido **Arquitectura Hexagonal** (ADR-001), necesitamos definir cómo se modelará la lógica de negocio y los datos dentro del núcleo de la aplicación. Esta decisión es fundamental para determinar cómo se expresan, se prueban y se mantienen las reglas de negocio. El enfoque elegido debe ser coherente con nuestra
estrategia de pruebas (ADR-003) y nuestros objetivos de mantenibilidad a largo plazo.

---

## Opciones Consideradas

---

### 1. Modelo de Dominio Anémico

- **Descripción:** Los objetos del dominio son simples contenedores de datos (propiedades con getters/setters) y toda la lógica de negocio reside en clases de "Servicio" externas.
- **Inconvenientes:** La lógica de negocio queda dispersa y no encapsulada, lo que dificulta su seguimiento y prueba, yendo en contra de los principios de la Orientación a Objetos.

### 2. Enfoque Centrado en los Datos (Database-First)

- **Descripción:** El diseño comienza en la base de datos, definiendo el esquema, tablas y relaciones primero. El código de la aplicación se deriva de esta estructura de datos.
- **Ventajas:** Es un enfoque muy robusto para aplicaciones donde la estructura y la integridad de los datos son la principal preocupación. Sería una opción muy eficiente para el objeto de la aplicación.

### 3. Enfoque Centrado en los Datos (Database-First)

- **Descripción:** ElSe enfoca en crear un modelo rico del dominio de negocio, donde los objetos (Entidades, Objetos de Valor) encapsulan tanto los datos como el comportamiento relacionado. Se basa en un "Lenguaje Ubicuo" compartido con los expertos del dominio.
- **Ventajas:** Produce un código muy expresivo que refleja fielmente las reglas de negocio. La lógica encapsulada es altamente cohesiva y muy fácil de probar de forma aislada.

## Decisión

---

Se ha decidido adoptar el enfoque de **Domain-Driven Design (DDD)** para modelar el núcleo de la aplicación.

**Justificación:** Somos conscientes de que un **Enfoque Centrado en los Datos**, similar a la filosofía detrás de CQRS, podría funcionar muy bien para este caso de uso. Un registro de precios es, en esencia, una estructura de datos optimizada para escritura y lectura, y diseñar alrededor de la base
de datos es una estrategia válida y performante. Sin embargo, para este proyecto, priorizamos otros dos atributos por encima del rendimiento bruto inicial: **la testabilidad superior y la expresividad del modelo**.

1. **Testabilidad Superior:** DDD nos permite encapsular la lógica compleja (ej: "cómo se recalcula el ranking al añadir una puntuación") dentro de nuestros objetos de dominio. Esto nos da la capacidad de probar estas reglas de negocio como unidades puras y aisladas, sin depender de una base de
   datos ni de ningún framework, lo cual es el pilar de nuestra estrategia de TDD (ADR-003).
2. **Expresividad y Mantenibilidad:** Con DDD, nuestro código hablará el lenguaje del problema que estamos resolviendo. Un objeto PriceTracker no será solo un conjunto de datos, sino que tendrá comportamientos como añadirRegistroDePrecios(tienda, fecha, precio, unidad) u obtenerUltimosPrecios(N).
   Esto hace que el código sea mucho más fácil de entender, razonar y mantener a largo plazo para cualquier persona que se incorpore al proyecto.

Elegimos DDD porque valoramos un diseño limpio, comprobable y expresivo como la mejor inversión para la salud a largo plazo del proyecto.

### Consecuencias

--- 

- El núcleo de la aplicación contendrá objetos de dominio ricos que encapsulan estado y comportamiento.
- Se definirá y utilizará un "Lenguaje Ubicuo" para los conceptos clave (Compra, Producto, Tienda, etc.).
- Se espera que el código del dominio sea extremadamente claro y esté protegido por una suite de tests unitarios robusta. 