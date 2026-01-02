package com.franciscogarciagarzon.acceptance_test.steps

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.runAndroidComposeUiTest
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import androidx.test.platform.app.InstrumentationRegistry
import com.franciscogarciagarzon.pricetracker.MainActivity
import com.franciscogarciagarzon.pricetracker.presentation.R
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.composables.PurchaseFormTestTags
import io.cucumber.java.After
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import java.util.Locale

@OptIn(ExperimentalTestApi::class)
class PriceInputSteps {

    @io.cucumber.java.Before
    fun forceLocale() {
        Locale.setDefault(Locale.US)
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val config = context.resources.configuration
        config.setLocale(Locale.US)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    @After
    fun tearDown() {
        // Clean up the activity after each scenario
    }

    @Given("I am at the price input screen")
    fun iAmAtThePriceInputScreen() {
    }

    @When("I fill the product details with {string}, {string}, {string}, {string} and {string} and I press the button")
    fun iFillTheProductDetailsWithAndAndIPressTheButton(name: String, amount: String, unit: String, price: String, store: String) {
        // Usamos runAndroidComposeUiTest para lanzar la actividad UNA VEZ.
        runAndroidComposeUiTest<MainActivity> {
            // 1. Rellenar datos
            waitUntilAtLeastOneExists(hasTestTag(PurchaseFormTestTags.PRODUCT_NAME_INPUT), 10000)

            onNodeWithTag(PurchaseFormTestTags.PRODUCT_NAME_INPUT).performTextReplacement(name)
            onNodeWithTag(PurchaseFormTestTags.QUANTITY_INPUT).performTextReplacement(amount)

            onNodeWithTag(PurchaseFormTestTags.UNIT_FORMAT_INPUT).performClick()
            onNodeWithTag("${PurchaseFormTestTags.UNIT_FORMAT_INPUT}_$unit").performClick()

            onNodeWithTag(PurchaseFormTestTags.PRICE_INPUT).performTextReplacement(price)
            onNodeWithTag(PurchaseFormTestTags.STORE_NAME_INPUT).performTextReplacement(store)

            // 2. Pulsar el botón INMEDIATAMENTE (mismo bloque, misma Activity)
            onNodeWithTag(PurchaseFormTestTags.REGISTER_BUTTON)
                .assertIsEnabled()
                .performClick()
            // 3. VERIFICACIÓN INMEDIATA (Aquí evitamos que la Activity se cierre)
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val expectedMessage = context.getString(R.string.PURCHASE_REGISTRATION_SUCCESS)

            // Esperamos a que aparezca la StatusCard de éxito
            waitUntilAtLeastOneExists(hasText(expectedMessage), 10000)
            onNodeWithText(expectedMessage).assertIsDisplayed()
            waitForIdle()
        }
    }

    @Then("the fields go blank and I see a success message")
    fun theFieldsGoBlankAndISeeSuccessMessage() {
    }

    @Then("I fill the form with {string}, {string}, {string}, {string}, {string} and I should see error {string}")
    fun fillAndVerifyError(name: String, amount: String, unit: String, price: String, store: String, errorMessage: String) {
        runAndroidComposeUiTest<MainActivity> {
            // 1. Esperar y Rellenar
            waitUntilAtLeastOneExists(hasTestTag(PurchaseFormTestTags.PRODUCT_NAME_INPUT), 10000)

            onNodeWithTag(PurchaseFormTestTags.PRODUCT_NAME_INPUT).performTextReplacement(name)
            onNodeWithTag(PurchaseFormTestTags.QUANTITY_INPUT).performTextReplacement(amount)

            onNodeWithTag(PurchaseFormTestTags.UNIT_FORMAT_INPUT).performClick()
            onNodeWithTag("${PurchaseFormTestTags.UNIT_FORMAT_INPUT}_$unit").performClick()

            onNodeWithTag(PurchaseFormTestTags.PRICE_INPUT).performTextReplacement(price)
            onNodeWithTag(PurchaseFormTestTags.STORE_NAME_INPUT).performTextReplacement(store)

            // 2. Intentar pulsar el botón
            // Nota: Si el botón está deshabilitado por el ViewModel, performClick fallará.
            // Si tu lógica es "validar al pulsar", esto funcionará:
            onNodeWithTag(PurchaseFormTestTags.REGISTER_BUTTON).performClick()

            // 3. Verificación inmediata del error (Misma sesión, misma Activity)
            waitUntilAtLeastOneExists(hasText(errorMessage), 10000)
            onNodeWithText(errorMessage).assertIsDisplayed()

            waitForIdle()
        }
    }

}