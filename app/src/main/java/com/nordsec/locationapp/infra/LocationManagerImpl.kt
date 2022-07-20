package com.nordsec.locationapp.infra

import android.location.Location as AndroidLocation
import com.nordsec.locationapp.domain.infra.LocationManager
import com.nordsec.locationapp.domain.models.Location
import javax.inject.Inject

class LocationManagerImpl @Inject constructor() : LocationManager {

    override fun distanceBetween(start: Location, end: Location): Float {
        val result = FloatArray(1)
        AndroidLocation.distanceBetween(
            start.latitude.toDouble(),
            start.longitude.toDouble(),
            end.latitude.toDouble(),
            end.longitude.toDouble(),
            result
        )
        return result[0]
    }
}