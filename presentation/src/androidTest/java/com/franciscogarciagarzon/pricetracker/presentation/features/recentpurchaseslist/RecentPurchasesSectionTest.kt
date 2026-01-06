package com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.franciscogarciagarzon.pricetracker.presentation.HiltTestActivity
import com.franciscogarciagarzon.pricetracker.presentation.R
import com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist.composables.RecentPurchasesSection
import com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist.composables.RecentPurchasesSectionTestTags.RECENT_PURCHASES_EMPTY_TEXT
import com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist.composables.RecentPurchasesSectionTestTags.RECENT_PURCHASES_SECTION
import com.franciscogarciagarzon.pricetracker.presentation.ui.theme.PriceTrackerTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RecentPurchasesSectionTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Test
    fun recentPurchasesSection_whenSuccess_displaysItems() {
        // Arrange: Creamos un estado de éxito con un producto
        val productName = "Leche Desnatada"
        val mockPurchases = listOf(
            PurchaseRecord(
                name = ProductName(productName),
                unitFormat = UnitFormat.LITER,
                storeName = "Mercamona",
                amount = QuantityPurchased(1.0),
                price = Price(1.20),
                purchaseDate = System.currentTimeMillis()
            )
        )
        val uiState = PurchaseListUiState.Success(mockPurchases)

        // Act: Renderizamos el componente
        composeTestRule.setContent {
            PriceTrackerTheme {
                RecentPurchasesSection(uiState = uiState)
            }
        }

        // Assert: Verificamos que el nombre del producto es visible
        // Nota: PurchaseItem debe usar el nombre del producto en algún Text
        composeTestRule.onNodeWithText(productName).assertIsDisplayed()
        composeTestRule.onNodeWithTag(RECENT_PURCHASES_SECTION).assertIsDisplayed()
    }

    @Test
    fun recentPurchasesSection_whenEmpty_displaysEmptyMessage() {
        // Act: Renderizamos el estado vacío
        composeTestRule.setContent {
            PriceTrackerTheme {
                RecentPurchasesSection(uiState = PurchaseListUiState.Empty)
            }
        }

        // Assert: Buscamos el tag : RECENT_PURCHASES_EMPTY_TEXT
        composeTestRule.onNodeWithTag(RECENT_PURCHASES_EMPTY_TEXT).assertIsDisplayed()
        val expectedText = composeTestRule.activity.getString(R.string.recent_purchases_no_purchases_found)
        composeTestRule.onNodeWithText(expectedText).assertIsDisplayed()
    }
}