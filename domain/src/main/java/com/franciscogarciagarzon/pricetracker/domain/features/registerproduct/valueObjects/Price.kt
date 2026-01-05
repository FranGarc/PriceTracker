package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects

/**
 * Value Object que representa el precio de un producto.
 * El uso de @JvmInline permite que Price sea tratado como un 'double'
 * primitivo en tiempo de ejecución, eliminando la sobrecarga de memoria (autoboxing)
 * Al usar una 'value class', garantizamos que un Precio no sea tratado
 * como un simple Double genérico en el resto del dominio, evitando errores donde
 * se sumen cantidades accidentales a precios o viceversa.
 */
@JvmInline
value class Price(val value: Double) {
    init {
        // Un precio de 0.0 es válido (regalos/promociones),
        // pero un precio negativo no tiene sentido en este dominio de compras.
        // Se aplica 'fail-fast' para evitar cálculos financieros erróneos.
        require(value >= 0) { "Price cannot be negative" }
    }
    /**
     * Sobrecarga del operador 'plus'.
     * Permite usar la sintaxis natural 'precio1 + precio2' en el dominio,
     * devolviendo siempre un nuevo objeto Price válido y manteniendo la inmutabilidad.
     */
    operator fun plus(other: Price): Price = Price(this.value + other.value)
    /**
     * Sobrecarga del operador 'times' (multiplicación).
     * Facilita el cálculo del coste total de un registro multiplicando
     * el precio unitario por una cantidad entera.
     */
    operator fun times(quantity: Int): Price = Price(this.value * quantity)

}