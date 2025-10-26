package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects

@JvmInline
value class QuantityPurchased(val value: Double) {
    init {
        require(value > 0) { "QuantityPurchased cannot be negative nor zero" }
    }
}