# ADR-005: Implementación de la Arquitectura Hexagonal

---

- **Fecha:** 2025-10-03
- **Estado:** Aceptado

## Contexto

---

Tras la decisión de adoptar una Arquitectura Hexagonal ([ADR-001](ADR-001_Architecture.md)), es crucial definir cómo se implementará esta estructura físicamente en nuestro proyecto Gradle. La forma en que organicemos el código (en paquetes o en módulos) determinará el nivel de protección que tendremos para evitar violaciones arquitectónicas y asegurar que el dominio permanezca aislado de la capa de datos.

## Opciones Consideradas

---

# 1. Un Solo Módulo (Separación por Paquetes)

- **Descripción:** Todo el código reside en un único módulo de Gradle. La separación entre ```domain``` , ```presentation``` e ```data``` se realiza únicamente a través de paquetes de Java
- **Ventajas:** Es la configuración más simple y rápida de iniciar.
- **Inconvenientes:** No ofrece ninguna garantía en tiempo de compilación. El respeto a las fronteras de la arquitectura depende enteramente de la disciplina del desarrollador.

# 2. Multi-módulo de Gradle
 
- **Descripción:** El proyecto se divide en módulos de Gradle independientes (```domain```, ```presentation```, ```data```, ```app```). Las dependencias entre ellos se definen explícitamente en los ficheros de construcción, permitiendo que ```data``` y  ```presentation```  dependan de ```domain```,  pero nunca al revés ni  ```presentation``` y ```data``` tengan relación entre sí.
- **Ventajas:** Proporciona garantías en tiempo de compilación, haciendo imposible crear dependencias que violen la arquitectura. La estructura del proyecto refleja fielmente el diseño arquitectónico.
- **Inconvenientes:** La configuración de la build es ligeramente más compleja al tener que gestionar las relaciones entre módulos.


## Decisión

---

Se ha decidido implementar la Arquitectura Hexagonal utilizando un enfoque de **multi-módulo de Gradle.**


**Justificación:** La principal razón para esta elección es que proporciona mayores garantías estructurales en cuanto a la organización del proyecto. Al definir explícitamente las dependencias entre los módulos (```domain```, ```presentation```, ```data```, ```app```), utilizamos el propio compilador como una herramienta para hacer cumplir nuestra arquitectura. Esto previene de forma automática y sistemática las violaciones de la regla de dependencia (que el dominio no conozca las otras capas), que es el pilar de la Arquitectura Hexagonal.

Este enfoque representa el punto de equilibrio ideal para nuestro proyecto: es significativamente más robusto y seguro que la simple separación por paquetes.

### Consecuencias

--- 

- El proyecto se organizará en una estructura de directorios multi-módulo. Como mínimo, se crearán los siguientes módulos: ```domain```, ```presentation```, ```data```.
- El fichero ```settings.gradle.kts``` definirá los módulos incluidos en la construcción.
- Los ficheros ```build.gradle.kts``` de cada módulo definirán sus dependencias. El módulo ```domain```, por ejemplo, no tendrá dependencias de frameworks.
- Esta estructura modular será la base para todo el desarrollo futuro, asegurando un desacoplamiento limpio entre las distintas capas de la aplicación.
