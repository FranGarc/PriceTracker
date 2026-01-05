package com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franciscogarciagarzon.commons.utils.contracts.SharingStrategyProvider
import com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.incoming.GetRecentPurchasesPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel reactivo para la visualización de la lista de compras recientes.
 * Implementa un flujo de datos unidireccional puramente observacional.
 * No requiere acciones del usuario, ya que se sincroniza automáticamente con la
 * "Fuente Única de Verdad" (la base de datos) a través de un Flow.
 * @property uiState Flujo de estado que representa los datos, carga o errores de la lista.
 */
@HiltViewModel
class RecentPurchasesListViewModel @Inject constructor(
    @Suppress("UNUSED_PARAMETER") private val getRecentPurchases: GetRecentPurchasesPort,
    sharingStrategyProvider: SharingStrategyProvider
) : ViewModel() {

    // Se utiliza stateIn para convertir el Flow de dominio en un estado
    // persistente que sobrevive a cambios de configuración de la UI.
    val uiState: StateFlow<PurchaseListUiState> = getRecentPurchases()
        .map (::mapToUiState)
        .catch { e -> emit(mapError(e)) }
        .stateIn(
            scope = viewModelScope,
            started = sharingStrategyProvider.getStrategy(),
            initialValue = PurchaseListUiState.Loading
        )

    /**
     * Determina el estado visual basado en la presencia de datos.
     */
    private fun mapToUiState(purchases: List<PurchaseRecord>): PurchaseListUiState {
        return if (purchases.isEmpty()) PurchaseListUiState.Empty
        else PurchaseListUiState.Success(purchases)
    }

    /**
     * Función de seguridad contra errores no controlados.
     * Al capturar Throwable y transformarlo en un estado de UI se evita el crash
     */
    private fun mapError(e: Throwable): PurchaseListUiState =
        PurchaseListUiState.Error(e.message ?: "Unknown Error")
}