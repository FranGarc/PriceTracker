package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects

/**
 * Value Object que representa la cantidad de producto adquirida.
 * Se utiliza @JvmInline para que, en tiempo de ejecución, esta clase se comporte
 * como un 'double' primitivo, evitando la creación de objetos adicionales en memoria y
 * optimizando el rendimiento en listas extensas de registros.
  Al usar una 'value class', garantizamos que una Cantidad no sea tratada
 * como un simple Double genérico en el resto del dominio, evitando errores donde
 * se sumen cantidades accidentales a precios o viceversa.
 */
@JvmInline
value class QuantityPurchased(val value: Double) {
    init {
        // A diferencia del precio (que podría ser 0 en promociones),
        // una compra sin cantidad no tiene sentido lógico en este dominio.
        // Por ello, se restringe el valor a ser estrictamente mayor que cero.
        require(value > 0) { "QuantityPurchased cannot be negative nor zero" }
    }
}