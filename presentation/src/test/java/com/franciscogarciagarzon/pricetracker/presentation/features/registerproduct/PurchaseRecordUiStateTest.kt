package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PurchaseRecordUiStateTest {

    @Test
    fun `isIdle should cover all logical branches`() {
        // Caso 1: Estado inicial por defecto (Idle real)
        val idleState = PurchaseRecordUiState()
        assertTrue(idleState.isIdle)

        // Caso 2: Cargando (No debe ser idle)
        assertFalse(idleState.copy(isLoading = true).isIdle)

        // Caso 3: Con mensaje de éxito (No debe ser idle)
        assertFalse(idleState.copy(successMessage = UiMessage.DynamicString("Ok")).isIdle)

        // Caso 4: Con mensaje de error (No debe ser idle)
        assertFalse(idleState.copy(errorMessage = UiMessage.DynamicString("Error")).isIdle)

        // Caso 5: Formulario deshabilitado (No debe ser idle)
        assertFalse(idleState.copy(isFormEnabled = false).isIdle)
    }

    @Test
    fun `isSuccess should cover all logical branches`() {
        val baseState = PurchaseRecordUiState()

        // Caso 1: Éxito real (No carga, tiene mensaje, no tiene error, form habilitado)
        val successState = baseState.copy(
            successMessage = UiMessage.DynamicString("Registrado")
        )
        assertTrue(successState.isSuccess)

        // Caso 2: Tiene mensaje pero está cargando (No debe dar success aún)
        assertFalse(successState.copy(isLoading = true).isSuccess)

        // Caso 3: No tiene mensaje de éxito (No es success)
        assertFalse(baseState.isSuccess)

        // Caso 4: Tiene mensaje de éxito pero TAMBIÉN de error (Estado inconsistente, no es success)
        assertFalse(successState.copy(errorMessage = UiMessage.DynamicString("Error")).isSuccess)
    }

    @Test
    fun `result property should store and return the domain result`() {
        val realResult = PurchaseRecordRegistrationResult.DatabaseError

        val state = PurchaseRecordUiState(
            result = realResult
        )

        // Verificamos que el objeto se asigna correctamente
        assertEquals(realResult, state.result)
    }
}