package com.franciscogarciagarzon.pricetracker.domain.common

interface ResultWithValue<out T> {
    val value: T?
}

fun <T> ResultWithValue<T>.getOrNull(): T? = value

