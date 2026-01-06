package com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.usecases

import com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.incoming.GetRecentPurchasesPort
import com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.outgoing.RecentPurchaseListRepository
import com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.usecases.GetRecentPurchasesUseCase
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

class GetRecentPurchasesUseCaseTest {

    private lateinit var getRecentPurchasesUseCase: GetRecentPurchasesPort
    private lateinit var mockRecentPurchaseListRepository: RecentPurchaseListRepository

    @BeforeEach
    fun setUp() {
        mockRecentPurchaseListRepository = mock(RecentPurchaseListRepository::class.java)

        getRecentPurchasesUseCase = GetRecentPurchasesUseCase(
            repository = mockRecentPurchaseListRepository
        )
    }

    @AfterEach
    fun tearDown() {

    }

    @Suppress("UNUSED_VARIABLE", "UNUSED_Expression")
    @ParameterizedTest(name = "Registro de {0} ({1}) debe ser exitoso")
    // @CsvFileSource(files = arrayOf<String>()) // si se quiere probar con ficheros CSV
    // Usamos @CsvSource para pasar las filas del ejemplo BDD como argumentos
    @CsvSource(
        // | productName |QuantityPurchased | unitFormat | price | store              |
        "Leche, 1,LITER, 1.20, Mercamona",
        "Huevos LITER, 12,UNIT, 2.40, Frutería Las nenas",
        "Carne picada de ternera, 400, GRAM, 3.54, Carnicería Puri",
        "Harina de Repostería, 1, KILOGRAM, 1.10, Carreflus"
    )fun `should register product it should fetch purchases from repository`(
        @Suppress("UNUSED_PARAMETER")productName: String,
        @Suppress("UNUSED_PARAMETER")quantityPurchased: String,
        @Suppress("UNUSED_PARAMETER")unitFormat: String,
        @Suppress("UNUSED_PARAMETER")price: String,
        @Suppress("UNUSED_PARAMETER")storeName: String // Los argumentos del metodo deben coincidir con las columnas de CsvSource
    ){
        runTest {
            // GIVEN
            val mockList = listOf(mock(PurchaseRecord::class.java))
            `when`(mockRecentPurchaseListRepository.getRecentPurchases()).thenReturn(flowOf(mockList))

            // ACT
            val result = getRecentPurchasesUseCase.invoke().first()

            // ASSERT
            assertEquals(mockList, result)
            verify(mockRecentPurchaseListRepository).getRecentPurchases().let {  }
        }
    }

}