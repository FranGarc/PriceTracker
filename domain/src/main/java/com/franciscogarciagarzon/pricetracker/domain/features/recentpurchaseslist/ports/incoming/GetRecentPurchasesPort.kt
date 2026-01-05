package com.franciscogarciagarzon.pricetracker.domain.features.recentpurchaseslist.ports.incoming

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.entities.PurchaseRecord
import kotlinx.coroutines.flow.Flow

/**
 * Puerto de entrada para la recuperación de los últimos registros de compra.
 * Define un flujo de lectura de datos. Al devolver un Flow, el dominio
 * establece un contrato reactivo: la UI se suscribe una vez y recibirá actualizaciones
 * automáticas cada vez que los datos subyacentes cambien (ej. al añadir una nueva compra).
 */
interface GetRecentPurchasesPort {
    /**
     * Recupera el listado de compras recientes.
     * Se utiliza la sobrecarga del operador 'invoke' para permitir que el
     * caso de uso se llame como una función (ej. getRecentPurchasesPort()), lo cual
     * simplifica la sintaxis en el ViewModel y enfatiza que este puerto tiene
     * una única responsabilidad clara.
     * * @return Flow con la lista de entidades PurchaseRecord, emitiendo nuevos
     * estados ante cambios en la persistencia.
     */
    operator fun invoke(): Flow<List<PurchaseRecord>>
}