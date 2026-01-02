package com.franciscogarciagarzon.commons.utils.contracts

/**
 * A module-agnostic logging interface.
 * Code should depend on this interface, not on a specific logging implementation.
 */
interface LoggerContract {
    fun v(tag: String, message: String)
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun w(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable? = null)
    fun wtf(tag: String, message: String, throwable: Throwable? = null)
}
