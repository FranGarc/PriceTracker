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

@HiltViewModel
class RecentPurchasesListViewModel @Inject constructor(
    @Suppress("UNUSED_PARAMETER") private val getRecentPurchases: GetRecentPurchasesPort,
    sharingStrategyProvider: SharingStrategyProvider
) : ViewModel() {

    val uiState: StateFlow<PurchaseListUiState> = getRecentPurchases()
        .map (::mapToUiState)
        .catch { e -> emit(mapError(e)) }
        .stateIn(
            scope = viewModelScope,
            started = sharingStrategyProvider.getStrategy(),
            initialValue = PurchaseListUiState.Loading
        )

    private fun mapToUiState(purchases: List<PurchaseRecord>): PurchaseListUiState {
        return if (purchases.isEmpty()) PurchaseListUiState.Empty
        else PurchaseListUiState.Success(purchases)
    }
    private fun mapError(e: Throwable): PurchaseListUiState =
        PurchaseListUiState.Error(e.message ?: "Unknown Error")
}