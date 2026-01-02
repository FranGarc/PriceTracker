package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects

@JvmInline
value class ProductName(val value: String) {
    init {
        require(value.length in 1..100) { "Product name must be between 1 and 100 characters" }
        require(value.isNotBlank()) { "Product name cannot be blank" }
    }

}