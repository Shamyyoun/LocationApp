package com.nordsec.locationapp.core

import androidx.lifecycle.ViewModel
import com.nordsec.locationapp.di.IOScheduler
import com.nordsec.locationapp.di.MainThreadScheduler
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    @Inject
    @MainThreadScheduler
    lateinit var mainScheduler: Scheduler

    @Inject
    @IOScheduler
    lateinit var ioScheduler: Scheduler
}