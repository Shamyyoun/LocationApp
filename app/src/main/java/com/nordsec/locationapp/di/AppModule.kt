package com.nordsec.locationapp.di

import com.nordsec.locationapp.data.local.LocationsLocalDataSource
import com.nordsec.locationapp.data.local.LocationsLocalDataSourceImpl
import com.nordsec.locationapp.data.repositories.LocationsRepositoryImpl
import com.nordsec.locationapp.domain.repositories.LocationsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideLocationsRepository(locationsLocalDS: LocationsLocalDataSource): LocationsRepository {
        return LocationsRepositoryImpl(locationsLocalDS)
    }

    @Provides
    fun provideLocationLocalDS(): LocationsLocalDataSource {
        return LocationsLocalDataSourceImpl()
    }

    @Provides
    @MainThreadScheduler
    fun provideMainScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    @IOScheduler
    fun provideIOScheduler(): Scheduler {
        return Schedulers.io()
    }
}