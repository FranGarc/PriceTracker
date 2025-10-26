package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases

import com.franciscogarciagarzon.pricetracker.domain.common.ResultWithValue
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseItem
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.PurchaseItemRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories.PurchaseRepository

class PurchaseItemRegistrationUseCase(private val productRepository: PurchaseRepository) : PurchaseItemRegistrationPort {
    override suspend fun registerPurchaseItem(command: PurchaseItemRegisterCommand): PurchaseItemRegistrationResult {

        if (command.name.isBlank() || command.unitFormat.isBlank()) {
            return PurchaseItemRegistrationResult.ValidationError("Product name and unit format cannot be blank.")
        }
        if (command.price < 0) {
            return PurchaseItemRegistrationResult.ValidationError("Product price cannot be negative.")
        }

        if (command.quantityPurchased < 1) {
            return PurchaseItemRegistrationResult.ValidationError("Product amount must be greater than 0.")
        }

        return try {
            productRepository.registerPurchaseItem(
                name = command.name,
                quantityPurchased = command.quantityPurchased,
                unitFormat = command.unitFormat,
                price = command.price,
                storeName = command.storeName,

                )
        } catch (e: Exception) {
            e.printStackTrace()
            PurchaseItemRegistrationResult.DatabaseError
        }
    }
}

data class PurchaseItemRegisterCommand(
    val name: String,
    val quantityPurchased: Double,
    val unitFormat: String,
    val price: Double,
    val storeName: String
)

sealed class PurchaseItemRegistrationResult : ResultWithValue<PurchaseItem> {
    data class Success(
        override val value: PurchaseItem?
    ) : PurchaseItemRegistrationResult()

    object DatabaseError : PurchaseItemRegistrationResult() {
        override val value: PurchaseItem? = null
    }

    data class ValidationError(
        val message: String
    ) : PurchaseItemRegistrationResult() {
        override val value: PurchaseItem? = null
    }
}

