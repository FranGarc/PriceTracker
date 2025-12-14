package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.franciscogarciagarzon.pricetracker.presentation.HiltTestActivity
import com.franciscogarciagarzon.pricetracker.presentation.R
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.composables.PurchaseFormTestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


// 1. Annotate the test class for Hilt and to uninstall the real DomainModule
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PurchaseRegistrationScreenTest {

    // 2. Set up Hilt and Compose test rules
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

//    // 3. Inject the mock UseCase provided by TestUseCaseModule
//    @Inject
//    lateinit var mockUseCase: PurchaseRecordRegistrationPort

    @Before
    fun setUp() {
        // Initializes Hilt dependencies for the test
        hiltRule.inject()
    }

    @Test
    fun givenValidInput_whenRegisterIsClicked_thenSuccessMessageIsShown() {
        // --- ARRANGE ---
        // 4. Mock the UseCase to return Success when called
//        runBlocking {
//            whenever(mockUseCase.registerPurchaseRecord(any())).thenReturn(
//                PurchaseRecordRegistrationResult.Success(null)
//            )
//        }

        // --- ACT ---
        // 5. Set the content to be tested
        composeTestRule.setContent {
            PurchaseRegistrationScreen() // Hilt will inject the ViewModel with the mocked UseCase
        }

        // 6. Find UI elements by their test tags and perform actions
        composeTestRule.onNodeWithTag(PurchaseFormTestTags.PRODUCT_NAME_INPUT).performTextInput("Milk")
//        composeTestRule.onNodeWithTag(PurchaseFormTestTags.PRODUCT_NAME_INPUT).performTextInput("")
        composeTestRule.onNodeWithTag(PurchaseFormTestTags.QUANTITY_INPUT).performTextInput("1.5")

        // Open the dropdown
        composeTestRule.onNodeWithTag(PurchaseFormTestTags.UNIT_FORMAT_INPUT).performClick()
        // Find the SPECIFIC menu item for "LITER" by its unique test tag and click it.
        composeTestRule.onNodeWithTag("${PurchaseFormTestTags.UNIT_FORMAT_INPUT}_LITER").performClick()


        composeTestRule.onNodeWithTag(PurchaseFormTestTags.PRICE_INPUT).performTextInput("1.20")
        composeTestRule.onNodeWithTag(PurchaseFormTestTags.STORE_NAME_INPUT).performTextInput("Supermarket")

        // Click the register button
        composeTestRule.onNodeWithTag(PurchaseFormTestTags.REGISTER_BUTTON).performClick()

        // --- ASSERT ---
        //7. Verify that the success message is displayed on the screen
        val successMessage = composeTestRule.activity.getString(R.string.PURCHASE_REGISTRATION_SUCCESS)
        composeTestRule.onNodeWithText(successMessage).assertIsDisplayed()
    }
}

