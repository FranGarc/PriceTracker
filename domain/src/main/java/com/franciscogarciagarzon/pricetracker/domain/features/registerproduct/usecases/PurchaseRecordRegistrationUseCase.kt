package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases

import com.franciscogarciagarzon.pricetracker.domain.common.ResultWithValue
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.PurchaseRecordRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories.PurchaseRepository

class PurchaseRecordRegistrationUseCase(private val productRepository: PurchaseRepository) : PurchaseRecordRegistrationPort {
    override suspend fun registerPurchaseRecord(command: PurchaseRecordRegisterCommand): PurchaseRecordRegistrationResult {

        if (command.name.isBlank() || command.unitFormat.isBlank()) {
            return PurchaseRecordRegistrationResult.ValidationError("Product name and unit format cannot be blank.")
        }
        if (command.price < 0) {
            return PurchaseRecordRegistrationResult.ValidationError("Product price cannot be negative.")
        }

        if (command.quantityPurchased < 1) {
            return PurchaseRecordRegistrationResult.ValidationError("Product amount must be greater than 0.")
        }

        return try {
            productRepository.registerPurchaseRecord(
                name = command.name,
                quantityPurchased = command.quantityPurchased,
                unitFormat = command.unitFormat,
                price = command.price,
                storeName = command.storeName,

                )
        } catch (e: Exception) {
            e.printStackTrace()
            PurchaseRecordRegistrationResult.DatabaseError
        }
    }
}

data class PurchaseRecordRegisterCommand(
    val name: String,
    val quantityPurchased: Double,
    val unitFormat: String,
    val price: Double,
    val storeName: String
)

sealed class PurchaseRecordRegistrationResult : ResultWithValue<PurchaseRecord> {
    data class Success(
        override val value: PurchaseRecord?
    ) : PurchaseRecordRegistrationResult()

    object DatabaseError : PurchaseRecordRegistrationResult() {
        override val value: PurchaseRecord? = null
    }

    data class ValidationError(
        val message: String
    ) : PurchaseRecordRegistrationResult() {
        override val value: PurchaseRecord? = null
    }
}

