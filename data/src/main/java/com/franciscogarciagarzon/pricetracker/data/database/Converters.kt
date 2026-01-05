package com.franciscogarciagarzon.pricetracker.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date
import javax.inject.Inject

/**
 * Conversores de tipos para Room.
 * SQLite solo soporta tipos de datos básicos. Esta clase permite
 * persistir objetos complejos (Enums, Dates, Lists) transformándolos en tipos
 * primitivos compatibles.
 * Se usa @ProvidedTypeConverter para permitir la Inyección de Dependencias.
 * Esto asegura que usemos la misma instancia de Gson configurada en toda la app,
 * mejorando el rendimiento y la consistencia en la serialización.
 */
@ProvidedTypeConverter
class Converters @Inject constructor(private val gson: Gson) {
    /**
     * Mapeo de Enums: Se utiliza .name para asegurar que el valor guardado sea
     * el identificador único del Enum, facilitando búsquedas SQL legibles.
     */
    @TypeConverter
    fun fromUnitFormat(unit: UnitFormat): String {
        return unit.name
    }

    /**
     * Convierte una String de la base de datos a [UnitFormat].
     */
    @TypeConverter
    fun toUnitFormat(value: String): UnitFormat {
        return UnitFormat.valueOf(value)
    }

    /**
     * Mapeo de Fechas: Se transforman a Long (Timestamp) para optimizar
     * las comparaciones y el ordenamiento a nivel de base de datos.
     */
    @Suppress("unused")
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @Suppress("unused")
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    /**
     * Mapeo de Listas: Se utiliza serialización JSON para almacenar colecciones
     * de Strings en una sola columna de texto.
     */
    @Suppress("unused")
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
    @Suppress("unused")
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return gson.toJson(list)
    }
}
