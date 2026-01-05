package com.franciscogarciagarzon.pricetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.franciscogarciagarzon.pricetracker.presentation.features.registerproduct.PurchaseRegistrationScreen
import com.franciscogarciagarzon.pricetracker.presentation.ui.theme.PriceTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal y punto de entrada de la aplicación.
 * Utiliza Compose para la interfaz de usuario y Hilt para la gestión de dependencias.
 * El uso de @AndroidEntryPoint es obligatorio para que Hilt pueda inyectar el ViewModel
 * y los casos de uso dentro de la jerarquía de vistas de esta actividad.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PriceTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    PurchaseRegistrationScreen()
                }
            }
        }
    }
}

