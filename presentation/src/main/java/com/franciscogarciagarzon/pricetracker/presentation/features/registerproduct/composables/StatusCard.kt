package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
 * Etiquetas para pruebas automatizadas.
 */
object StatusCardTestTags {
    const val CARD = "card"
    const val MESSAGE_LABEL = "message_label"
    const val DISMISS_BUTTON = "dismiss_button"
}

/**
 * Tarjeta de estado versátil para mensajes de Error o Éxito.
 * Centraliza la lógica visual de notificaciones dentro de la pantalla.
 * Utiliza los roles de color de Material 3 para diferenciar visualmente la gravedad del mensaje.
 * @param message Recurso de texto a mostrar.
 * @param isError Determina si se aplica la paleta de colores de error o de éxito (terciaria).
 * @param onDismiss Acción a ejecutar cuando el usuario cierra la tarjeta.
 */
@Composable
fun StatusCard(
    @StringRes message: Int,
    isError: Boolean,
    onDismiss: () -> Unit
) {
    // Selección dinámica de colores basada en el esquema de Material 3.
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
            // Uso de IconButton o Button con icono para cerrar el mensaje.
            // Se mantiene el 'contentColor' para asegurar contraste en el icono.
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .testTag(StatusCardTestTags.DISMISS_BUTTON),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.statuscard_dismiss_button_content_description),
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
                isError = true,
                onDismiss = {}
            )
        }
    }

}