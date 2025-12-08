package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.uiModel

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseValidationError
import com.franciscogarciagarzon.pricetracker.presentation.R
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage

sealed class PurchaseRecordRegistrationResultUiModel{
    // Success carries data, UI decides how to show it
    data class Success(
        val purchaseRecord: PurchaseRecord?,
        val message: UiMessage = UiMessage.Resource(R.string.PURCHASE_REGISTRATION_SUCCESS)
    ) : PurchaseRecordRegistrationResultUiModel()

    // Errors know how to display themselves
    sealed class Error(val message: UiMessage) : PurchaseRecordRegistrationResultUiModel() {
        object ProductNameEmpty : Error(UiMessage.Resource(R.string.PRODUCT_NAME_EMPTY))
        object UnitEmpty : Error(UiMessage.Resource(R.string.UNIT_EMPTY))
        object PriceInvalidFormat : Error(UiMessage.Resource(R.string.PRICE_INVALID_FORMAT))
        object PriceZeroOrNegative : Error(UiMessage.Resource(R.string.PRICE_IS_ZERO_OR_NEGATIVE))
        object QuantityInvalidFormat : Error(UiMessage.Resource(R.string.QUANTITY_INVALID_FORMAT))
        object QuantityZeroOrNegative : Error(UiMessage.Resource(R.string.QUANTITY_IS_ZERO_OR_NEGATIVE))
        object DatabaseError : Error(UiMessage.Resource(R.string.DATABASE_ERROR))
    }
}

// Extension function
fun PurchaseRecordRegistrationResult.toPresentation(): PurchaseRecordRegistrationResultUiModel {
    return when(this) {
        PurchaseRecordRegistrationResult.DatabaseError ->
            PurchaseRecordRegistrationResultUiModel.Error.DatabaseError

        is PurchaseRecordRegistrationResult.Success ->
            PurchaseRecordRegistrationResultUiModel.Success(this.value)

        is PurchaseRecordRegistrationResult.ValidationError -> when(this.errorType) {
            PurchaseValidationError.PRODUCT_NAME_EMPTY ->
                PurchaseRecordRegistrationResultUiModel.Error.ProductNameEmpty
            PurchaseValidationError.UNIT_EMPTY ->
                PurchaseRecordRegistrationResultUiModel.Error.UnitEmpty
            PurchaseValidationError.PRICE_INVALID_FORMAT ->
                PurchaseRecordRegistrationResultUiModel.Error.PriceInvalidFormat
            PurchaseValidationError.PRICE_IS_ZERO_OR_NEGATIVE ->
                PurchaseRecordRegistrationResultUiModel.Error.PriceZeroOrNegative
            PurchaseValidationError.QUANTITY_INVALID_FORMAT ->
                PurchaseRecordRegistrationResultUiModel.Error.QuantityInvalidFormat
            PurchaseValidationError.QUANTITY_IS_ZERO_OR_NEGATIVE ->
                PurchaseRecordRegistrationResultUiModel.Error.QuantityZeroOrNegative
        }
    }
}


