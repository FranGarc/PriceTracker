package com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.franciscogarciagarzon.pricetracker.presentation.ui.theme.PriceTrackerTheme
import java.util.Locale

/**
 * PurchaseItem: Representación visual de un registro de compra individual.
 * - 'testTag' dinámico: Permite identificar elementos específicos en los tests de UI
 * basándose en el contenido de los datos.
 */
@Composable
fun PurchaseItem(
    purchase: PurchaseRecord,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("purchase_item_${purchase.name.value}"),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Título: Nombre del producto
                Text(
                    text = purchase.name.value,
                    style = MaterialTheme.typography.titleMedium
                )
                // Subtítulo: Nombre del establecimiento
                Text(
                    text = purchase.storeName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // Valor: Precio formateado
            // Se usa primary color para resaltar el dato más relevante de la app: el precio.
            Text(
                text = "${String.format(
                    Locale.getDefault(),
                    "%.2f", purchase.price.value
                )}€",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PurchaseItemPreview() {
    val samplePurchase = PurchaseRecord(
        name = ProductName("Aceite de Oliva"),
        unitFormat = UnitFormat.LITER,
        storeName = "Cooperativa Local",
        amount = QuantityPurchased(5.0),
        price = Price(45.50),
        purchaseDate = System.currentTimeMillis()
    )

    PriceTrackerTheme {
        PurchaseItem(
            purchase = samplePurchase,
            modifier = Modifier.padding(8.dp)
        )
    }
}