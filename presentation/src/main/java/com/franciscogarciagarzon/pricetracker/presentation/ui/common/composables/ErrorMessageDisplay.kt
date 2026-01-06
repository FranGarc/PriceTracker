package com.franciscogarciagarzon.pricetracker.presentation.ui.common.composables

import androidx.compose.runtime.Composable
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage
import com.franciscogarciagarzon.pricetracker.presentation.common.StatusUiState
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.composables.StatusCard
import com.franciscogarciagarzon.pricetracker.presentation.isEmpty


@Composable
fun ErrorMessageDisplay(statusState: StatusUiState, onDismiss: () -> Unit) {
    // Intentamos extraer el recurso de texto solo si hay un mensaje real.
    val message = if (statusState.errorMessage.isEmpty()) null else statusState.errorMessage as? UiMessage.Resource

    message?.let {
        StatusCard(
            message = it.resId,
            isError = true,
            onDismiss = onDismiss
        )
    }
}