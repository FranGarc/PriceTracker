package com.franciscogarciagarzon.pricetracker.presentation.ui.common.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

const val LOADING_INDICATOR_TEST_TAG = "loading_indicator"

@Composable
fun LoadingIndicator(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        LinearProgressIndicator(
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                // Uso de testTag para pruebas de UI automatizadas con Semantics.
                .testTag(LOADING_INDICATOR_TEST_TAG)
        )
    }
}