package com.franciscogarciagarzon.acceptance_test.test

import android.os.Bundle
import io.cucumber.android.runner.CucumberAndroidJUnitRunner

class CucumberTestRunner : CucumberAndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle) {
        val cacheDir = targetContext.cacheDir.absolutePath
        arguments.putString("plugin", "pretty,html:$cacheDir/report.html")
        super.onCreate(arguments)
    }
}