package com.franciscogarciagarzon.pricetracker.presentation.features.purchaselist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franciscogarciagarzon.pricetracker.domain.features.purchaselist.ports.incoming.GetRecentPurchasesPort
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PurchaseListViewModel @Inject constructor(
    @Suppress("UNUSED_PARAMETER") private val getRecentPurchases: GetRecentPurchasesPort
) : ViewModel() {

    val uiState: StateFlow<PurchaseListUiState> = getRecentPurchases()
        .map (::mapToUiState)//{ purchases -> mapToUiState(purchases) }
        .catch { e -> emit(mapError(e)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PurchaseListUiState.Loading
        )

    private fun mapToUiState(purchases: List<PurchaseRecord>): PurchaseListUiState {
        return if (purchases.isEmpty()) PurchaseListUiState.Empty
        else PurchaseListUiState.Success(purchases)
    }
    private fun mapError(e: Throwable): PurchaseListUiState =
        PurchaseListUiState.Error(e.message ?: "Unknown Error")


}