package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.ProductRegistrationResult
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.RegisterProductCommand

interface ProductRegistrationPort {
    suspend fun registerProduct(command: RegisterProductCommand): ProductRegistrationResult
}