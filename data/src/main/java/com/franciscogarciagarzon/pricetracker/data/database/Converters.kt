package com.franciscogarciagarzon.pricetracker.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date
import javax.inject.Inject

/**
 * Room Type Converters to allow storing complex types like enums in the database.
 */
@ProvidedTypeConverter
class Converters @Inject constructor(private val gson: Gson) {
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

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }@TypeConverter
    fun fromList(list: List<String>?): String? {
        return gson.toJson(list)
    }
}
