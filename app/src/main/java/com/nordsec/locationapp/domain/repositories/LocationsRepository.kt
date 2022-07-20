package com.nordsec.locationapp.domain.repositories

import com.nordsec.locationapp.domain.models.Location
import io.reactivex.rxjava3.core.Observable

interface LocationsRepository {
    fun getLocations(): Observable<Location>
}