package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases

import com.franciscogarciagarzon.pricetracker.domain.common.ResultWithValue
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.PurchaseRecordRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories.PurchaseRepository
import javax.inject.Inject

class PurchaseRecordRegistrationUseCase @Inject constructor(private val productRepository: PurchaseRepository) : PurchaseRecordRegistrationPort {
    override suspend fun registerPurchaseRecord(command: PurchaseRecordRegisterCommand): PurchaseRecordRegistrationResult {

        if (command.name.isBlank()) {
            return PurchaseRecordRegistrationResult.ValidationError(errorType = PurchaseValidationError.PRODUCT_NAME_EMPTY)
        }
        if (command.unitFormat.isBlank()) {
            return PurchaseRecordRegistrationResult.ValidationError(errorType = PurchaseValidationError.UNIT_EMPTY)
        }
        val quantityAsDouble = command.quantityPurchased.toDoubleOrNull() ?: return PurchaseRecordRegistrationResult.ValidationError(PurchaseValidationError.QUANTITY_INVALID_FORMAT)

        val priceAsDouble = command.price.toDoubleOrNull() ?: return PurchaseRecordRegistrationResult.ValidationError(PurchaseValidationError.PRICE_INVALID_FORMAT)

        if (quantityAsDouble <= 0.0) {
            return PurchaseRecordRegistrationResult.ValidationError(PurchaseValidationError.QUANTITY_IS_ZERO_OR_NEGATIVE)
        }

        if (priceAsDouble < 0.0) { // Price can be 0.0 (e.g., a free item), but not negative
            return PurchaseRecordRegistrationResult.ValidationError(PurchaseValidationError.PRICE_IS_ZERO_OR_NEGATIVE)
        }

        return try {
            productRepository.registerPurchaseRecord(
                name = command.name,
                quantityPurchased = quantityAsDouble,
                unitFormat = command.unitFormat,
                price = priceAsDouble,
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
    val quantityPurchased: String,
    val unitFormat: String,
    val price: String,
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
        val errorType: PurchaseValidationError
    ) : PurchaseRecordRegistrationResult() {
        override val value: PurchaseRecord? = null
    }

}

enum class PurchaseValidationError {
    PRODUCT_NAME_EMPTY,
    UNIT_EMPTY,
    PRICE_INVALID_FORMAT,
    PRICE_IS_ZERO_OR_NEGATIVE,
    QUANTITY_INVALID_FORMAT,
    QUANTITY_IS_ZERO_OR_NEGATIVE,
}

