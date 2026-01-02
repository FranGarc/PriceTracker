package com.franciscogarciagarzon.commons.utils

import com.franciscogarciagarzon.commons.utils.contracts.LoggerContract
import com.franciscogarciagarzon.commons.utils.implementations.JvmLogger

/**
 * A static logger facade that delegates logging calls to a swappable Logger implementation.
 *
 * By default, it uses a console-based JvmLogger.
 *
 * The `inject` method allows a dependency injection framework (like Hilt) to replace
 * the default logger with a context-appropriate one (e.g., AndroidLogger in the app,
 * or a specific test logger).
 */
object Logger: LoggerContract {
    // Default logger, safe to use in any environment (JVM, test, etc.).
    private var delegate: LoggerContract = JvmLogger()

    /**
     * Injects the Logger implementation to be used by the facade.
     * This method should be called once at application startup.
     */
    fun inject(logger: LoggerContract) {
        this.delegate = logger
    }

    override fun v(tag: String, message: String) = delegate.v(tag, message)
    override fun d(tag: String, message: String) = delegate.d(tag, message)
    override fun i(tag: String, message: String) = delegate.i(tag, message)
    override fun w(tag: String, message: String) = delegate.w(tag, message)
    override fun e(
        tag: String,
        message: String,
        throwable: Throwable?
    ) = delegate.e(tag, message, throwable)
    override fun wtf(
        tag: String,
        message: String,
        throwable: Throwable?
    ) = delegate.wtf(tag, message, throwable)
}