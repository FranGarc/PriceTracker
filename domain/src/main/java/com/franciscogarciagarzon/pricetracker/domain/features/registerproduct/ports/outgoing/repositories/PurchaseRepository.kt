package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat

/**
 * Puerto de salida para la persistencia de registros de compra.
 * Siguiendo el principio de Inversión de Dependencias (D), el dominio define
 * este contrato para que la lógica de negocio no dependa de ninguna base de datos específica.
 */
interface PurchaseRepository {

    /**
     * Registra una nueva compra en el sistema de almacenamiento.
     * Se utilizan tipos primitivos (String, Double) en lugar de Value Objects
     * en los parámetros de entrada para facilitar el mapeo en la capa de infraestructura.
     * Las validaciones de negocio ya han sido garantizadas por el Use Case antes de
     * llamar a este método.
     * * @return PurchaseRecordRegistrationResult indicando éxito o el tipo de error técnico (DB).
     */
    suspend fun registerPurchaseRecord(
        name: String,
        quantityPurchased: Double,
        unitFormat: UnitFormat,
        price: Double,
        storeName: String
    ): PurchaseRecordRegistrationResult
}