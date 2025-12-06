package com.franciscogarciagarzon.commons.utils.implementations

import com.franciscogarciagarzon.commons.utils.contracts.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class DefaultDispatcherProvider @Inject constructor(): DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Main.immediate // ensures determinism and performance when managing UI state
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
}