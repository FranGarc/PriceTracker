package com.franciscogarciagarzon.acceptance_test.steps

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

object ComposeInitializer {
    val composeTestRule = createEmptyComposeRule()

    fun initialize() {
        try {
            // Este es el "truco": aplicamos la regla a un Statement vacío
            // para que el código interno de Android registre el ComposeRootRegistry.
            composeTestRule.apply(object : Statement() {
                override fun evaluate() {}
            }, Description.EMPTY).evaluate()
        } catch (e: Throwable) {
            // Si ya está inicializado, ignoramos el error
        }
    }
}