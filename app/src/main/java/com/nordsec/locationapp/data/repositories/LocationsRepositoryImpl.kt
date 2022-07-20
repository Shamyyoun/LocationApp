package com.nordsec.locationapp.data.repositories

import com.nordsec.locationapp.data.local.LocationsLocalDataSource
import com.nordsec.locationapp.domain.models.Location
import com.nordsec.locationapp.domain.repositories.LocationsRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LocationsRepositoryImpl
@Inject constructor(private val locationsLocalDS: LocationsLocalDataSource) : LocationsRepository {

    override fun getLocations(): Observable<Location> {
        return Observable.fromIterable(
            locationsLocalDS.getLocations().locations
        )
    }
}