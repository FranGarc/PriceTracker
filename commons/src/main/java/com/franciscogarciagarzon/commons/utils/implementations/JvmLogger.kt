package com.franciscogarciagarzon.commons.utils.implementations

import com.franciscogarciagarzon.commons.utils.contracts.LoggerContract
import javax.inject.Inject

/**
 * Test implementation of the LoggerContract that prints to the console,
 * avoiding Android framework dependencies.
 */
class JvmLogger @Inject constructor() : LoggerContract {
    override fun v(tag: String, message: String) {
        println("V/$tag: $message")
    }

    override fun d(tag: String, message: String) {
        println("D/$tag: $message")
    }

    override fun i(tag: String, message: String) {
        println("I/$tag: $message")
    }

    override fun w(tag: String, message: String) {
        println("W/$tag: $message")
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        println("E/$tag: $message")
        throwable?.printStackTrace()
    }

    override fun wtf(tag: String, message: String, throwable: Throwable?) {
        println("WTF/$tag: $message")
        throwable?.printStackTrace()
    }
}
