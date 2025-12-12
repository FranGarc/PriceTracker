package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.franciscogarciagarzon.commons.utils.Logger
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.franciscogarciagarzon.pricetracker.presentation.R
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.composables.PurchaseRegistrationForm
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.composables.PurchaseRegistrationFormState
import com.franciscogarciagarzon.pricetracker.presentation.isEmpty
import com.franciscogarciagarzon.pricetracker.presentation.ui.common.composables.ErrorMessageDisplay
import com.franciscogarciagarzon.pricetracker.presentation.ui.common.composables.LoadingIndicator
import com.franciscogarciagarzon.pricetracker.presentation.ui.common.composables.SuccessMessageDisplay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseRegistrationScreen(
    viewModel: PurchaseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var formState by remember { mutableStateOf(PurchaseRegistrationFormState()) }

    // Logic to submit the form
    val onRegister: () -> Unit = {

        viewModel.handleIntent(
            PurchaseIntent.RegisterNewPurchase(
                productName = formState.productName.trim(),
                quantityPurchased = formState.quantityPurchased.trim(),
                unitFormat = formState.unitFormat,
                price = formState.price.trim(),
                storeName = formState.storeName.trim()
            )
        )
    }
    val currentFormState by rememberUpdatedState(formState)
    val onProductNameChange: (String) -> Unit = remember { { newProductName -> formState = currentFormState.copy(productName = newProductName) } }
    val onQuantityChange: (String) -> Unit = remember { { newQuantity -> formState = currentFormState.copy(quantityPurchased = newQuantity) } }
    val onUnitFormatChange: (String) -> Unit = remember { { newUnit -> formState = currentFormState.copy(unitFormat = UnitFormat.valueOf(newUnit)) } }
    val onPriceChange: (String) -> Unit = remember { { newPrice -> formState = currentFormState.copy(price = newPrice) } }
    val onStoreNameChange: (String) -> Unit = remember { { newStore -> formState = currentFormState.copy(storeName = newStore) } }


    // Logic to reset the form and the state after success
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage.isEmpty().not()) {
            // Reset form fields
            formState = PurchaseRegistrationFormState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.purchaseRegistrationScreen_appbar_title)) },
                navigationIcon = {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = remember { SnackbarHostState() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            LoadingIndicator(statusState = uiState)
            ErrorMessageDisplay(statusState = uiState, onDismiss = { viewModel.handleIntent(PurchaseIntent.ClearStatus) })
            SuccessMessageDisplay(statusState = uiState, onDismiss = { viewModel.handleIntent(PurchaseIntent.ClearStatus) })

            Logger.d("PurchaseRegistrationScreen", "PurchaseRegistrationForm called with uiState: $uiState")
            PurchaseRegistrationForm(
                padding = padding,
                isLoading = uiState.isLoading,
                isFormEnabled = uiState.isFormEnabled,
                formState = { formState },
                onProductNameChange = onProductNameChange,
                onQuantityChange = onQuantityChange,
                onUnitFormatChange = onUnitFormatChange,
                onPriceChange = onPriceChange,
                onStoreNameChange = onStoreNameChange,

                onRegister = onRegister
            )
        }


    }
}