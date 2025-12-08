package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects

/**
 * Represents the standardized units of measure for products.
 * This is a pure domain concept, free of any UI-specific text.
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