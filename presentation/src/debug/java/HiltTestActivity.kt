package com.franciscogarciagarzon.pricetracker.presentation

import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * An empty, Hilt-annotated activity used as an entry point for UI tests that require Hilt.
 * This activity lives in the `debug` source set, so it is included in the debug APK
 * and can be launched by instrumentation tests, but it is NOT included in the final
 * production APK.
 */
@AndroidEntryPoint
class HiltTestActivity : ComponentActivity()
