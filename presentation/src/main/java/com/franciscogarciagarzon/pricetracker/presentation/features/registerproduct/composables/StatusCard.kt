package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.franciscogarciagarzon.pricetracker.presentation.R
import com.franciscogarciagarzon.pricetracker.presentation.ui.theme.PriceTrackerTheme

/**
 * Common Composable for displaying success or error messages.
 */

object StatusCardTestTags {
    const val CARD = "card"
    const val MESSAGE_LABEL = "message_label"
    const val DISMISS_BUTTON = "dismiss_button"
}

@Composable
fun StatusCard(
    @StringRes message: Int,
    isError: Boolean,
    onDismiss: () -> Unit
) {
    val containerColor = if (isError) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }
    val contentColor = if (isError) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onTertiaryContainer
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag(StatusCardTestTags.CARD)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(message),
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .testTag(StatusCardTestTags.MESSAGE_LABEL)
            )
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .testTag(StatusCardTestTags.DISMISS_BUTTON),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = contentColor
                )
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true,

    )
fun StatusCardPreview() {
    PriceTrackerTheme {
        Surface {
            StatusCard(
                message = R.string.DATABASE_ERROR,
                isError = false,
                onDismiss = {}
            )
        }
    }

}