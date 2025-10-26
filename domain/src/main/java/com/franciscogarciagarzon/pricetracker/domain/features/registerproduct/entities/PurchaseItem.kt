package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductId
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName

data class PurchaseItem(
    val id: ProductId,
    val name: ProductName,
    val amount: QuantityPurchased,
    val unitFormat: String,
    val price: Price,
    val storeName: String
) {
    init {
        require(name.value.isNotBlank()) { "Product name cannot be blank" }
        require(price.value >= 0) { "Price cannot be negative" }
        require(amount.value > 0) { "Amount must be greater than 0" }

    }

}
