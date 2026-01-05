package com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.franciscogarciagarzon.pricetracker.presentation.R
import com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist.PurchaseListUiState
import com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist.composables.RecentPurchasesSectionTestTags.RECENT_PURCHASES_EMPTY_TEXT
import com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist.composables.RecentPurchasesSectionTestTags.RECENT_PURCHASES_ERROR_TEXT
import com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist.composables.RecentPurchasesSectionTestTags.RECENT_PURCHASES_SECTION
import com.franciscogarciagarzon.pricetracker.presentation.ui.common.composables.LoadingIndicator
import com.franciscogarciagarzon.pricetracker.presentation.ui.theme.PriceTrackerTheme

object RecentPurchasesSectionTestTags {
    const val RECENT_PURCHASES_SECTION = "recent_purchases_section"
    const val RECENT_PURCHASES_EMPTY_TEXT = "recent_purchases_empty_text"
    const val RECENT_PURCHASES_ERROR_TEXT = "recent_purchases_error_text"
}

@Composable
fun RecentPurchasesSection(
    uiState: PurchaseListUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag(RECENT_PURCHASES_SECTION)
    ) {
        Text(
            text = stringResource(R.string.recent_purchases_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (uiState) {
            is PurchaseListUiState.Loading -> {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    LoadingIndicator(isLoading = true)
                }
            }

            is PurchaseListUiState.Empty -> {
                Text(
                    text = stringResource(R.string.recent_purchases_no_purchases_found),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 24.dp)
                        .testTag(RECENT_PURCHASES_EMPTY_TEXT)
                )
            }

            is PurchaseListUiState.Success -> {
                // Al ser solo los últimos 5, usamos Column para evitar
                // conflictos de scroll con la pantalla principal
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.purchases.forEach { purchase ->
                        PurchaseItem(purchase = purchase)
                    }
                }
            }

            is PurchaseListUiState.Error -> {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag(RECENT_PURCHASES_ERROR_TEXT)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Recent Purchases - Success")
@Composable
fun RecentPurchasesSectionSuccessPreview() {
    val mockPurchases = listOf(
        PurchaseRecord(
            name = ProductName("Leche Entera"),
            unitFormat = UnitFormat.LITER,
            storeName = "Mercamona",
            amount = QuantityPurchased(1.0),
            price = Price(1.20),
            purchaseDate = System.currentTimeMillis()
        ),
        PurchaseRecord(
            name = ProductName("Pan de Molde"),
            unitFormat = UnitFormat.UNIT,
            storeName = "Carreflus",
            amount = QuantityPurchased(2.0),
            price = Price(2.10),
            purchaseDate = System.currentTimeMillis()
        )
    )

    PriceTrackerTheme {
        RecentPurchasesSection(
            uiState = PurchaseListUiState.Success(mockPurchases),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, name = "Recent Purchases - Empty State")
@Composable
fun RecentPurchasesSectionEmptyPreview() {
    PriceTrackerTheme {
        RecentPurchasesSection(
            uiState = PurchaseListUiState.Empty,
            modifier = Modifier.padding(16.dp)
        )
    }
}