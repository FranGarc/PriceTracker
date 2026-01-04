package com.franciscogarciagarzon.pricetracker.data.mapper

import com.franciscogarciagarzon.pricetracker.data.database.entity.PriceRecordEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.ProductEntity
import com.franciscogarciagarzon.pricetracker.data.database.entity.StoreEntity
import com.franciscogarciagarzon.pricetracker.data.database.model.PriceRecordWithDetails
import com.franciscogarciagarzon.pricetracker.data.mappers.PurchaseDataMapper
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PurchaseDataMapperTest {

    private lateinit var mapper: PurchaseDataMapper

    @BeforeEach
    fun setup() {
        mapper = PurchaseDataMapper()
    }

    @Test
    @DisplayName("Debería mapear PriceRecordWithDetails (DTO) correctamente a PurchaseRecord (Domain)")
    fun shouldMapDtoToDomainCorrectly() {
        // GIVEN
        val product = ProductEntity(dbId = 1L, name = "Milk", unitFormat = UnitFormat.LITER)
        val store = StoreEntity(dbId = 1L, name = "SuperMarket")
        val priceRecord = PriceRecordEntity(
            dbId = 1L,
            productId = 1L,
            storeId = 1L,
            quantityPurchased = 2.0,
            price = 1.50,
            purchaseDate = 123456789L,
            unitFormat = UnitFormat.LITER
        )
        val dto = PriceRecordWithDetails(priceRecord = priceRecord, product = product, store = store)

        // WHEN
        val result = mapper.toDomain(dto)

        // THEN
        assertThat(result.name.value).isEqualTo("Milk")
        assertThat(result.storeName).isEqualTo("SuperMarket")
        assertThat(result.price.value).isEqualTo(1.50)
        assertThat(result.amount.value).isEqualTo(2.0)
        assertThat(result.purchaseDate).isEqualTo(123456789L)
        assertThat(result.unitFormat).isEqualTo(UnitFormat.LITER)
    }

    @Test
    @DisplayName("Debería mapear entidades individuales correctamente a PurchaseRecord (Domain)")
    fun shouldMapEntitiesToDomainCorrectly() {
        // GIVEN
        val product = ProductEntity(dbId = 10L, name = "Bread", unitFormat = UnitFormat.UNIT)
        val store = StoreEntity(dbId = 20L, name = "Bakery")
        val priceRecord = PriceRecordEntity(
            dbId = 30L,
            productId = 10L,
            storeId = 20L,
            quantityPurchased = 1.0,
            price = 0.85,
            purchaseDate = 987654321L,
            unitFormat = UnitFormat.UNIT
        )

        // WHEN
        val result = mapper.toDomainFromEntities(priceRecord, product, store)

        // THEN
        assertThat(result.name.value).isEqualTo("Bread")
        assertThat(result.storeName).isEqualTo("Bakery")
        assertThat(result.price.value).isEqualTo(0.85)
        assertThat(result.purchaseDate).isEqualTo(987654321L)
    }
}