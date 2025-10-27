package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects

import java.util.UUID

@JvmInline
value class Id(val value: String) {
    init {
        require(value.isNotBlank()) { "Product ID cannot be blank" }
    }

    companion object {
        fun generate(): Id = Id(UUID.randomUUID().toString())
    }

}