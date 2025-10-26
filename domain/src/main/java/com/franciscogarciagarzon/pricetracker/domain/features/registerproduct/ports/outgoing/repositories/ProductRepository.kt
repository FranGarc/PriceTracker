package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.ProductRegistrationResult

interface ProductRepository {

    suspend fun registerProduct(name: String, quantityPurchased: Double, unitFormat: String, price: Double, storeName: String): ProductRegistrationResult
}