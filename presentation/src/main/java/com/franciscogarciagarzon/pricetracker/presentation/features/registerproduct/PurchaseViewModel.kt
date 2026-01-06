package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franciscogarciagarzon.commons.utils.Logger
import com.franciscogarciagarzon.commons.utils.contracts.DispatcherProvider
import com.franciscogarciagarzon.commons.utils.contracts.SharingStrategyProvider
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming.PurchaseRecordRegistrationPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegisterCommand
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.uiModel.PurchaseRecordRegistrationResultUiModel
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.uiModel.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel encargado de la gestión de registros de compra.
 * Implementa una arquitectura basada en estados e intenciones (MVI).
 * Actúa como mediador entre la UI de registro y el caso de uso de dominio.
 * Se inyectan proveedores de Dispatchers y Estrategias de Compartido
 * para garantizar la testabilidad unitaria sin dependencias de hilos reales.
 */
@HiltViewModel
open class PurchaseViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    val registerPurchaseRecordUseCase: PurchaseRecordRegistrationPort,
    sharingStrategyProvider: SharingStrategyProvider
) : ViewModel() {
    /**
     * Estado único de la UI.
     * Al usar StateFlow, aseguramos que la UI siempre tenga
     * acceso al último estado válido, incluso tras cambios de configuración.
     */
    private val _uiState = MutableStateFlow<PurchaseRecordUiState>(PurchaseRecordUiState())
    val uiState: StateFlow<PurchaseRecordUiState> = _uiState.asStateFlow().stateIn(
        scope = viewModelScope,
        started = sharingStrategyProvider.getStrategy(),
        initialValue = PurchaseRecordUiState()
    )

    /**
     * Punto de entrada para todas las acciones del usuario.
     * Implementar un 'reducer' permite que
     * el estado de la UI sea predecible y fácil de depurar.
     */
    fun handleIntent(intent: PurchaseIntent) {
        Logger.d("PurchaseViewModel", "handleIntent($intent)")
        when (intent) {
            is PurchaseIntent.RegisterNewPurchase -> {
                registerPurchase(intent)
            }

            is PurchaseIntent.ClearStatus -> _uiState.update {
                reducer(it, intent)
            }

            is PurchaseIntent.PurchaseRegistrationSuccess -> {
                _uiState.update { reducer(it, intent) }
            }

            is PurchaseIntent.PurchaseRegistrationError -> {
                _uiState.update { reducer(it, intent) }
            }

            PurchaseIntent.SetLoading -> {
                _uiState.update { reducer(it, intent) }
            }
        }
    }

    /**
     * Ejecución de la lógica de negocio.
     * Transforma los datos de la UI en un 'Command' de dominio y
     * procesa el resultado mapeándolo a un modelo de presentación.
     */
    private fun registerPurchase(intent: PurchaseIntent.RegisterNewPurchase) {
        Logger.d("PurchaseViewModel", "registerPurchase(intent: $intent)")

        handleIntent(PurchaseIntent.SetLoading)

        viewModelScope.launch(dispatchers.io) {

            val command = PurchaseRecordRegisterCommand(
                name = intent.productName,
                quantityPurchased = intent.quantityPurchased,
                unitFormat = intent.unitFormat,
                price = intent.price,
                storeName = intent.storeName
            )

            val result = registerPurchaseRecordUseCase.registerPurchaseRecord(command).toPresentation()

            withContext(dispatchers.main) { // Switch to Main (immediate)
                when (result) {
                    is PurchaseRecordRegistrationResultUiModel.Success -> {
                        handleIntent(PurchaseIntent.PurchaseRegistrationSuccess(result.message))
                    }

                    is PurchaseRecordRegistrationResultUiModel.Error -> {
                        handleIntent(PurchaseIntent.PurchaseRegistrationError(result.message))
                    }
                }
            }

        }

    }
}