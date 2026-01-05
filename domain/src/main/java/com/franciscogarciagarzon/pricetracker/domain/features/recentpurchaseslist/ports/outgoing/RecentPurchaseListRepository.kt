package com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.outgoing

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import kotlinx.coroutines.flow.Flow

/**
 * Puerto de salida para la observación de datos de compra.
 * Siguiendo el principio de Inversión de Dependencias, este contrato
 * permite al dominio suscribirse a cambios en la persistencia sin conocer si los
 * datos provienen de Room, un socket en tiempo real o un sensor.
 */
interface RecentPurchaseListRepository {
    /**
     * Proporciona un flujo continuo de la lista de compras.
     * A diferencia de las operaciones "One-Shot" (como el registro),
     * aquí no se utiliza 'ResultWithValue'. Al ser un Flow, la gestión de errores
     * se delega a los operadores de flujo (catch) o se asume que el flujo emitirá
     * una lista vacía si no hay datos, manteniendo la tubería de datos abierta
     * y reactiva.
     * * @return Flow que emite la lista actualizada de PurchaseRecord cada vez
     * que ocurra un cambio en la fuente de datos.
     */
    fun getRecentPurchases(): Flow<List<PurchaseRecord>>
}