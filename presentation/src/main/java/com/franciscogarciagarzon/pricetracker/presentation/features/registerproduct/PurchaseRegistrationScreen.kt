package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

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
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


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
                unitFormat = formState.unitFormat.trim(),
                price = formState.price.trim(),
                storeName = formState.storeName.trim()
            )
        )
    }

    // Logic to reset the form and the state after success
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            // Reset form fields
            formState = PurchaseRegistrationFormState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register Purchase") },
                navigationIcon = {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Purchase Icon")
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = remember { SnackbarHostState() }) }
    ) { padding ->

        PurchaseRegistrationForm(
            padding = padding,
            uiState = uiState,
            onClearIntent = { viewModel.handleIntent(PurchaseIntent.ClearStatus) },
            formState = formState,
            onFormStateChanged = { newState ->
                formState = newState
            },
            onRegister = onRegister
        )

    }
}