package com.nordsec.locationapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainThreadScheduler

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IOScheduler
