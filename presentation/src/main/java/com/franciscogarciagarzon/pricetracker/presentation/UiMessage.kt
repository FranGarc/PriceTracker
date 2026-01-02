package com.franciscogarciagarzon.pricetracker.presentation

import androidx.annotation.StringRes

sealed class UiMessage {
    data class DynamicString(val text: String) : UiMessage()
    data class Resource(
        @param:StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : UiMessage()

    object None : UiMessage()
}

fun UiMessage.isEmpty(): Boolean{
    return this is UiMessage.None
}