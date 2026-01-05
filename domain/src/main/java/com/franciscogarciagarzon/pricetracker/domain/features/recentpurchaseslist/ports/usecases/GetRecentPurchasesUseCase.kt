package com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.usecases

import com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.incoming.GetRecentPurchasesPort
import com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.outgoing.RecentPurchaseListRepository
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementación del caso de uso para obtener las compras recientes.
 * Aunque en este caso la lógica es una delegación directa al repositorio,
 * mantener esta clase asegura que la capa de Presentation (ViewModels) nunca hable
 * directamente con la capa de Data. Esto preserva la estructura de cebolla de
 * Clean Architecture.
 */
class GetRecentPurchasesUseCase @Inject constructor(
    private val repository: RecentPurchaseListRepository
) : GetRecentPurchasesPort {
    /**
     * Ejecuta la suscripción al flujo de datos de compras.
     * Se devuelve el Flow directamente desde el repositorio. Esto permite
     * que cualquier transformación futura de los datos de dominio (como filtrado o
     * ordenación específica de negocio) se pueda centralizar aquí sin modificar
     * la capa de datos ni la UI.
     */
    override fun invoke(): Flow<List<PurchaseRecord>>
        = repository.getRecentPurchases()
}