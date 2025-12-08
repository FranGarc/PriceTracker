package com.franciscogarciagarzon.pricetracker.presentation.util

import com.franciscogarciagarzon.commons.utils.contracts.SharingStrategyProvider
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject

class TestSharingStrategyProvider @Inject constructor() : SharingStrategyProvider {
    override fun getStrategy(): SharingStarted = SharingStarted.Eagerly
}
