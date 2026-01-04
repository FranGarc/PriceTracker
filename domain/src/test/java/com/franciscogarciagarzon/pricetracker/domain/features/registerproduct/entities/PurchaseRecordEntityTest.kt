package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows

class PurchaseRecordEntityTest {
    @Test
    fun `verify data class integrity and coverage`() {
        // 1. Creamos una instancia válida
        val record = PurchaseRecord(
            name = ProductName("Leche"),
            amount = QuantityPurchased(1.0),
            unitFormat = UnitFormat.LITER,
            price = Price(1.20),
            storeName = "Mercamona",
            purchaseDate = 123456789L
        )




        // 2. Accedemos explícitamente a cada propiedad.

        val (@Suppress("UNUSED_PARAMETER")name,
            @Suppress("UNUSED_PARAMETER")amount,
            @Suppress("UNUSED_PARAMETER")unit,
            @Suppress("UNUSED_PARAMETER")price,
            @Suppress("UNUSED_PARAMETER")store,
            @Suppress("UNUSED_PARAMETER")date) = record
        assertNotNull(record.name)
        assertNotNull(record.amount)
        assertNotNull(record.unitFormat)
        assertNotNull(record.price)
        assertNotNull(record.storeName)

        assertNotNull(record.toString())
        assertNotNull(record.hashCode())

        // 3. Forzar copia e igualdad
        val clone = record.copy()
        assertEquals(record, clone)
        assertNotSame(record, clone)

        val different = record.copy(storeName = "Another Store")
        assertNotEquals(record, different)
    }
    @Test
    fun `ProductName should throw exception when blank`() {
        val exception = assertThrows<IllegalArgumentException> {
            ProductName("") // O "   "
        }
        assertEquals("Product name must be between 1 and 100 characters", exception.message)
    }

    @Test
    fun `Price should throw exception when negative`() {
        val exception = assertThrows<IllegalArgumentException> {
            Price(-1.0)
        }
        assertEquals("Price cannot be negative", exception.message)
    }

    @Test
    fun `QuantityPurchased should throw exception when zero or negative`() {
        // Probamos el límite (0)
        val exZero = assertThrows<IllegalArgumentException> {
            QuantityPurchased(0.0)
        }
        assertEquals("QuantityPurchased cannot be negative nor zero", exZero.message)
        // Probamos negativo
        assertThrows<IllegalArgumentException> {
            QuantityPurchased(-5.0)
        }
    }
}