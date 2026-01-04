package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases

import com.franciscogarciagarzon.pricetracker.domain.common.ResultWithValue
import com.franciscogarciagarzon.pricetracker.domain.common.getOrNull
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.PurchaseRecordRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories.PurchaseRepository
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

class RegisterPurchaseRecordUseCaseTest {

    private lateinit var purchaseItemRegistrationUseCase: PurchaseRecordRegistrationPort
    private lateinit var mockPurchaseRepository: PurchaseRepository


    @BeforeEach
    fun setUp() {
        mockPurchaseRepository = mock(PurchaseRepository::class.java)

        purchaseItemRegistrationUseCase = PurchaseRecordRegistrationUseCase(
            productRepository = mockPurchaseRepository
        )
    }

    @AfterEach
    fun tearDown() {

    }

    // Usamos @ParameterizedTest en lugar de @Test
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
    fun `should register product and return the stored entity`(
        productName: String,
        quantityPurchased: String,
        unitFormat: String,
        price: String,
        storeName: String // Los argumentos del metodo deben coincidir con las columnas de CsvSource
    ) {
        runTest {
            val inputCommand = PurchaseRecordRegisterCommand(
                name = productName,
                quantityPurchased = quantityPurchased,
                unitFormat = UnitFormat.valueOf(unitFormat),
                price = price,
                storeName = storeName,
            )
            val expectedPurchaseRecord = PurchaseRecord(
                name = ProductName(productName),
                amount = QuantityPurchased(quantityPurchased.toDouble()),
                storeName = storeName,
                price = Price(price.toDouble()),
                purchaseDate = System.currentTimeMillis(),
                unitFormat = UnitFormat.UNIT,
            )
            val expectedResult = PurchaseRecordRegistrationResult.Success(expectedPurchaseRecord)
            `when`(
                mockPurchaseRepository.registerPurchaseRecord(
                    anyString(),
                    anyDouble(),
                    any(),
                    anyDouble(),
                    anyString()
                )
            ).thenReturn(expectedResult)


            val result: ResultWithValue<PurchaseRecord> = purchaseItemRegistrationUseCase.registerPurchaseRecord(inputCommand)
            val product = result.getOrNull()
            assert(product != null)
            assert(product?.storeName.equals(expectedPurchaseRecord.storeName))
            // Now, verify it was called with the *specific* correct values
            verify(mockPurchaseRepository).registerPurchaseRecord(
                inputCommand.name,
                inputCommand.quantityPurchased.toDouble(),
                inputCommand.unitFormat,
                inputCommand.price.toDouble(),
                inputCommand.storeName
            )
        }
    }

    @ParameterizedTest(name = "Fallo de DB para {0} ({1}) debe devolver DatabaseError")
    @CsvSource(
        // | productName |amount | unitFormat | price | store              |
        "Leche, 1,LITER, 1.20, Mercamona",
        "Huevos LITER, 12,UNIT, 2.40, Frutería Las nenas",
        "Carne picada de ternera, 400, GRAM, 3.54, Carnicería Puri",
        "Harina de Repostería, 1, KILOGRAM, 1.10, Carreflus"
    )
    fun `should return DatabaseError when repository fails to save`(
        productName: String,
        quantityPurchased: String,
        unitFormat: String,
        price: String,
        storeName: String
    ) {
        runTest {
            val inputCommand = PurchaseRecordRegisterCommand(
                name = productName,
                quantityPurchased = quantityPurchased,
                unitFormat = UnitFormat.valueOf(unitFormat),
                price = price,
                storeName = storeName,
            )


            val expectedResult = PurchaseRecordRegistrationResult.DatabaseError

            // Utilizamos 'any()' de Mockito-Kotlin para simplificar la configuración
            // del mock, indicando que debe devolver el error sin importar el comando exacto.
            `when`(
                mockPurchaseRepository.registerPurchaseRecord(
                    name = anyString(),
                    quantityPurchased = anyDouble(),
                    unitFormat = any(),
                    price = anyDouble(),
                    storeName = anyString()
                )
            ).thenReturn(expectedResult)

            // ACT
            val result = purchaseItemRegistrationUseCase.registerPurchaseRecord(inputCommand)

            // ASSERT: Verificar que el resultado es el tipo de error esperado.
            assert(result is PurchaseRecordRegistrationResult.DatabaseError)
            assert(result.getOrNull() == null)
        }
    }

    @ParameterizedTest(name = "Falta de un campo,  debe devolver ValidationError")
    @CsvSource(
        " , 1,LITER, 1.20, Mercamona", // name is null
        " ' ', 1,LITER, 1.20, Mercamona", // blank name
        " Leche, ,LITER, 1.20, Mercamona", // amount is null
        " Leche, 1,LITER, , Mercamona", // price is null
        // it is allowed to have a blank store
    )
    fun `should return ValidationError when a parameter is empty or blank`(
        productName: String?,
        quantityPurchased: String?,
        unitFormat: String,
        price: String?,
        storeName: String?
    ) {
        runTest {
            // --- ARRANGE ---
            val inputCommand = PurchaseRecordRegisterCommand(
                name = productName ?: "",
                quantityPurchased = quantityPurchased ?: "",
                unitFormat = UnitFormat.valueOf(unitFormat),
                price = price ?: "",
                storeName = storeName ?: "",
            )
            // ACT
            val result = purchaseItemRegistrationUseCase.registerPurchaseRecord(inputCommand)

            // ASSERT
            // 1. Asegura que el resultado siempre sea  ValidationError
            assert(result is PurchaseRecordRegistrationResult.ValidationError)
            assert(result.getOrNull() == null)

            val errorResult = result as PurchaseRecordRegistrationResult.ValidationError
            // 2. Comprobar que el tipo de error específico de cada caso inválido case con la lógica del UseCase.
            when {
                productName.isNullOrBlank() -> assertEquals(PurchaseValidationError.PRODUCT_NAME_EMPTY, errorResult.errorType)
                quantityPurchased.isNullOrBlank() || quantityPurchased.toDoubleOrNull() == null -> assertEquals(PurchaseValidationError.QUANTITY_INVALID_FORMAT, errorResult.errorType)
                price.isNullOrBlank() || price.toDoubleOrNull() == null -> assertEquals(PurchaseValidationError.PRICE_INVALID_FORMAT, errorResult.errorType)
                quantityPurchased.toDouble() <= 0.0 -> assertEquals(PurchaseValidationError.QUANTITY_IS_ZERO_OR_NEGATIVE, errorResult.errorType)
                price.toDouble() < 0.0 -> assertEquals(PurchaseValidationError.PRICE_IS_ZERO_OR_NEGATIVE, errorResult.errorType)
            }

            // Verificar que NUNCA se llama al  repository.
            // La validación del UseCase debería detener la ejecución antes de tocar la capa data
            verify(mockPurchaseRepository, never()).registerPurchaseRecord(
                name = any<String>(),
                quantityPurchased = any<Double>(),
                unitFormat = any(),
                price = any<Double>(),
                storeName = any<String>(),
            )
        }
    }
    @Test
    fun `should return DatabaseError when repository throws an unexpected exception`() = runTest {
        val inputCommand = PurchaseRecordRegisterCommand("Leche", "1.0", UnitFormat.LITER, "1.20", "Mercamona")


        `when`(mockPurchaseRepository.registerPurchaseRecord(any(), any(), any(), any(), any()))
            .thenThrow(RuntimeException("Fatal DB Error"))

        val result = purchaseItemRegistrationUseCase.registerPurchaseRecord(inputCommand)

        assert(result is PurchaseRecordRegistrationResult.DatabaseError)
    }
}