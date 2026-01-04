package com.franciscogarciagarzon.pricetracker.presentation.features.purchaselist

import com.franciscogarciagarzon.pricetracker.domain.features.purchaselist.ports.incoming.GetRecentPurchasesPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class PurchaseListViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    @Mock private lateinit var getRecentPurchasesPort: GetRecentPurchasesPort
    private lateinit var viewModel: PurchaseListViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getRecentPurchasesPort = mock()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("Initial state is Loading")
    fun `Initial state is loading`() = runTest {
        // GIVEN: Un flujo que no emite nada inmediatamente (o tarda)
        whenever(getRecentPurchasesPort.invoke()).thenReturn(emptyFlow())

        // WHEN: Instanciamos
        viewModel = PurchaseListViewModel(getRecentPurchasesPort)

        // THEN: El valor inmediato debe ser Loading
        val state = viewModel.uiState.value
        assertIs<PurchaseListUiState.Loading>(state)
    }


    @Test
    fun `when use case emits list, uiState becomes Success`() = runTest {
        // GIVEN
        val mockPurchases = listOf(createMockPurchase())
        whenever(getRecentPurchasesPort.invoke()).thenReturn(flowOf(mockPurchases))

        // WHEN
        viewModel = PurchaseListViewModel(getRecentPurchasesPort)

        // THEN: Creamos un job que mantiene vivo el flujo durante el test entero
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { /* Obligamos a ejecutar map/catch */ }
        }

        val state = viewModel.uiState.value
        assertIs<PurchaseListUiState.Success>(state)
        Assertions.assertEquals("Leche", state.purchases[0].name.value)

        job.cancel()
    }

    @Test
    @DisplayName("When use case fails, uiState becomes Error")
    fun `when use case fails uiState becomes Error`() = runTest {
        // 1. GIVEN: El puerto está preparado para fallar
        val errorMessage = "Database connection failed"
        whenever(getRecentPurchasesPort.invoke()).thenReturn(kotlinx.coroutines.flow.flow {
            throw RuntimeException(errorMessage)
        })

        // 2. WHEN: Instanciamos el ViewModel justo ahora
        viewModel = PurchaseListViewModel(getRecentPurchasesPort)

        // 3. THEN: Recolectamos y verificamos
        val results = mutableListOf<PurchaseListUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { results.add(it) }
        }

        advanceUntilIdle()

        // 4. Verificaciones
        // Debería haber pasado por Loading y terminado en Error
        val errorState = results.last()
        assertIs<PurchaseListUiState.Error>(errorState)
        Assertions.assertEquals(errorMessage, errorState.message)

        job.cancel()
    }

    @Test
    @DisplayName("When list is empty, uiState becomes Empty")
    fun `when list is empty uiState becomes Empty`() = runTest {
        // Forzamos lista vacía
        whenever(getRecentPurchasesPort.invoke()).thenReturn(flowOf(emptyList()))

        viewModel = PurchaseListViewModel(getRecentPurchasesPort)

        val results = mutableListOf<PurchaseListUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { results.add(it) }
        }

        advanceUntilIdle()

        assertIs<PurchaseListUiState.Empty>(results.last())
        job.cancel()
    }

    @Test
    @DisplayName("When use case fails with null message, uiState becomes Error with default message")
    fun `when use case fails with null message uiState becomes Error with default message`() = runTest {
        // GIVEN: Una excepción sin mensaje (null)
        whenever(getRecentPurchasesPort.invoke()).thenReturn(kotlinx.coroutines.flow.flow {
            throw RuntimeException() // message será null
        })

        viewModel = PurchaseListViewModel(getRecentPurchasesPort)

        val results = mutableListOf<PurchaseListUiState>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { results.add(it) }
        }

        advanceUntilIdle()

        // THEN: Debe usar "Unknown Error"
        val errorState = results.last()
        assertIs<PurchaseListUiState.Error>(errorState)
        Assertions.assertEquals("Unknown Error", errorState.message)

        job.cancel()
    }



    // Función auxiliar para crear registros rápidamente
    private fun createMockPurchase() = PurchaseRecord(
        name = ProductName("Leche"),
        unitFormat = UnitFormat.LITER,
        storeName = "Test Store",
        amount = QuantityPurchased(1.0),
        price = Price(1.0),
        purchaseDate = System.currentTimeMillis()
    )

}