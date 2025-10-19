package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases

import com.franciscogarciagarzon.pricetracker.domain.common.ResultWithValue
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.Product
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.ProductRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories.ProductRepository

class ProductRegistrationUseCase(private val productRepository: ProductRepository) : ProductRegistrationPort {
    override suspend fun registerProduct(command: RegisterProductCommand): ProductRegistrationResult {

        if (command.name.isBlank() || command.unitFormat.isBlank()) {
            return ProductRegistrationResult.ValidationError("Product name and unit format cannot be blank.")
        }
        if (command.price < 0) {
            return ProductRegistrationResult.ValidationError("Product price cannot be negative.")
        }

        return try {
            productRepository.registerProduct(
                command.name,
                command.unitFormat,
                command.price,
                command.storeName,

                )
        } catch (e: Exception) {
            e.printStackTrace()
            ProductRegistrationResult.DatabaseError
        }
    }
}

data class RegisterProductCommand(
    val name: String,
    val unitFormat: String,
    val price: Double,
    val storeName: String
)

sealed class ProductRegistrationResult : ResultWithValue<Product> {
    data class Success(
        override val value: Product?
    ) : ProductRegistrationResult()

    object DatabaseError : ProductRegistrationResult() {
        override val value: Product? = null
    }

    data class ValidationError(
        val message: String
    ) : ProductRegistrationResult() {
        override val value: Product? = null
    }
}

