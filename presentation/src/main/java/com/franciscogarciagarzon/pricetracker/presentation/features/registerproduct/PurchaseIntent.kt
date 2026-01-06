package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage

/**
 * Representación formal de todas las acciones y eventos en el flujo de compra.
 * Centraliza la comunicación entre la UI y el ViewModel. Al ser una 'sealed interface',
 * permite que el compilador verifique que el Reducer maneje todos los casos posibles (exhaustividad).
 */sealed interface PurchaseIntent {
    /**
     * Acción del Usuario: Intento de persistir una nueva compra.
     */
    data class RegisterNewPurchase(
        val productName: String,
        val quantityPurchased: String,
        val unitFormat: UnitFormat,
        val price: String,
        val storeName: String
    ) : PurchaseIntent

    /**
     * Acción del Sistema/Usuario: Limpia los mensajes de éxito/error de la UI.
     */
    data object ClearStatus : PurchaseIntent
    /**
     * Acción del Sistema: Fuerza el estado de carga (Loading).
     */
    data object SetLoading : PurchaseIntent
    /**
     * Evento del Sistema: Resultado exitoso del proceso de dominio.
     */
    data class PurchaseRegistrationSuccess(val message: UiMessage) : PurchaseIntent
    /**
     * Evento del Sistema: Fallo en la validación o persistencia del dominio.
     */
    data class PurchaseRegistrationError(val message: UiMessage) : PurchaseIntent
}