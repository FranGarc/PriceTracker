package com.franciscogarciagarzon.pricetracker.di

import android.util.Log
import com.franciscogarciagarzon.commons.utils.contracts.LoggerContract
import javax.inject.Inject

/**
 * The production implementation of the LoggerContract interface that uses Android's Logcat.
 */
class AndroidLogger @Inject constructor() : LoggerContract {
    override fun v(tag: String, message: String) {
        Log.v(tag, message)
    }

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }

    override fun wtf(tag: String, message: String, throwable: Throwable?) {
        Log.wtf(tag, message, throwable)
    }
}