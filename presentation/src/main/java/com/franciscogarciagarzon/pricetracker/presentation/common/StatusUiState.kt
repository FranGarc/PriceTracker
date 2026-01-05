package com.franciscogarciagarzon.pricetracker.presentation.common

import com.franciscogarciagarzon.pricetracker.presentation.UiMessage

/**
 * StatusUiState: Contrato base para el estado de la interfaz de usuario.
 *
 * Define el conjunto mínimo de propiedades que cualquier pantalla de la aplicación
 * debe tener para gestionar el feedback del usuario.
 *
 * Se implementa en las 'data classes' de estado (UiState) de cada pantalla.
 * Utiliza el tipo [UiMessage] para asegurar que los mensajes sean localizables.
 *
 * Permite crear componentes de Compose genéricos (ej. ErrorBanner)
 * que acepten un [StatusUiState] en lugar de estados específicos de cada pantalla.
 *
 * Garantiza que nunca se nos olvide añadir la lógica de carga o
 * manejo de errores al crear una nueva funcionalidad.
 */
interface StatusUiState {
    val isLoading: Boolean
    val errorMessage: UiMessage
    val successMessage: UiMessage
}