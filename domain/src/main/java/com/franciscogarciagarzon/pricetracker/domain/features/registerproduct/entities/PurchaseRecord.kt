package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat

data class PurchaseRecord(
    val name: ProductName,
    val amount: QuantityPurchased,
    val unitFormat: UnitFormat,
    val price: Price,
    val storeName: String,
    val purchaseDate: Long,
)
