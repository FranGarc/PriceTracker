package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects

@JvmInline
value class Price(val value: Double) {
    init {
        require(value >= 0) { "Price cannot be negative" }
    }

    operator fun plus(other: Price): Price = Price(this.value + other.value)
    operator fun times(quantity: Int): Price = Price(this.value * quantity)

}