package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import com.franciscogarciagarzon.commons.utils.Logger
import com.franciscogarciagarzon.commons.utils.contracts.DispatcherProvider
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegisterCommand
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationUseCase
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseValidationError
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import com.franciscogarciagarzon.pricetracker.presentation.utils.StringResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class PurchaseViewModelTest {

    @Mock
    lateinit var mockStringResourceProvider: StringResourceProvider

    @Mock
    private val mockDispatcherProvider: DispatcherProvider = mock()

    @Mock
    lateinit var mockUseCase: PurchaseRecordRegistrationUseCase

    // 3. SUT: System Under Test (the ViewModel)

    private lateinit var viewModel: PurchaseViewModel
    private lateinit var testDispatcher: TestDispatcher

    // --- Test Data for Success ---
    private val SUCCESS_COMMAND = PurchaseIntent.RegisterNewPurchase(
        productName = "Leche",
        quantityPurchased = "1.0",
        unitFormat = "L",
        price = "1.20",
        storeName = "Mercamona"
    )

    @BeforeEach
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        whenever(mockDispatcherProvider.main) doReturn testDispatcher
        whenever(mockDispatcherProvider.io) doReturn testDispatcher
        whenever(mockDispatcherProvider.default) doReturn testDispatcher

        // Mock all required string resources for error cases
        whenever(mockStringResourceProvider.getString(1001))
            .thenReturn("Price must be a valid number (e.g., 2.99, 10).")
        whenever(mockStringResourceProvider.getString(1002))
            .thenReturn("Quantity must be a valid number (e.g., 1, 1.5).")
        whenever(mockStringResourceProvider.getString(1003))
            .thenReturn("Product name must not be empty.")
        whenever(mockStringResourceProvider.getString(1004))
            .thenReturn("Unit must not be empty.")
        whenever(mockStringResourceProvider.getString(1005))
            .thenReturn("Price must be greater than 0.")
        whenever(mockStringResourceProvider.getString(1006))
            .thenReturn("Quantity must be greater than 0.")

        viewModel = PurchaseViewModel(
            dispatchers = mockDispatcherProvider,
            registerPurchaseRecordUseCase = mockUseCase,
            stringResourceProvider = mockStringResourceProvider,
        )

    }

    @AfterEach
    fun tearDown() {
        // [JUnit 5 Teardown]: Reset the Main dispatcher to the original one
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("Initial state is idle")
    fun initialState_isIdle() {
        val expectedState = PurchaseRecordUiState()
        assertEquals(expectedState, viewModel.uiState.value)
    }

    /**
     * Test case for a successful purchase registration, demonstrating MVI state transitions.
     * The test asserts the sequence: Idle -> Loading -> Success.
     */
    @ParameterizedTest(name = "Registro de {0} ({1}) debe ser exitoso")
    // @CsvFileSource(files = arrayOf<String>()) // si se quiere probar con ficheros CSV
    // Usamos @CsvSource para pasar las filas del ejemplo BDD como argumentos
    @CsvSource(
        // | productName |QuantityPurchased | unitFormat | price | store              |
        "Leche, 1.0,L, 1.20, Mercamona",
        "Huevos L, 12.0,unidad, 2.40, Frutería Las nenas",
        "Carne picada de ternera, 400.0, g, 3.54, Carnicería Puri",
        "Harina de Repostería, 1.0, Kg, 1.10, Carreflus"
    )
    @DisplayName("GIVEN valid purchase data, WHEN RegisterNewPurchase intent is handled, THEN UI state shows success")
    fun `GIVEN valid data WHEN RegisterNewPurchase THEN UI state shows success`(
        productName: String,
        quantityPurchased: String,
        unitFormat: String,
        price: String,
        storeName: String
    ) {
        runTest {
            // Arrange
            val intent = PurchaseIntent.RegisterNewPurchase(
                productName, quantityPurchased, unitFormat, price, storeName
            )
            val mockRecord = PurchaseRecord(
                name = ProductName(productName),
                amount = QuantityPurchased(quantityPurchased.toDouble()),
                unitFormat = unitFormat,
                price = Price(price.toDouble()),
                storeName = storeName,
                purchaseDate = System.currentTimeMillis(),
            )
            // Mock the UseCase to return success
            whenever(mockUseCase.registerPurchaseRecord(any()))
                .thenReturn(PurchaseRecordRegistrationResult.Success(mockRecord))

            // Act
            viewModel.handleIntent(intent)
            testDispatcher.scheduler.advanceUntilIdle() // Execute all pending coroutines

            // Assert
            // 1. Check UseCase was called with the correct command
            verify(mockUseCase).registerPurchaseRecord(
                PurchaseRecordRegisterCommand(productName, quantityPurchased, unitFormat, price, storeName)
            )

            // 2. Check the final UI state is Success
            val finalState = viewModel.uiState.value
            Logger.d("TEST", "finalState: $finalState")
            val expectedSuccessState = PurchaseRecordUiState(
                isLoading = false,
                successMessage = "Purchase record successfully registered!",
                isFormEnabled = true
            )
            assertEquals(expectedSuccessState, finalState, "Final state should be Success.")
        }
    }

    /**
     * Test case for a registration failure due to a validation error, demonstrating MVI state transitions.
     * The test asserts the sequence: Idle -> Loading -> Error.
     */
    @Test
    fun givenInvalidData_whenRegisteringPurchase_thenUiStateTransitionsToLoadingAndValidationError() {
        runTest(testDispatcher) {
            // --- SETUP MOCK BEHAVIOR ---
            val errorMessage = "Product name cannot be empty."
            val command = PurchaseRecordRegisterCommand(
                name = "", quantityPurchased = "1.0", unitFormat = "L", price = "1.0", storeName = "Test"
            )

            // Mock a validation error result from the Use Case
            val expectedResult = PurchaseRecordRegistrationResult.ValidationError(PurchaseValidationError.PRODUCT_NAME_EMPTY)
            whenever(mockUseCase.registerPurchaseRecord(any())) doReturn expectedResult

            // Define expected states
            val expectedIdleState = PurchaseRecordUiState()
            val expectedLoadingState = PurchaseRecordUiState(isLoading = true, isFormEnabled = false)
            val expectedErrorState = PurchaseRecordUiState(
                isLoading = false,
                successMessage = null,
                errorMessage = errorMessage,
                isFormEnabled = true
            )

            // --- ASSERT INITIAL STATE ---
            assertEquals(expectedIdleState, viewModel.uiState.value, "Initial state should be default Idle.")
            assertTrue(viewModel.uiState.value.isIdle, "Initial state must report as isIdle = true.")

            // --- WHEN: The registration INTENT is sent with invalid data ---
            val registerIntent = PurchaseIntent.RegisterNewPurchase(
                "", "1.0", "L", "1.0", "Test"
            )
            viewModel.handleIntent(registerIntent)

            // --- ASSERT 1 (LOADING STATE) ---
            assertEquals(expectedLoadingState, viewModel.uiState.value, "State should be Loading after intent is handled.")
            assertFalse(viewModel.uiState.value.isIdle, "State must NOT be idle when loading.")

            // --- EXECUTE COROUTINE ---
            testDispatcher.scheduler.advanceUntilIdle()

            // --- ASSERT 2 (FINAL ERROR STATE) ---
            assertEquals(expectedErrorState, viewModel.uiState.value, "Final state should be Error with the validation message.")
            assertFalse(viewModel.uiState.value.isIdle, "State must NOT be idle when errorMessage is present.")

            // --- VERIFICATION ---
            verify(mockUseCase).registerPurchaseRecord(command)
        }
    }

    /**
     * NEW TEST: Covers the PurchaseRecordRegistrationResult.DatabaseError branch in the ViewModel (Branch Coverage).
     * Also verifies that isIdle returns false when the error message is present.
     */
    @Test
    fun givenDatabaseError_whenRegisteringPurchase_thenUiStateTransitionsToLoadingAndDatabaseError() {
        runTest(testDispatcher) {
            // --- SETUP MOCK BEHAVIOR ---
            // Mock the use case to return the DatabaseError result
            val dbErrorResult = PurchaseRecordRegistrationResult.DatabaseError
            whenever(mockUseCase.registerPurchaseRecord(any())) doReturn dbErrorResult

            // Define expected states
            val expectedIdleState = PurchaseRecordUiState()
            val expectedLoadingState = PurchaseRecordUiState(isLoading = true, isFormEnabled = false)
            val expectedErrorState = PurchaseRecordUiState(
                isLoading = false,
                // Assert against the hardcoded message defined in the ViewModel for this error type
                errorMessage = "A database error occurred during registration.",
                isFormEnabled = true
            )

            // --- ASSERT INITIAL STATE ---
            assertEquals(expectedIdleState, viewModel.uiState.value, "Initial state should be default Idle.")
            assertTrue(viewModel.uiState.value.isIdle, "Initial state must report as isIdle = true.")


            // WHEN: Send Intent
            val registerIntent = PurchaseIntent.RegisterNewPurchase("Test", "1.0", "Kg", "1.0", "Test")
            viewModel.handleIntent(registerIntent)

            // --- ASSERT 1 (LOADING STATE) ---
            assertEquals(expectedLoadingState, viewModel.uiState.value, "State should be Loading after intent is handled.")
            assertFalse(viewModel.uiState.value.isIdle, "State must NOT be idle when loading.")


            // --- EXECUTE COROUTINE ---
            testDispatcher.scheduler.advanceUntilIdle()

            // --- ASSERT 2 (FINAL ERROR STATE) ---
            assertEquals(expectedErrorState, viewModel.uiState.value, "Final state must reflect the hardcoded database error message.")
            assertFalse(viewModel.uiState.value.isIdle, "State should NOT be idle when an error message is visible.")
        }
    }

    /**
     * NEW TEST: Covers the PurchaseIntent.ClearStatus branch in the Reducer (Reducer Line/Branch Coverage).
     * Also verifies the reset to the isIdle=true state.
     */
    @Test
    fun whenClearStatusIntentIsSent_thenStateResetsToIdle() {
        runTest(testDispatcher) {
            val errorMessage = "A previous error message."
            val expectedErrorState = PurchaseRecordUiState(
                errorMessage = errorMessage,
                isFormEnabled = true,
                isLoading = false
            )

            // --- ARRANGE 1: Transition to Error State using a valid Intent (public API) ---
            // This puts the ViewModel into a non-idle state before we test the ClearStatus intent
            viewModel.handleIntent(PurchaseIntent.PurchaseRegistrationError(errorMessage))
            // Advance the clock to execute the state update scheduled by `_uiState.update`
            testDispatcher.scheduler.advanceUntilIdle()
            // Sanity Check: Ensure we reached the non-idle state correctly
            assertEquals(expectedErrorState, viewModel.uiState.value, "ARRANGE: Must be in the Error state before clearing.")
            assertFalse(viewModel.uiState.value.isIdle, "Pre-condition: State should NOT be idle.")

            // --- WHEN: ClearStatus intent is sent (e.g., user dismissed the error banner) ---
            viewModel.handleIntent(PurchaseIntent.ClearStatus)
            // Advance the clock again for the second state update
            testDispatcher.scheduler.advanceUntilIdle()
            // --- ASSERT: The state should reset to the default/idle state ---
            val expectedIdleState = PurchaseRecordUiState()
            assertEquals(expectedIdleState, viewModel.uiState.value, "State must reset to default Idle state (all null/false/true).")
            assertTrue(viewModel.uiState.value.isIdle, "Post-condition: State should be reported as isIdle = true.")
        }
    }

    /**
     * Covers the SetLoading intent and its reducer branch,
     * which was introduced to achieve 100% coverage by forcing all state changes through the reducer.
     */
    @Test
    fun whenSetLoadingIntentIsSent_thenStateTransitionsToLoading() = runTest {
        // ARRANGE: Start in a neutral state (Idle)
        val initialState = viewModel.uiState.value
        assertTrue(initialState.isIdle, "Pre-condition: State must be Idle.")

        // ACT: Send the new loading intent
        viewModel.handleIntent(PurchaseIntent.SetLoading)

        // ASSERT: Verify the state transition using the reducer's logic
        val currentState = viewModel.uiState.value
        assertTrue(currentState.isLoading, "State must transition to Loading.")
        assertFalse(currentState.isFormEnabled, "Form must be disabled.")
        assertNull(currentState.errorMessage, "Error message must be null.")
        assertNull(currentState.successMessage, "Success message must be null.")
        assertFalse(currentState.isIdle, "State must not be idle when loading.")
    }

}

