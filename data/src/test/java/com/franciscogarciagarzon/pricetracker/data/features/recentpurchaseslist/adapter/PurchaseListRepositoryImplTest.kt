package com.franciscogarciagarzon.pricetracker.data.features.recentpurchaseslist.adapter

import com.franciscogarciagarzon.pricetracker.data.database.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.database.model.PriceRecordWithDetails
import com.franciscogarciagarzon.pricetracker.data.mappers.PurchaseDataMapper
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@DisplayName("RecentPurchaseListRepository Unit Tests")
class PurchaseListRepositoryImplTest {

    private lateinit var priceRecordDao: PriceRecordDao
    private lateinit var mapper: PurchaseDataMapper
    private lateinit var repository: RecentPurchaseListRepositoryImpl

    @BeforeEach
    fun setup() {
        // Inicializamos mocks
        priceRecordDao = mock()
        mapper = mock()
        repository = RecentPurchaseListRepositoryImpl(priceRecordDao, mapper)
    }

    @Test
    @DisplayName("Should return the purchase list Flow correctly mapped")
    fun getAllPurchases_shouldReturnMappedFlow() = runTest {
        // ARRANGE
        val mockDto = mock(PriceRecordWithDetails::class.java)
        val mockDomain = mock(PurchaseRecord::class.java)

        // Simulamos que el DAO devuelve un Flow con una lista que tiene 1 DTO
        whenever(priceRecordDao.getRecentRecordsWithDetails()).thenReturn(flowOf(listOf(mockDto)))

        // Simulamos que el mapper transforma ese DTO en el objeto de dominio mockeado
        whenever(mapper.toDomain(any())).thenReturn(mockDomain)

        // ACT
        // Consumimos el Flow (la primera emisión)
        val result = repository.getRecentPurchases().first()

        // ASSERT
        assertEquals(1, result.size, "Resulting list should contain one element")
        assertEquals(mockDomain, result[0], "List item should be the mapped object")
    }
}