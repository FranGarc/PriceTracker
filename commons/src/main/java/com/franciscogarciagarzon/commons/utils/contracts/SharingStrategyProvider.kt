package com.franciscogarciagarzon.commons.utils.contracts

import kotlinx.coroutines.flow.SharingStarted

/**
 * Provides a SharingStarted policy. This allows swapping the policy
 * between production (e.g., WhileSubscribed) and tests (e.g., Eagerly).
 */
interface SharingStrategyProvider {
    fun getStrategy(): SharingStarted
}