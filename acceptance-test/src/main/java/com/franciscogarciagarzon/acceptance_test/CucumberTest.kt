package com.franciscogarciagarzon.acceptance_test

import io.cucumber.junit.CucumberOptions

/**
 * This class is the entry point for Cucumber.
 * The runner looks for the @CucumberOptions annotation here.
 */
@CucumberOptions(
    features = ["features"],
    glue = ["com.franciscogarciagarzon.acceptance_test.steps"],
    tags = "not @ignored",
    plugin = [
        "pretty",
        // Usamos una ruta relativa que Android sí puede manejar dentro de su sandbox
        "html:/sdcard/Download/target/reports/cucumber/report.html",
        "json:/sdcard/Download/target/reports/cucumber/report.json"
    ],
)
@Suppress("unused") class CucumberTest