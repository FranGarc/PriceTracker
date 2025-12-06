package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases

import com.franciscogarciagarzon.pricetracker.domain.common.ResultWithValue
import com.franciscogarciagarzon.pricetracker.domain.common.getOrNull
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.PurchaseRecordRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories.PurchaseRepository
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.QuantityPurchased
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
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

    companion object {
        const val INVALID_PRICE_VALUE  = "-1.0"
        const val INVALID_AMOUNT_VALUE = "0.0"
    }

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
        "Leche, 1,L, 1.20, Mercamona",
        "Huevos L, 12,unidad, 2.40, Frutería Las nenas",
        "Carne picada de ternera, 400, g, 3.54, Carnicería Puri",
        "Harina de Repostería, 1, Kg, 1.10, Carreflus"
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
                unitFormat = unitFormat,
                price = price,
                storeName = storeName,
            )
            val expectedPurchaseRecord = PurchaseRecord(
                name = ProductName(productName),
                amount = QuantityPurchased(quantityPurchased.toDouble()),
                unitFormat = unitFormat,
                storeName = storeName,
                price = Price(price.toDouble()),
                purchaseDate = System.currentTimeMillis()
            )
            val expectedResult = PurchaseRecordRegistrationResult.Success(expectedPurchaseRecord)
            `when`(
                mockPurchaseRepository.registerPurchaseRecord(
                    anyString(),
                    anyDouble(),
                    anyString(),
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
        "Leche, 1,L, 1.20, Mercamona",
        "Huevos L, 12,unidad, 2.40, Frutería Las nenas",
        "Carne picada de ternera, 400, g, 3.54, Carnicería Puri",
        "Harina de Repostería, 1, Kg, 1.10, Carreflus"
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
                unitFormat = unitFormat,
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
                    unitFormat = anyString(),
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
        " , 1,L, 1.20, Mercamona", // name is null
        " ' ', 1,L, 1.20, Mercamona", // blank name
        " Leche, ,L, 1.20, Mercamona", // amount is null
        " Leche, 1,L, , Mercamona", // price is null
        // it is allowed to have a blank store
    )
    fun `should return ValidationError when a parameter is empty or blank`(
        productName: String?,
        amount: String?,
        unitFormat: String?,
        price: String?,
        storeName: String?
    ) {
        runTest {
            val inputCommand = PurchaseRecordRegisterCommand(
                name = productName ?: "",
                quantityPurchased = amount ?: INVALID_AMOUNT_VALUE,
                unitFormat = unitFormat ?: "",
                price = price ?: INVALID_PRICE_VALUE,
                storeName = storeName ?: "",
            )
            // ACT
            val result = purchaseItemRegistrationUseCase.registerPurchaseRecord(inputCommand)

            // ASSERT: Verificar que el resultado es el tipo de error esperado.
            assert(result is PurchaseRecordRegistrationResult.ValidationError)
            assert(result.getOrNull() == null)

            val errorResult = result as PurchaseRecordRegistrationResult.ValidationError

            if (productName.isNullOrBlank()) {
                assert(PurchaseValidationError.PRODUCT_NAME_EMPTY == errorResult.errorType)
            } else if (unitFormat.isNullOrBlank()) {
                assert(PurchaseValidationError.UNIT_EMPTY == errorResult.errorType)
            } else if (amount == null || amount.toDoubleOrNull() == null) {
                assert(PurchaseValidationError.QUANTITY_INVALID_FORMAT == errorResult.errorType)
            } else if (price == null || price.toDoubleOrNull() == null) {
                assert(PurchaseValidationError.PRICE_INVALID_FORMAT == errorResult.errorType)
            } else if (amount.toDouble() <= 0.0) { // Check for <= 0, not < 1
                assert(PurchaseValidationError.QUANTITY_IS_ZERO_OR_NEGATIVE == errorResult.errorType)
            } else if (price.toDouble() < 0.0) {
                assert(PurchaseValidationError.PRICE_IS_ZERO_OR_NEGATIVE == errorResult.errorType)
            }

            // Verify that the repository was NEVER called.
            // The Use Case's validation should have stopped execution before touching the Data Layer
            verify(mockPurchaseRepository, never()).registerPurchaseRecord(
                name = any<String>(),
                quantityPurchased = any<Double>(),
                unitFormat = any<String>(),
                price = any<Double>(),
                storeName = any<String>(),
            )
        }
    }

}