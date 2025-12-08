package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.franciscogarciagarzon.pricetracker.presentation.R
import com.franciscogarciagarzon.pricetracker.presentation.UiMessage
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.PurchaseRecordUiState
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.mappers.toDisplayName
import com.franciscogarciagarzon.pricetracker.presentation.isEmpty

data class PurchaseRegistrationFormState(
    val productName: String = "",
    val quantityPurchased: String = "",
    val unitFormat:  UnitFormat = UnitFormat.entries.first(),
    val price: String = "",
    val storeName: String = "",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseRegistrationForm(
    padding: PaddingValues,
    uiState: PurchaseRecordUiState,
    onClearIntent: () -> Unit,
    formState: PurchaseRegistrationFormState,
    onFormStateChanged: (PurchaseRegistrationFormState) -> Unit,
    onRegister: () -> Unit,
) {
    Column(
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
            label = { Text(stringResource(id = R.string.purchaseRegistrationScreen_product_name_dropdown_label)) },
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
                label = { Text(stringResource(id = R.string.purchaseRegistrationScreen_quantity_dropdown_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = uiState.isFormEnabled,
                modifier = Modifier.weight(0.5f)
            )

            // Unit Format
//            OutlinedTextField(
//                value = formState.unitFormat,
//                onValueChange = { onFormStateChanged(formState.copy(unitFormat = it)) },
//                label = { Text("Unit (e.g., Kg, L, unit)") },
//                enabled = uiState.isFormEnabled,
//                modifier = Modifier.weight(0.5f)
//            )
            var unitDropdownExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = unitDropdownExpanded,
                onExpandedChange = { if (uiState.isFormEnabled) unitDropdownExpanded = !unitDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = formState.unitFormat.toDisplayName(), // The form state still holds the selected string
                    onValueChange = {},// it needs to be empty because it's readonly
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.purchaseRegistrationScreen_unit_dropdown_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable
                        )
                        .weight(0.5f),
                    enabled = uiState.isFormEnabled
                )
                ExposedDropdownMenu(
                    expanded = unitDropdownExpanded,
                    onDismissRequest = { unitDropdownExpanded = false }
                ) {
                    UnitFormat.entries.forEach { selectionOption ->
                        val displayName = selectionOption.toDisplayName()
                        DropdownMenuItem(
                            text = { Text(displayName) }, // Use the mapper here
                            onClick = {
                                // Update the form state with the display name string
                                onFormStateChanged(formState.copy(unitFormat = selectionOption))
                                unitDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = formState.price,
            onValueChange = { onFormStateChanged(formState.copy(price = it)) },
            label = { Text(stringResource(id = R.string.purchaseRegistrationScreen_price_dropdown_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            enabled = uiState.isFormEnabled,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formState.storeName,
            onValueChange = { onFormStateChanged(formState.copy(storeName = it)) },
            label = { Text(stringResource(id = R.string.purchaseRegistrationScreen_store_name_dropdown_label)) },
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
                Text(stringResource(id = R.string.purchaseRegistrationScreen_loading_label))
            } else {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(id = R.string.purchaseRegistrationScreen_button_record_purchase_label))
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