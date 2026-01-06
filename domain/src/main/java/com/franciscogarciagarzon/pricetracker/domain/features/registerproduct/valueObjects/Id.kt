package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects

import java.util.UUID

/**
 * Value Object para la identidad única de los registros.
 * NOTA: Esta clase se mantiene en reserva para uso futuro.
 * Actualmente la base de datos gestiona la identidad de forma autoincremental
 * en la capa de infraestructura, pero se predefine este tipo para soportar futuras
 * operaciones de edición o borrado (CRUD) desde la capa de dominio sin depender
 * de tipos primitivos.
 * Se utiliza @JvmInline para asegurar que, cuando se implemente, no
 * suponga una carga de memoria adicional al ser tratado como String por la JVM.
 */
@JvmInline
value class Id(val value: String) {
    init {
        // Asegura la integridad mínima: un ID nunca puede nacer vacío.
        require(value.isNotBlank()) { "Product ID cannot be blank" }
    }

    companion object {
        /**
         * Generador de identidades únicas basado en el estándar UUID.
         */
        fun generate(): Id = Id(UUID.randomUUID().toString())
    }

}