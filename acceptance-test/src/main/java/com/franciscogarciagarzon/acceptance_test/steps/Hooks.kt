package com.franciscogarciagarzon.acceptance_test.steps

import io.cucumber.java.Before

class Hooks {
    @Before(order = 0) // El orden 0 asegura que sea lo primero
    fun setup() {
        ComposeInitializer.initialize()
    }
}