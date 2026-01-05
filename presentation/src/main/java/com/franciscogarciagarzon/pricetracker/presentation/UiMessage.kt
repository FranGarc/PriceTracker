package com.franciscogarciagarzon.pricetracker.presentation

import androidx.annotation.StringRes

/**
 * Representación agnóstica de mensajes para la interfaz de usuario.
 * Permite que las capas lógicas (ViewModel/Domain) definan mensajes
 * sin depender del Context de Android. La resolución del texto real se delega
 * a la capa de UI (Compose), manteniendo la lógica de negocio testeable.
 */
sealed class UiMessage {
    /**
     * Para mensajes que vienen del servidor, excepciones o strings generados dinámicamente.
     */
    data class DynamicString(val text: String) : UiMessage()

    /**
     * Para mensajes localizados en strings.xml.
     * @param resId Referencia al recurso (R.string.xxx).
     * @param args Argumentos para strings formateados (ej. "Precio: %d €").
     */
    data class Resource(
        @param:StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : UiMessage()

    /**
     * Representa la ausencia de mensaje (Estado inicial o reset).
     */
    object None : UiMessage()
}

/**
 * Extensión de utilidad para comprobar si el mensaje es nulo o vacío de forma semántica.
 */
fun UiMessage.isEmpty(): Boolean {
    return this is UiMessage.None
}