package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased

data class PurchaseRecord(
    val name: ProductName,
    val amount: QuantityPurchased,
    val unitFormat: String,
    val price: Price,
    val storeName: String,
    val purchaseDate: Long,
) {
    init {
        require(name.value.isNotBlank()) { "Product name cannot be blank" }
        require(price.value >= 0) { "Price cannot be negative" }
        require(amount.value > 0) { "Amount must be greater than 0" }

    }

}
