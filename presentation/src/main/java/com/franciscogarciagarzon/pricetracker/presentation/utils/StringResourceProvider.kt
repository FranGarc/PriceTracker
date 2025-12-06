package com.franciscogarciagarzon.pricetracker.presentation.utils

import androidx.annotation.StringRes

interface StringResourceProvider {
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
}