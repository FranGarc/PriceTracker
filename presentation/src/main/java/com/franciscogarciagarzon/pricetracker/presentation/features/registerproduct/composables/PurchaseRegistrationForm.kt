package com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.valueObjects.UnitFormat
import com.franciscogarciagarzon.pricetracker.presentation.R
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.PurchaseRecordUiState
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.mappers.toDisplayName

data class PurchaseRegistrationFormState(
    val productName: String = "",
    val quantityPurchased: String = "",
    val unitFormat: UnitFormat = UnitFormat.entries.first(),
    val price: String = "",
    val storeName: String = "",
)

object PurchaseFormTestTags {
    const val PRODUCT_NAME_INPUT = "product_name_input"
    const val QUANTITY_INPUT = "quantity_input"
    const val UNIT_FORMAT_INPUT = "unit_format_input"
    const val PRICE_INPUT = "price_input"
    const val STORE_NAME_INPUT = "store_name_input"
    const val REGISTER_BUTTON = "register_button"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseRegistrationForm(
    padding: PaddingValues,
    onProductNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onUnitFormatChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onStoreNameChange: (String) -> Unit,
    formState: () -> PurchaseRegistrationFormState, // Pass as provider
    isLoading: Boolean,
    isFormEnabled: Boolean,
    onRegister: () -> Unit,
) {
    // Cacheamos los valores para que los campos individuales
    // no disparen  recomposition de TODA la columna cuando cambie uno
    val enabled = isFormEnabled
    val loading = isLoading
    val currentForm = formState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = currentForm.productName,
            onValueChange = onProductNameChange,
            label = { Text(stringResource(id = R.string.purchaseRegistrationScreen_product_name_dropdown_label)) },
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(PurchaseFormTestTags.PRODUCT_NAME_INPUT)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentForm.quantityPurchased,
                onValueChange = onQuantityChange,
                label = { Text(stringResource(id = R.string.purchaseRegistrationScreen_quantity_dropdown_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = enabled,
                modifier = Modifier
                    .weight(0.5f)
                    .testTag(PurchaseFormTestTags.QUANTITY_INPUT)
            )

            var unitDropdownExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = unitDropdownExpanded,
                onExpandedChange = { if (enabled) unitDropdownExpanded = !unitDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = currentForm.unitFormat.toDisplayName().let { stringResource(it) }, // The form state still holds the selected string
                    onValueChange = {},// vacío para que sólo sea de lectura
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.purchaseRegistrationScreen_unit_dropdown_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitDropdownExpanded) },
                    modifier = Modifier
                        .testTag(PurchaseFormTestTags.UNIT_FORMAT_INPUT)
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable
                        ),
                    enabled = enabled
                )
                ExposedDropdownMenu(
                    expanded = unitDropdownExpanded,
                    onDismissRequest = { unitDropdownExpanded = false }
                ) {
                    UnitFormat.entries.forEach { selectionOption ->
                        val displayName = stringResource(selectionOption.toDisplayName())
                        DropdownMenuItem(
                            text = { Text(displayName) },
                            onClick = {
                                // actualiza el estado del formulario con el "nombre de pantalla"
                                // en vez del nombre del enum
                                onUnitFormatChange(selectionOption.name)
                                unitDropdownExpanded = false
                            },
                            modifier = Modifier.testTag("${PurchaseFormTestTags.UNIT_FORMAT_INPUT}_${selectionOption.name}")
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = currentForm.price,
            onValueChange = onPriceChange,
            label = { Text(stringResource(id = R.string.purchaseRegistrationScreen_price_dropdown_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(PurchaseFormTestTags.PRICE_INPUT)

        )

        OutlinedTextField(
            value = currentForm.storeName,
            onValueChange = onStoreNameChange,
            label = { Text(stringResource(id = R.string.purchaseRegistrationScreen_store_name_dropdown_label)) },
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(PurchaseFormTestTags.STORE_NAME_INPUT)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Registration Button ---
        Button(
            onClick = onRegister,
            enabled = enabled && !loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag(PurchaseFormTestTags.REGISTER_BUTTON)
        ) {
            if (loading) {
                Text(stringResource(id = R.string.purchaseRegistrationScreen_loading_label))
            } else {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(id = R.string.purchaseRegistrationScreen_button_record_purchase_label))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PurchaseRegistrationFormPreview() {

    val padding = PaddingValues()
    val uiState = PurchaseRecordUiState()
    val formState = PurchaseRegistrationFormState()
    val onRegister = {}


    PurchaseRegistrationForm(
        padding,
        onProductNameChange = {},
        onQuantityChange = {},
        onUnitFormatChange = {},
        onPriceChange = {},
        onStoreNameChange = {},
        formState = { formState },
        isLoading = uiState.isLoading,
        isFormEnabled = uiState.isFormEnabled,
        onRegister = onRegister,
    )
}