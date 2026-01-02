package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.mappers

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.franciscogarciagarzon.pricetracker.presentation.R

/**
 * A Composable mapper function that converts a domain [UnitFormat] enum
 * into a localized, user-facing string.
 */
@Composable
fun UnitFormat.toDisplayName(): String {
    return when (this) {
        UnitFormat.KILOGRAM -> stringResource(id = R.string.unit_format_kilogram)
        UnitFormat.GRAM -> stringResource(id = R.string.unit_format_gram)
        UnitFormat.LITER -> stringResource(id = R.string.unit_format_liter)
        UnitFormat.CENTILITER -> stringResource(id = R.string.unit_format_centiliter)
        UnitFormat.MILLILITER -> stringResource(id = R.string.unit_format_milliliter)
        UnitFormat.DOZEN -> stringResource(id = R.string.unit_format_dozen)
        UnitFormat.UNIT -> stringResource(id = R.string.unit_format_unit)
    }
}
