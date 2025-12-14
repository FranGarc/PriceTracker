package com.franciscogarciagarzon.pricetracker.presentation

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * A custom test runner required for Hilt to work in Android Instrumentation tests.
 *
 * This runner tells the test framework to use Hilt's `HiltTestApplication` as the
 * application class during the test run. This allows Hilt to properly manage
 * the dependency injection container for the tests.
 */
class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(
            classLoader,
            HiltTestApplication::class.java.name,
            context
        )
    }
}
