package com.nordsec.locationapp.domain.infra

import com.nordsec.locationapp.domain.models.Location

interface LocationManager {
    fun distanceBetween(start: Location, end: Location): Float
}