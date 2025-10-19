package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases

import com.franciscogarciagarzon.pricetracker.domain.common.ResultWithValue
import com.franciscogarciagarzon.pricetracker.domain.common.getOrNull
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.Product
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.ProductRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.outgoing.repositories.ProductRepository
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.Price
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductId
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.ProductName
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.eq

class RegisterProductUseCaseTest {

    private lateinit var productRegistrationUseCase: ProductRegistrationPort
    private lateinit var mockProductRepository: ProductRepository

    @BeforeEach
    fun setUp() {
        mockProductRepository = mock(ProductRepository::class.java)

        productRegistrationUseCase = ProductRegistrationUseCase(
            productRepository = mockProductRepository
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
        // | productName | unitFormat | price | store              |
        "Leche, 1L, 1.20, Mercamona",
        "Huevos L, 12, 2.40, Frutería Las nenas",
        "Carne picada de ternera, 400g, 3.54, Carnicería Puri",
        "Harina de Repostería, 1Kg, 1.10, Carreflus"
    )
    fun `should register product and return the stored entity`(
        productName: String,
        unitFormat: String,
        price: Double,
        storeName: String // Los argumentos del metodo deben coincidir con las columnas de CsvSource
    ) {
        runTest {
            val inputCommand = RegisterProductCommand(
                name = productName,
                unitFormat = unitFormat,
                price = price,
                storeName = storeName,
            )
            val expectedProduct = Product(
                name = ProductName(productName),
                unitFormat = unitFormat,
                storeName = storeName,
                price = Price(price),
                id = ProductId("id")
            )
            val expectedResult = ProductRegistrationResult.Success(expectedProduct)
            `when`(
                mockProductRepository.registerProduct(
                    inputCommand.name,
                    inputCommand.unitFormat,
                    inputCommand.price,
                    inputCommand.storeName,
                )
            ).thenReturn(expectedResult)


            val result: ResultWithValue<Product> = productRegistrationUseCase.registerProduct(inputCommand)
            val product = result.getOrNull()
            assert(product != null)
            assert(product?.storeName.equals(expectedProduct.storeName))
        }
    }

    @ParameterizedTest(name = "Fallo de DB para {0} ({1}) debe devolver DatabaseError")
    @CsvSource(
        "Leche, 1L, 1.20, Mercamona",
        "Huevos L, 12, 2.40, Frutería Las nenas",
        "Carne picada de ternera, 400g, 3.54, Carnicería Puri",
        "Harina de Repostería, 1Kg, 1.10, Carreflus"
    )
    fun `should return DatabaseError when repository fails to save`(
        productName: String,
        unitFormat: String,
        price: Double,
        storeName: String
    ) {
        runTest {
            val inputCommand = RegisterProductCommand(
                name = productName,
                unitFormat = unitFormat,
                price = price,
                storeName = storeName,
            )


            val expectedResult = ProductRegistrationResult.DatabaseError

            // Utilizamos 'any()' de Mockito-Kotlin para simplificar la configuración
            // del mock, indicando que debe devolver el error sin importar el comando exacto.
            `when`(mockProductRepository.registerProduct(anyString(), anyString(), anyDouble(), anyString())).thenReturn(expectedResult)

            // ACT
            val result = productRegistrationUseCase.registerProduct(inputCommand)

            // ASSERT: Verificar que el resultado es el tipo de error esperado.
            assert(result is ProductRegistrationResult.DatabaseError)
            assert(result.getOrNull() == null)
        }
    }

    @ParameterizedTest(name = "Falta de un campo para {0} ({1}) debe devolver ValidationError")
    @CsvSource(
        " ' ', 1L, 1.20, Mercamona",
    )
    fun `should return ValidationError when productName is empty or blank`(
        productName: String,
        unitFormat: String,
        price: Double,
        storeName: String
    ) {
        runTest {
            val inputCommand = RegisterProductCommand(
                name = productName,
                unitFormat = unitFormat,
                price = price,
                storeName = storeName,
            )


            val expectedResult = ProductRegistrationResult.ValidationError("error message")

            `when`(mockProductRepository.registerProduct(eq(""), anyString(), anyDouble(), anyString())).thenReturn(expectedResult)

            // ACT
            val result = productRegistrationUseCase.registerProduct(inputCommand)

            // ASSERT: Verificar que el resultado es el tipo de error esperado.
            assert(result is ProductRegistrationResult.ValidationError)
            assert(result.getOrNull() == null)
        }
    }
}