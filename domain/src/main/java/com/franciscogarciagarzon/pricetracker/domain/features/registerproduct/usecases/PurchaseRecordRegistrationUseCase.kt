package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases

import com.franciscogarciagarzon.pricetracker.domain.common.ResultWithValue
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.PurchaseRecordRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories.PurchaseRepository
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import javax.inject.Inject

/**
 * Implementación del caso de uso para registrar una compra.
 * Se separa la lógica de validación de la lógica de persistencia. El Use Case
 * actúa como barrera de entrada, asegurando que solo datos íntegros lleguen al repositorio.
 */
class PurchaseRecordRegistrationUseCase @Inject constructor(private val productRepository: PurchaseRepository) : PurchaseRecordRegistrationPort {
    override suspend fun registerPurchaseRecord(command: PurchaseRecordRegisterCommand): PurchaseRecordRegistrationResult {

        // Validaciones manuales antes de la conversión de tipos para capturar
        // errores de entrada de usuario sin lanzar excepciones costosas.
        if (command.name.isBlank()) {
            return PurchaseRecordRegistrationResult.ValidationError(errorType = PurchaseValidationError.PRODUCT_NAME_EMPTY)
        }
        // Uso de toDoubleOrNull() para validar el formato numérico de forma segura,
        // permitiendo manejar Strings vacíos o mal formateados como errores de validación específicos.
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
            // El bloque catch en el Use Case garantiza que cualquier fallo imprevisto
            // en la infraestructura (DB, Red, etc.) se traduzca en un tipo de resultado de dominio
            // (DatabaseError), evitando que la app se cierre.
            e.printStackTrace()
            PurchaseRecordRegistrationResult.DatabaseError
        }
    }
}

/**
 * Objeto de transferencia de datos (DTO) de entrada.
 * Los campos numéricos se reciben como String para simplificar el binding con la UI
 * y permitir que la lógica de validación del dominio decida cómo procesar formatos inválidos.
 */
data class PurchaseRecordRegisterCommand(
    val name: String,
    val quantityPurchased: String,
    val unitFormat: UnitFormat,
    val price: String,
    val storeName: String
)

/**
 * Representación sellada de los posibles resultados del registro.
 * Al implementar ResultWithValue<PurchaseRecord>, cumplimos con el contrato global
 * de resultados, permitiendo que la capa de presentación maneje el éxito y los errores de
 * forma exhaustiva mediante expresiones 'when'.
 */
sealed class PurchaseRecordRegistrationResult : ResultWithValue<PurchaseRecord> {
    data class Success(
        override val value: PurchaseRecord?
    ) : PurchaseRecordRegistrationResult()

    // Los estados de error devuelven null en 'value' para forzar a la UI a
    // gestionar el mensaje de error en lugar de intentar leer un dato inexistente.
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

