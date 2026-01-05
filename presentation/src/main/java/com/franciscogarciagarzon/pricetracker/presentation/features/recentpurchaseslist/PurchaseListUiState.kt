package com.franciscogarciagarzon.pricetracker.presentation.features.recentpurchaseslist

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord

/**
 * Estados de representación para la lista de compras recientes.
 * Se utiliza una 'sealed interface' para modelar la UI como una máquina de estados
 * finitos. Esto garantiza que la vista sea siempre un reflejo fiel de la lógica de negocio
 * y que el compilador nos obligue a manejar todos los escenarios posibles (exhaustividad).
 */
sealed interface PurchaseListUiState {
    /**
     * Estado inicial o de transición.
     * Se emite mientras el Flow de la base de datos está resolviendo la primera consulta.
     * Permite a la UI mostrar indicadores de carga (Shimmers/Spinners) y evitar
     * saltos visuales bruscos (flickering).
     */
    data object Loading : PurchaseListUiState
    /**
     * Representa el éxito en la obtención de datos.
     * @param purchases Lista de registros de dominio [PurchaseRecord].
     * Desacopla la vista de la fuente de datos; la UI solo se preocupa por renderizar
     * lo que recibe en este contenedor.
     */
    data class Success(val purchases: List<PurchaseRecord>) : PurchaseListUiState
    /**
     * Estado semántico de ausencia de datos.
     * Es fundamental distinguir entre "Cargando" y "Vacío". Permite implementar
     * un 'Empty State' con llamadas a la acción (CTA) para mejorar la retención del usuario.
     */
    data object Empty : PurchaseListUiState
    /**
     * Captura de fallos en el flujo de datos.
     * @param message Descripción técnica o amigable del error.
     * El Reducer o el operador 'catch' del Flow transforman excepciones en este estado.
     * Evita que la aplicación se cierre (crash) y proporciona feedback al usuario
     * sobre qué ha fallado (ej. error de lectura en disco).
     */
    data class Error(val message: String) : PurchaseListUiState
}