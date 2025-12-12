package com.franciscogarciagarzon.pricetracker.presentation.ui.common.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.franciscogarciagarzon.pricetracker.presentation.common.StatusUiState

@Composable
fun LoadingIndicator(statusState: StatusUiState) {
    if (statusState.isLoading) {
        LinearProgressIndicator(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}