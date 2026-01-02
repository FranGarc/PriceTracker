package com.franciscogarciagarzon.commons.utils.implementations

import com.franciscogarciagarzon.commons.utils.contracts.SharingStrategyProvider
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject

class DefaultSharingStrategyProvider @Inject constructor(): SharingStrategyProvider {
    override fun getStrategy(): SharingStarted = SharingStarted.WhileSubscribed(5000L)
}