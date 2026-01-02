package com.franciscogarciagarzon.pricetracker.presentation.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StringResourceProviderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): StringResourceProvider {
    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }
}