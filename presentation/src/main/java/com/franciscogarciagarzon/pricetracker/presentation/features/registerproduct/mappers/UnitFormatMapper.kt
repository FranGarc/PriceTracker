package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.mappers

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.franciscogarciagarzon.pricetracker.presentation.R

/**
 * A Composable mapper function that converts a domain [UnitFormat] enum
 * into a localized, user-facing string.
 */

fun UnitFormat.toDisplayName(): Int {
    return when (this) {
        UnitFormat.KILOGRAM ->  R.string.unit_format_kilogram
        UnitFormat.GRAM ->  R.string.unit_format_gram
        UnitFormat.LITER ->  R.string.unit_format_liter
        UnitFormat.CENTILITER ->  R.string.unit_format_centiliter
        UnitFormat.MILLILITER ->  R.string.unit_format_milliliter
        UnitFormat.DOZEN ->  R.string.unit_format_dozen
        UnitFormat.UNIT -> R.string.unit_format_unit
    }
}
