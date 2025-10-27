package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProductIdTest {
    @Test
    fun `test should generate valid id`() {
        // ARRANGE / ACT
        val productId = Id.generate()

        // ASSERT
        assert(productId.value.isNotBlank())
    }

    @Test
    fun `test should accept valid_uuid string`() {
        // ARRANGE
        val validUuid = "a1b2c3d4-e5f6-7g8h-9i0j-k1l2m3n4o5p6"

        // ACT
        val id = Id(validUuid)

        // ASSERT
        assert(validUuid == id.value)
    }

    @Test
    fun `test should throw exception when blank or empty`() {
        // ARRANGE - Casos de fallo: string vacío ("") y string en blanco (" ")
        val emptyString = ""
        val blankString = "   "

        // ASSERT: Verificar que el constructor lanza IllegalArgumentException para ambos casos

        // Caso 1: String vacío
        val emptyException = assertThrows<IllegalArgumentException> {
            Id(emptyString)
        }
        assert(emptyException.message?.contains("cannot be blank") == true)

        // Caso 2: String en blanco
        val blankException = assertThrows<IllegalArgumentException> {
            Id(blankString)
        }
        assert(blankException.message?.contains("cannot be blank") == true)
    }
}