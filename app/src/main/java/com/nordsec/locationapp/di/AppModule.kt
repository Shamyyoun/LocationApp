package com.nordsec.locationapp.di

import com.nordsec.locationapp.data.local.LocationsLocalDataSource
import com.nordsec.locationapp.data.local.LocationsLocalDataSourceImpl
import com.nordsec.locationapp.data.repositories.LocationsRepositoryImpl
import com.nordsec.locationapp.domain.repositories.LocationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindLocationsRepository(impl: LocationsRepositoryImpl): LocationsRepository

    @Binds
    abstract fun bindLocationLocalDS(impl: LocationsLocalDataSourceImpl): LocationsLocalDataSource
}