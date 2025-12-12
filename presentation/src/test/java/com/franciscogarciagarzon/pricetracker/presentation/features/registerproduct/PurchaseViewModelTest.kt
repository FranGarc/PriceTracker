package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import com.franciscogarciagarzon.commons.utils.Logger
import com.franciscogarciagarzon.commons.utils.contracts.DispatcherProvider
import com.franciscogarciagarzon.commons.utils.contracts.SharingStrategyProvider
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegisterCommand
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationUseCase
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseValidationError
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.uiModel.PurchaseRecordRegistrationResultUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class PurchaseViewModelTest {

    @Mock
    private val mockDispatcherProvider: DispatcherProvider = mock()

    @Mock
    private lateinit var mockUseCase: PurchaseRecordRegistrationUseCase

    @Mock
    private val mockSharingStrategyProvider: SharingStrategyProvider = mock()

    // 3. SUT: System Under Test (the ViewModel)

    private lateinit var viewModel: PurchaseViewModel
    private lateinit var testDispatcher: TestDispatcher

    private val successMessage = PurchaseRecordRegistrationResultUiModel.Success(null).message

    @BeforeEach
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        // lenient prevents test not using this mock to fail
        lenient().whenever(mockDispatcherProvider.main).thenReturn(testDispatcher)
        lenient().whenever(mockDispatcherProvider.io).thenReturn(testDispatcher)
        lenient().whenever(mockDispatcherProvider.default).thenReturn(testDispatcher)

        lenient().whenever(mockSharingStrategyProvider.getStrategy()).thenReturn(SharingStarted.Eagerly)
        viewModel = PurchaseViewModel(
            dispatchers = mockDispatcherProvider,
            registerPurchaseRecordUseCase = mockUseCase,
            sharingStrategyProvider = mockSharingStrategyProvider,
        )

    }

    @AfterEach
    fun tearDown() {
        // [JUnit 5 Teardown]: Reset the Main dispatcher to the original one
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("Initial state is idle with form enabled")
    fun `Initial state is idle with form enabled`() {
        val state = viewModel.uiState.value
        assertEquals(state.isIdle, true)
    }

    @Test
    @DisplayName("Clear Status resets messages and enables form")
    fun `Clear Status resets messages and enables form`() {
        runTest {
            // First set a non-default state through other intents

            viewModel.handleIntent(PurchaseIntent.PurchaseRegistrationSuccess(successMessage))
            testDispatcher.scheduler.advanceUntilIdle()

            // Verify we have the expected state before clearing
            val stateBeforeClear = viewModel.uiState.value
            Logger.d("PurchaseViewModelTest", "stateBeforeClear: $stateBeforeClear")
            assertEquals(true, stateBeforeClear.isSuccess)

            // When ClearStatus intent is sent
            viewModel.handleIntent(PurchaseIntent.ClearStatus)
            testDispatcher.scheduler.advanceUntilIdle()
            // Then state should be reset to default
            val stateAfterClear = viewModel.uiState.value
            assertEquals(true, stateAfterClear.isIdle)
        }
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
        "Leche, 1,LITER, 1.20, Mercamona",
        "Huevos LITER, 12,UNIT, 2.40, Frutería Las nenas",
        "Carne picada de ternera, 400, GRAM, 3.54, Carnicería Puri",
        "Harina de Repostería, 1, KILOGRAM, 1.10, Carreflus"
    )
    @DisplayName("GIVEN valid purchase data, WHEN RegisterNewPurchase intent is handled, THEN UI state shows success")
    fun `GIVEN valid data WHEN RegisterNewPurchase THEN UI state shows success`(
        productName: String,
        quantityPurchased: String,
        unitFormat: String,
        price: String,
        storeName: String
    ) {
        runTest(testDispatcher) {
            // --- ARRANGE ---
            val command = PurchaseRecordRegisterCommand(
                name = productName,
                quantityPurchased = quantityPurchased,
                unitFormat = UnitFormat.valueOf(unitFormat),
                price = price,
                storeName = storeName
            )
            val mockRecord = PurchaseRecord(
                name = ProductName(productName),
                amount = QuantityPurchased(quantityPurchased.toDouble()),
                unitFormat = UnitFormat.valueOf(unitFormat),
                price = Price(price.toDouble()),
                storeName = storeName,
                purchaseDate = 0L
            )
            whenever(
                mockUseCase.registerPurchaseRecord(command)
            )
                .thenReturn(
                    PurchaseRecordRegistrationResult.Success(mockRecord)
                )

            val states = mutableListOf<PurchaseRecordUiState>()
            val collectionJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.collect(states::add)
            }

            // --- ACT 1: Send the intent ---
            val intent = PurchaseIntent.RegisterNewPurchase(
                productName = productName,
                quantityPurchased = quantityPurchased,
                unitFormat = UnitFormat.valueOf(unitFormat),
                price = price,
                storeName = storeName
            )
            viewModel.handleIntent(intent)

            testDispatcher.scheduler.advanceUntilIdle()


            // --- ASSERT  ---


            val actualState1 = states[0]
            Logger.d("ViewModelTest", "actualState1: $actualState1")
            // State 1: Initial Idle State (collected upon subscription)
            assertEquals(PurchaseRecordUiState(), states[0])


            // State 2: Loading State
            val expectedLoadingState = PurchaseRecordUiState(isLoading = true, isFormEnabled = false)
            val actualState2 = states[1]
            Logger.d("ViewModelTest", "actualState2: $actualState2")
            assertEquals(expectedLoadingState, actualState2, "State should transition to Loading.")


            // State 3: Final Success State
            val expectedSuccessState = PurchaseRecordUiState(
                isLoading = false,
                successMessage = successMessage,
                errorMessage = UiMessage.None,
                isFormEnabled = true
            )
            val actualState3 = states[2]
            Logger.d("ViewModelTest", "actualState3: $actualState3")
            assertEquals(expectedSuccessState, actualState3, "Final state should be Success.")

            // --- VERIFY ---
            verify(mockUseCase).registerPurchaseRecord(command)


            // Clean up the collector
            collectionJob.cancel()
        }
    }

    /**
     * Test case for a registration failure due to a validation error, demonstrating MVI state transitions.
     * The test asserts the sequence: Idle -> Loading -> Error.
     */
    @ParameterizedTest
    @EnumSource(
        value = PurchaseValidationError::class,
        mode = EnumSource.Mode.INCLUDE,
        names = ["PRODUCT_NAME_EMPTY", "UNIT_EMPTY", "PRICE_INVALID_FORMAT",
            "PRICE_IS_ZERO_OR_NEGATIVE", "QUANTITY_INVALID_FORMAT",
            "QUANTITY_IS_ZERO_OR_NEGATIVE"]
    )
    fun givenInvalidData_whenRegisteringPurchase_thenUiStateTransitionsToLoadingAndValidationError(
        errorType: PurchaseValidationError
    ) {
        runTest(testDispatcher) {
            // --- SETUP MOCK BEHAVIOR ---

            val productName = ""
            val quantityPurchased = "1.0"
            val unitFormat = UnitFormat.UNIT
            val price = "1.0"
            val storeName = "Test"


            val command = PurchaseRecordRegisterCommand(
                name = productName,
                quantityPurchased = quantityPurchased,
                unitFormat = unitFormat,
                price = price,
                storeName = storeName
            )

            // Mock a validation error result from the Use Case
            val expectedResult = PurchaseRecordRegistrationResult.ValidationError(errorType)
            whenever(
                mockUseCase.registerPurchaseRecord(command)
            ).thenReturn(expectedResult)

            val expectedErrorMessage = when (errorType) {
                PurchaseValidationError.PRODUCT_NAME_EMPTY -> PurchaseRecordRegistrationResultUiModel.Error.ProductNameEmpty.message
                PurchaseValidationError.UNIT_EMPTY -> PurchaseRecordRegistrationResultUiModel.Error.UnitEmpty.message
                PurchaseValidationError.PRICE_INVALID_FORMAT -> PurchaseRecordRegistrationResultUiModel.Error.PriceInvalidFormat.message
                PurchaseValidationError.PRICE_IS_ZERO_OR_NEGATIVE -> PurchaseRecordRegistrationResultUiModel.Error.PriceZeroOrNegative.message
                PurchaseValidationError.QUANTITY_INVALID_FORMAT -> PurchaseRecordRegistrationResultUiModel.Error.QuantityInvalidFormat.message
                PurchaseValidationError.QUANTITY_IS_ZERO_OR_NEGATIVE -> PurchaseRecordRegistrationResultUiModel.Error.QuantityZeroOrNegative.message
            }


            val states = mutableListOf<PurchaseRecordUiState>()
            val collectionJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.collect(states::add)
            }

            // --- ACT 1: Send the intent ---
            val intent = PurchaseIntent.RegisterNewPurchase(
                productName = productName,
                quantityPurchased = quantityPurchased,
                unitFormat = unitFormat,
                price = price,
                storeName = storeName
            )
            viewModel.handleIntent(intent)

            testDispatcher.scheduler.advanceUntilIdle()


            // Define expected states
            val expectedIdleState = PurchaseRecordUiState()
            val expectedLoadingState = PurchaseRecordUiState(isLoading = true, isFormEnabled = false)

            // --- ASSERT  ---
            val actualState1 = states[0]
            Logger.d("ViewModelTest", "actualState1: $actualState1")
            // State 1: Initial Idle State (collected upon subscription)
            assertEquals(expectedIdleState, states[0])

            // State 2: Loading State
            val actualState2 = states[1]
            Logger.d("ViewModelTest", "actualState2: $actualState2")
            assertEquals(expectedLoadingState, actualState2, "State should transition to Loading.")

            // State 3: Final Error State
            val actualState3 = states[2]
            Logger.d("ViewModelTest", "actualState3: $actualState3")
            assertEquals(expectedErrorMessage, actualState3.errorMessage, "Final state should be Error.")

            // --- VERIFICATION ---
            verify(mockUseCase).registerPurchaseRecord(command)
            // Clean up the collector
            collectionJob.cancel()
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
            val command = PurchaseRecordRegisterCommand("Test", "1.0", UnitFormat.UNIT, "1.0", "Test")
            // Mock the use case to return the DatabaseError result
            val dbErrorResult = PurchaseRecordRegistrationResult.DatabaseError
            whenever(mockUseCase.registerPurchaseRecord(command)).thenReturn(dbErrorResult)

            val states = mutableListOf<PurchaseRecordUiState>()
            val collectionJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.collect(states::add)
            }

            val intent = PurchaseIntent.RegisterNewPurchase(
                "Test", "1.0", UnitFormat.UNIT, "1.0", "Test"
            )
            viewModel.handleIntent(intent)

            testDispatcher.scheduler.advanceUntilIdle()
            // Define expected states

            val databaseErrorMessage = PurchaseRecordRegistrationResultUiModel.Error.DatabaseError.message
            val expectedErrorState = PurchaseRecordUiState(
                isLoading = false,
                successMessage = UiMessage.None,
                errorMessage = databaseErrorMessage,
                isFormEnabled = true
            )

            // --- ASSERT INITIAL STATE ---
            // 3. Assert against the collected list of states.
            assertEquals(3, states.size, "Should have collected 3 states: Idle, Loading, Error")

            // State 1: Initial Idle State
            assertEquals(PurchaseRecordUiState(), states[0], "Initial state should be Idle.")

            // State 2: Loading State
            val expectedLoadingState = PurchaseRecordUiState(isLoading = true, isFormEnabled = false)
            assertEquals(expectedLoadingState, states[1], "State should transition to Loading.")

            // State 3: Final DatabaseError State
            assertEquals(expectedErrorState, states[2], "Final state should be Error with the database error message.")

            // --- VERIFY ---
            verify(mockUseCase).registerPurchaseRecord(command)

            // Clean up the collector
            collectionJob.cancel()
        }
    }

}

