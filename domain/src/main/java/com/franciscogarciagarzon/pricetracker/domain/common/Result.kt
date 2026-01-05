package com.franciscogarciagarzon.pricetracker.domain.common

/**
 * Interfaz base para el transporte de resultados en la capa de dominio.
 * Se define como interfaz con tipo genérico out T para permitir
 * la covarianza, facilitando que resultados de tipos derivados puedan
 * ser tratados como tipos base.
 */
interface ResultWithValue<out T> {
    /**
     * El valor contenido en el resultado.
     * Puede ser nulo si el resultado representa un estado de error o vacío.
     */
    val value: T?
}

/**
 * Función de extensión para facilitar la recuperación segura del valor.
 * Se abstrae el acceso al valor para simplificar la sintaxis
 * en las capas de llamada (Use Cases y ViewModels) sin exponer
 * directamente la lógica interna del contenedor.
 */
fun <T> ResultWithValue<T>.getOrNull(): T? = value

