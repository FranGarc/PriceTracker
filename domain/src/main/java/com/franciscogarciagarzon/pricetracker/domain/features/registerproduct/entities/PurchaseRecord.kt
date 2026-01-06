package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat

/**
 * Entidad que representa "una línea del ticket de compra", el registro completo de una compra individual.
 * Se utiliza una 'data class' para representar este modelo de dominio,
 * aprovechando la inmutabilidad y los métodos generados (copy, equals, hashCode)
 * que facilitan el flujo de datos reactivo.
 */
data class PurchaseRecord(
    // Se utilizan tipos específicos (Value Objects) en lugar de primitivos
    // para garantizar que las reglas de validación se apliquen desde la construcción.
    val name: ProductName,
    val amount: QuantityPurchased,
    val unitFormat: UnitFormat,
    val price: Price,

    // El nombre de la tienda se mantiene como String simple al no
    // tener todavía lógica de validación o comportamiento asociado en el dominio.
    val storeName: String,

    /**
     * Fecha de la compra en formato Timestamp (Long).
     * Se opta por Long para facilitar la interoperabilidad entre capas
     * (BD, UI, Dominio) y simplificar el almacenamiento, dejando el formateo
     * visual para la capa de Presentation.
     */
    val purchaseDate: Long,
)
