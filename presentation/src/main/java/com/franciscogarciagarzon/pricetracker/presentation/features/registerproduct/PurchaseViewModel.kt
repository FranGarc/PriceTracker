package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franciscogarciagarzon.commons.utils.Logger
import com.franciscogarciagarzon.commons.utils.contracts.DispatcherProvider
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegisterCommand
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationUseCase
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseValidationError
import com.franciscogarciagarzon.pricetracker.presentation.R
import com.franciscogarciagarzon.pricetracker.presentation.utils.StringResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    val registerPurchaseRecordUseCase: PurchaseRecordRegistrationUseCase,
    private val stringResourceProvider: StringResourceProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow<PurchaseRecordUiState>(PurchaseRecordUiState())
    val uiState: StateFlow<PurchaseRecordUiState> = _uiState.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = PurchaseRecordUiState()
    )

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

    private fun registerPurchase(intent: PurchaseIntent.RegisterNewPurchase) {
        Logger.d("PurchaseViewModel", "registerPurchase(intent: $intent)")

        // Immediately dispatch a system intent to update state to Loading
        handleIntent(PurchaseIntent.SetLoading) // Clear previous status
        _uiState.update { it.copy(isLoading = true, isFormEnabled = false) }

        viewModelScope.launch(dispatchers.io) {
            val command = PurchaseRecordRegisterCommand(
                name = intent.productName,
                quantityPurchased = intent.quantityPurchased,
                unitFormat = intent.unitFormat,
                price = intent.price,
                storeName = intent.storeName
            )

            val result = registerPurchaseRecordUseCase.registerPurchaseRecord(command)


            //The code behaves identically to standard Dispatchers.Main; it posts the task to the UI thread's queue to ensure thread safety.
            withContext(dispatchers.main) { // Switch to Main (immediate)
                when (result) {
                    is PurchaseRecordRegistrationResult.Success -> {
                        handleIntent(PurchaseIntent.PurchaseRegistrationSuccess)
                    }

                    is PurchaseRecordRegistrationResult.ValidationError -> {

                        val errorMessage = when (result.errorType) {
                            PurchaseValidationError.PRICE_INVALID_FORMAT ->
                                stringResourceProvider.getString(resId = R.string.PRICE_INVALID_FORMAT)

                            PurchaseValidationError.QUANTITY_INVALID_FORMAT ->
                                stringResourceProvider.getString(resId = R.string.QUANTITY_INVALID_FORMAT)

                            PurchaseValidationError.PRODUCT_NAME_EMPTY ->
                                stringResourceProvider.getString(resId = R.string.PRODUCT_NAME_EMPTY)

                            PurchaseValidationError.PRICE_IS_ZERO_OR_NEGATIVE ->
                                stringResourceProvider.getString(resId = R.string.PRICE_IS_ZERO_OR_NEGATIVE)

                            PurchaseValidationError.QUANTITY_IS_ZERO_OR_NEGATIVE ->
                                stringResourceProvider.getString(resId = R.string.QUANTITY_IS_ZERO_OR_NEGATIVE)

                            PurchaseValidationError.UNIT_EMPTY ->
                                stringResourceProvider.getString(resId = R.string.UNIT_EMPTY)
                        }

                        handleIntent(PurchaseIntent.PurchaseRegistrationError(errorMessage))
                    }

                    is PurchaseRecordRegistrationResult.DatabaseError -> {
                        // Provide a generic error message for the UI to display
                        handleIntent(PurchaseIntent.PurchaseRegistrationError("A database error occurred during registration."))
                    }
                }
            }

        }

    }
}