package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.uiModel

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseValidationError
import com.franciscogarciagarzon.pricetracker.presentation.R
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage

/**
 * Modelo de representación de UI para el resultado de registro de una compra.
 * Desacopla los errores lógicos del dominio de su representación visual.
 * Al centralizar aquí la asignación de recursos (R.string), el ViewModel permanece
 * agnóstico a las strings de Android, facilitando el testing y la internacionalización.
 */
sealed class PurchaseRecordRegistrationResultUiModel{
    // Éxito: contiene el registro opcional y un mensaje positivo predeterminado.
    data class Success(
        val purchaseRecord: PurchaseRecord?,
        val message: UiMessage = UiMessage.Resource(R.string.PURCHASE_REGISTRATION_SUCCESS)
    ) : PurchaseRecordRegistrationResultUiModel()

    // Errores: cada subtipo asocia un error de dominio con su string correspondiente.
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

/**
 * Mapper que transforma el resultado de dominio en un modelo de presentación.
 * Este mapeo ocurre en la frontera entre la capa de Domain y Presentation.
 * Transforma el enum de error técnico en un objeto que la UI puede renderizar directamente.
 */
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


