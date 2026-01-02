package com.franciscogarciagarzon.pricetracker.presentation.ui.common.composables

import androidx.compose.runtime.Composable
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage
import com.franciscogarciagarzon.pricetracker.presentation.common.StatusUiState
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.composables.StatusCard
import com.franciscogarciagarzon.pricetracker.presentation.isEmpty


@Composable
fun ErrorMessageDisplay(statusState: StatusUiState, onDismiss: () -> Unit) {
    val message = if (statusState.errorMessage.isEmpty()) null else statusState.errorMessage as? UiMessage.Resource

    message?.let {
        StatusCard(
            message = it.resId,
            isError = true,
            onDismiss = onDismiss // Use the passed handler
        )
    }
}