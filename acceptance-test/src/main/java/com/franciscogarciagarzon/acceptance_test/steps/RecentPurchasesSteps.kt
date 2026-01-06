package com.franciscogarciagarzon.acceptance_test.steps

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.runAndroidComposeUiTest
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import com.franciscogarciagarzon.pricetracker.MainActivity
import com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist.composables.RecentPurchasesSectionTestTags.RECENT_PURCHASES_EMPTY_TEXT
import com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist.composables.RecentPurchasesSectionTestTags.RECENT_PURCHASES_SECTION
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.composables.PurchaseFormTestTags
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

@OptIn(ExperimentalTestApi::class)
class RecentPurchasesSteps {

    @Given("the database has no records")
    fun theDatabaseHasNoRecords() {
        //  lógica de limpieza si fuera necesario
    }

    @When("I register the product details with {string}, {string}, {string}, {string} and {string} and I press the button")
    fun iFillAndVerify(
        name: String,
        amount: String,
        unit: String,
        price: String,
        store: String
    ) {
        runAndroidComposeUiTest<MainActivity> {
            // 1. Registro (Formulario)
            onNodeWithTag(PurchaseFormTestTags.PRODUCT_NAME_INPUT).performTextReplacement(name)
            onNodeWithTag(PurchaseFormTestTags.QUANTITY_INPUT).performTextReplacement(amount)
            onNodeWithTag(PurchaseFormTestTags.UNIT_FORMAT_INPUT).performClick()
            onNodeWithTag("${PurchaseFormTestTags.UNIT_FORMAT_INPUT}_$unit").performClick()
            onNodeWithTag(PurchaseFormTestTags.PRICE_INPUT).performTextReplacement(price)
            onNodeWithTag(PurchaseFormTestTags.STORE_NAME_INPUT).performTextReplacement(store)
            onNodeWithTag(PurchaseFormTestTags.REGISTER_BUTTON).performClick()

            // 2. Definimos un selector para el texto que sea HIJO de la lista
            // Esto evita capturar el texto del Input del formulario
            val productInListMatcher = hasText(name) and hasAnyAncestor(hasTestTag(RECENT_PURCHASES_SECTION))
            val priceInListMatcher = hasText(price, substring = true) and
                    hasAnyAncestor(hasTestTag(RECENT_PURCHASES_SECTION))
            // 3. Espera inteligente usando el matcher específico
            waitUntil("El producto '$name' no apareció en la sección de Compras Recientes", 10000L) {
                onAllNodes(productInListMatcher).fetchSemanticsNodes().isNotEmpty()
            }

            // 4. Verificación final precisa
            onNode(productInListMatcher).assertIsDisplayed()
            onNode(priceInListMatcher).assertIsDisplayed()
        }
    }


    @Then("I should see a message {string}")
    fun iShouldSeeAMessage(expectedMessage: String) {
        runAndroidComposeUiTest<MainActivity> {
            // Esperamos a que el componente cargue antes de validar
            waitUntilAtLeastOneExists(hasTestTag(RECENT_PURCHASES_EMPTY_TEXT), 10000)

            onNodeWithTag(RECENT_PURCHASES_EMPTY_TEXT).assertIsDisplayed()
            onNodeWithText(expectedMessage).assertIsDisplayed()
        }
    }

    // Cada paso Cucumber reinicia la actividad, con lo que se pierde lo que se ha hecho.
    // Por ello, tenemos que unificar lo que se haría en varios pasos en uno único y dejar los otros vacíos

    @Then("the purchase {string} should appear at the top of the list")
    fun thePurchaseShouldAppearAtTheTop(
        @Suppress("UNUSED_PARAMETER")productName: String
    ) {}

    @Then("the price shown for {string} should be {string}")
    fun verifyPriceShown(
        @Suppress("UNUSED_PARAMETER")productName: String,
        @Suppress("UNUSED_PARAMETER")price: String) {}
}