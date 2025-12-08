package com.franciscogarciagarzon.pricetracker.data.database

import androidx.room.TypeConverter
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat

/**
 * Room Type Converters to allow storing complex types like enums in the database.
 */
class Converters {
    /**
     * Converts a [UnitFormat] enum into a String for database storage.
     * We store the enum's `name` (e.g., "KILOGRAM"), which is stable and language-independent.
     */
    @TypeConverter
    fun fromUnitFormat(unit: UnitFormat): String {
        return unit.name
    }

    /**
     * Converts a String from the database back into a [UnitFormat] enum.
     */
    @TypeConverter
    fun toUnitFormat(value: String): UnitFormat {
        return UnitFormat.valueOf(value)
    }
}
