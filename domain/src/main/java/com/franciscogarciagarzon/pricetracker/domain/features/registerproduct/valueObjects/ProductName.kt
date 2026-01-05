package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects

/**
 * Value Object que encapsula el nombre de un producto.
 * El uso de @JvmInline permite que el dominio use el tipo ProductName
 * para mayor claridad semántica y validación, mientras que la JVM lo procesa como
 * un String primitivo para optimizar el rendimiento.
 */
@JvmInline
value class ProductName(val value: String) {
    init {
        // Se imponen límites físicos y lógicos al nombre del producto.
        // El límite de 100 caracteres previene abusos en la UI y asegura la
        // compatibilidad con los límites de almacenamiento de la infraestructura.
        require(value.length in 1..100) { "Product name must be between 1 and 100 characters" }
        // Un nombre compuesto solo por espacios se considera inválido
        // para asegurar que la información mostrada al usuario final tenga sentido.
        require(value.isNotBlank()) { "Product name cannot be blank" }
    }

}