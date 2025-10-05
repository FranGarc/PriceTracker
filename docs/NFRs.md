# Requisitos No Funcionales (NFRs) - App Price Tracker

- **Versión**: 1.0
- **Fecha**: 2025-10-03
- **Estado**: Aceptado

## Introducción

--- 
Este documento describe los Requisitos No Funcionales para la Versión 1.0 (MVP) de la app PriceTracker. Estos requisitos definen los criterios de calidad,

## 1. Rendimiento y Escalabilidad

--- 

- **NFR-01(Responsividad)**: Las comparaciones de históricos de precios de un único producto de todas las tiendas guardadas se debe cargar y mostrar en **1 segundo**.
- **NFR-02(Throughput)**: La app debe ser capaz de procesar y registrar eficientemente una nueva *Compra* (incluyendo la estandarización de precios y la actualización del Agregado PriceTracker) en en **500 milisegundos** desde la entrada del usuario.
- **NFR-03(Volumen de datos)**: El sistema debe poder manejar entre **5000 y 10000 entradas de Registro de Precio** sin una degradación notable en rendimiento de consulta.


## 2. Usabilidad y Experiencia de Usuario (UX)

--- 
- **NFR-04(Accesibilidad)**: La app debe cumplir con los estándares básicos de accesibilidad de Android (e.g.: definiciones de contenido para servicios de accesibilidad).
- **NFR-05(Entrada Intuitiva)**: Registrar una nueva compra debe ser un proceso fluido de varios pasos que permita al usuario completar la transacción en menos de *30 segundos*.
- **NFR-06(Capacidad Offline)**: Los usuarios deben poder *registrar nuevas compras y ver el histórico existente* cuando el dispositivo no tiene conectividad alguna. 



## 3. Mantenimiento y Testabilidad

--- 

- **NFR-07(Cobertura de tests)**: Toda la **Capa de Dominio** debe quedar cubierta por tests unitarios al 90% para asegurar que cambios en las capas de Presentación o Datos no rompen las reglas de negocio. 
- **NFR-08(Cumplimiento de Arquitectura)**: El sistema debe hacer cumplir estrictamente las reglas de la Arquitectura Hexagonal, siendo esto verificable por herramientas de testing de arquitectura que se ejecuten en cada build.
- **NFR-09(Modularidad)**: Las capas de Presentación, Dominio, y Datos deben ser implementados como módulos Gradle separados, cuyas dependencias quedarán gestionadas con interfaces de Inyección de Dependencias.
 


## 4. Copias de Seguridad

--- 

- **NFR-10(Exportación de datos)**: El sistema debe permitir poder exportar los datos de alguna forma que pueda llevarse a otro dispositivo. 
- **NFR-11(Importación de datos)**: El sistema debe permitir poder importar los datos que la misma app exporta. 

