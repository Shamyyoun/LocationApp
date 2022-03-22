package com.nordsec.locationapp.data.local

import com.nordsec.locationapp.data.dto.LocationsDto

interface LocationsLocalDataSource {
    fun getLocations(): LocationsDto
}