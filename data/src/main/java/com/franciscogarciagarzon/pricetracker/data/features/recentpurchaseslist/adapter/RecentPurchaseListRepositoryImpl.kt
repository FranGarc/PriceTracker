package com.franciscogarciagarzon.pricetracker.data.features.recentpurchaseslist.adapter

import com.franciscogarciagarzon.pricetracker.data.database.dao.PriceRecordDao
import com.franciscogarciagarzon.pricetracker.data.mappers.PurchaseDataMapper
import com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.outgoing.RecentPurchaseListRepository
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementación del repositorio para la observación de compras recientes.
 * Transforma el flujo de datos de bajo nivel (DB) en un flujo
 * de alta abstracción (Dominio). Al ser reactivo, cualquier cambio en las
 * tablas de la base de datos se notificará automáticamente a los suscriptores.
 */
class RecentPurchaseListRepositoryImpl @Inject constructor(
    private val priceRecordDao: PriceRecordDao,
    private val mapper: PurchaseDataMapper
) : RecentPurchaseListRepository {

    /**
     * Obtiene y mapea el flujo de registros.
     * Se utiliza el operador 'map' de Flow para transformar cada
     * emisión de la base de datos.
     * Esto garantiza que la capa de Dominio y Presentation
     * reciban siempre entidades 'PurchaseRecord' listas para usar, manteniendo
     * el acoplamiento con Room confinado exclusivamente en esta clase.
     */
    override fun getRecentPurchases(): Flow<List<PurchaseRecord>> {
        return priceRecordDao.getRecentRecordsWithDetails()
            .map { entities ->
                entities.map { mapper.toDomain(it) }
            }
    }
}