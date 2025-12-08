package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage
import com.franciscogarciagarzon.pricetracker.presentation.isEmpty

data class PurchaseRegistrationFormState(
    val productName: String = "",
    val quantityPurchased: String = "",
    val unitFormat: String = "",
    val price: String = "",
    val storeName: String = "",
)

@Composable
fun PurchaseRegistrationForm(
    padding: PaddingValues,
    uiState: PurchaseRecordUiState,
    onClearIntent: () -> Unit,
    formState: PurchaseRegistrationFormState,
    onFormStateChanged: (PurchaseRegistrationFormState) -> Unit,
    onRegister: () -> Unit,
) {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // --- UI Feedback Section ---

        // Loading Indicator
        if (uiState.isLoading) {
            LinearProgressIndicator(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        val errorMessage = if (uiState.errorMessage.isEmpty()) null else uiState.errorMessage as UiMessage.Resource
        val successMessage = if (uiState.successMessage.isEmpty()) null else uiState.successMessage as UiMessage.Resource

        // Error Message (Dismissible)
        errorMessage?.let { message ->
            StatusCard(
                message = message.resId,
                isError = true,
                onDismiss = onClearIntent
            )
        }


        // Success Message (Dismissible)
        successMessage?.let { message ->
            StatusCard(
                message = message.resId,
                isError = false,
                onDismiss = onClearIntent
            )
        }


        // --- Form Inputs ---

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = formState.productName,
            onValueChange = { onFormStateChanged(formState.copy(productName = it)) },
            label = { Text("Product Name") },
            enabled = uiState.isFormEnabled,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Quantity Purchased
            OutlinedTextField(
                value = formState.quantityPurchased,
                onValueChange = { onFormStateChanged(formState.copy(quantityPurchased = it)) },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = uiState.isFormEnabled,
                modifier = Modifier.weight(0.5f)
            )

            // Unit Format
            OutlinedTextField(
                value = formState.unitFormat,
                onValueChange = { onFormStateChanged(formState.copy(unitFormat = it)) },
                label = { Text("Unit (e.g., Kg, L, unit)") },
                enabled = uiState.isFormEnabled,
                modifier = Modifier.weight(0.5f)
            )
        }

        OutlinedTextField(
            value = formState.price,
            onValueChange = { onFormStateChanged(formState.copy(price = it)) },
            label = { Text("Price Paid (€)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            enabled = uiState.isFormEnabled,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formState.storeName,
            onValueChange = { onFormStateChanged(formState.copy(storeName = it)) },
            label = { Text("Store Name") },
            enabled = uiState.isFormEnabled,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Registration Button ---
        Button(
            onClick = onRegister,
            enabled = uiState.isFormEnabled && !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (uiState.isLoading) {
                Text("Registering...")
            } else {
                Icon(Icons.Default.Add, contentDescription = "Register")
                Spacer(Modifier.width(8.dp))
                Text("Record Purchase")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PurchaseRegistrationFormPreview() {

    val padding = PaddingValues()
    val uiState = PurchaseRecordUiState()
    val onClearIntent = {}
    val formState = PurchaseRegistrationFormState()
    val onFormStateChanged: (PurchaseRegistrationFormState) -> Unit = {}
    val onRegister = {}


    PurchaseRegistrationForm(
        padding,
        uiState,
        onClearIntent,
        formState,
        onFormStateChanged,
        onRegister
    )
}