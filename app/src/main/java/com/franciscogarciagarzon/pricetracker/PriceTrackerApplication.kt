package com.franciscogarciagarzon.pricetracker

import android.app.Application
import com.franciscogarciagarzon.commons.utils.contracts.LoggerContract
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class PriceTrackerApplication : Application() {

    // Hilt will inject the default Logger, which AppModule
    // has configured to be the AndroidLogger.
    @Inject
    lateinit var logger: LoggerContract
}