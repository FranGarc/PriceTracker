package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import com.franciscogarciagarzon.pricetracker.presentation.UiMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PurchaseReducerTest {

    @Test
    fun `reducer should cover SetLoading branch`() {
        val initialState = PurchaseRecordUiState(isLoading = false, isFormEnabled = true)

        // Act: Forzamos la rama que faltaba
        val newState = reducer(initialState, PurchaseIntent.SetLoading)

        // Assert
        assertEquals(true, newState.isLoading)
        assertEquals(false, newState.isFormEnabled)
    }

    @Test
    fun `isSuccess branches coverage`() {
        val base = PurchaseRecordUiState(
            isLoading = false,
            successMessage = UiMessage.DynamicString("Ok"),
            errorMessage = UiMessage.None,
            isFormEnabled = true
        )

        // Caso true
        assertEquals(true, base.isSuccess)

        // Casos false para cubrir todas las ramas (los && de la propiedad)
        assertEquals(false, base.copy(isLoading = true).isSuccess)
        assertEquals(false, base.copy(successMessage = UiMessage.None).isSuccess)
        assertEquals(false, base.copy(errorMessage = UiMessage.DynamicString("Err")).isSuccess)
        assertEquals(false, base.copy(isFormEnabled = false).isSuccess)
    }
}