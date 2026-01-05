package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects

/**
 * Enumerado que define las unidades de medida soportadas por el sistema.
 * El uso de un enum en lugar de texto libre asegura la integridad
 * referencial en la totalidad del dominio. Esto permite realizar conversiones de unidades
 * y comparaciones de precio por unidad (ej. precio por kg) de forma consistente
 * y sin errores de tipografía.
 */
enum class UnitFormat {
    KILOGRAM,
    GRAM,
    LITER,
    CENTILITER,
    MILLILITER,
    DOZEN,
    UNIT;
}