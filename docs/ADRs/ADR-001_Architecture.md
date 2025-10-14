# ADR-001: Elección del Patrón de Arquitectura Principal

---

- **Fecha:** 2025-10-03
- **Estado:** Aceptado

## Contexto

---

## Opciones Consideradas

---

### 1. Arquitectura en Capas (N-Tier)

- **Descripción:** El enfoque tradicional que organiza el código en capas horizontales (Presentación, Lógica de Negocio, Acceso a Datos). La comunicación es estrictamente jerárquica y hacia abajo.
- **Ventajas:** Es un patrón simple, muy conocido y rápido de implementar para proyectos sencillos.
- **Inconvenientes:** Genera un fuerte acoplamiento de la lógica de negocio con la capa de datos. Dificulta las pruebas unitarias aisladas del dominio y hace que cambiar tecnologías (como la base de datos) sea una tarea compleja y arriesgada.

### 2. Arquitectura Hexagonal (Puertos y Adaptadores)

- **Descripción:** Aísla el núcleo de la aplicación (el dominio con la lógica de negocio) del mundo exterior. El núcleo define "puertos" (interfaces) para la comunicación, y las tecnologías externas se "enchufan" a través de "adaptadores" que implementan esos puertos.
- **Ventajas:** La lógica de negocio es totalmente independiente de la capa de datos, lo que permite una testabilidad completa y aislada (ideal para TDD). Facilita enormemente el cambio o la adición de tecnologías (ej: añadir un nuevo tipo de consumidor de eventos) sin tocar el dominio.
- **Inconvenientes:** Requiere más disciplina y puede introducir más clases/interfaces (boilerplate) al principio.

## Decisión

---

Se ha decidido adoptar **Arquitectura Hexagonal (Puertos y Adaptadores)** como el patrón principal para el proyecto.

**Justificación:** Esta arquitectura se alinea perfectamente con nuestros objetivos principales. Nos permite **aislar la lógica de negocio** (el cálculo de estandarización de precio unitario, la gestión de compras) en un núcleo puro y agnóstico a la tecnología. Esto es fundamental para poder aplicar
**TDD** de manera efectiva sobre el dominio.

### Consecuencias

--- 

- La estructura del proyecto se organizará en torno a un módulo de ```presentación```, uno de  ```dominio``` y un módulo de ```datos```.
- El desarrollo inicial puede ser ligeramente más lento debido a la necesidad de definir interfaces (puertos) y sus implementaciones (adaptadores).
- A largo plazo, se espera que la mantenibilidad sea mayor y que la adición de nuevas funcionalidades o tecnologías sea más sencilla y segura. 