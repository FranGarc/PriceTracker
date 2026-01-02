package com.franciscogarciagarzon.commons.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class JvmLoggerQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AndroidLoggerQualifier