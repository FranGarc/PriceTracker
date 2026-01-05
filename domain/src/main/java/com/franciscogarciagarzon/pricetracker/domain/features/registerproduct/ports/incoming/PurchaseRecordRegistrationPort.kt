package com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.ports.incoming

import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegisterCommand
import com.franciscogarciagarzon.pricetracker.domain.features.registerproduct.usecases.PurchaseRecordRegistrationResult

/**
 * Puerto de entrada para el registro de nuevos productos/compras.
 * Define el contrato de lo que el sistema "puede hacer" desde el punto de vista del usuario.
 * Siguiendo Clean Architecture, el ViewModel dependerá de esta interfaz y no de la implementación
 * concreta (UseCase), facilitando el testing y el desacoplamiento.
 */
interface PurchaseRecordRegistrationPort {
    /**
     * Procesa la intención de registrar una compra.
     * Se utiliza el patrón 'Command' para encapsular los parámetros de entrada.
     * Esto permite que, si en el futuro el formulario de registro añade más campos, la firma
     * de este método no cambie, evitando romper las clases que lo implementan o consumen.
     * * @param command Objeto con los datos en bruto capturados de la interfaz de usuario.
     * @return PurchaseRecordRegistrationResult Resultado sellado que la UI deberá gestionar.
     */
    suspend fun registerPurchaseRecord(
        command: PurchaseRecordRegisterCommand
    ): PurchaseRecordRegistrationResult
}