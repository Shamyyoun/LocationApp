package com.nordsec.locationapp.di

import com.nordsec.locationapp.data.local.LocationsLocalDataSource
import com.nordsec.locationapp.data.local.LocationsLocalDataSourceImpl
import com.nordsec.locationapp.data.repositories.LocationsRepositoryImpl
import com.nordsec.locationapp.domain.infra.LocationManager
import com.nordsec.locationapp.domain.repositories.LocationsRepository
import com.nordsec.locationapp.infra.LocationManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
    fun provideLocationManager(): LocationManager {
        return LocationManagerImpl()
    }
}