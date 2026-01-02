package com.franciscogarciagarzon.acceptance_test

import io.cucumber.junit.CucumberOptions

/**
 * This class is the entry point for Cucumber.
 * The runner looks for the @CucumberOptions annotation here.
 */
@CucumberOptions(
    features = ["features"],
    glue = ["com.franciscogarciagarzon.acceptance_test.steps"],
    tags = "not @ignored"
)
class CucumberTest