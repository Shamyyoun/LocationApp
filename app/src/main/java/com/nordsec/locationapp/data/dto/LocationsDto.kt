package com.nordsec.locationapp.data.dto

import com.google.gson.annotations.SerializedName
import com.nordsec.locationapp.domain.models.Location

data class LocationsDto(
    @SerializedName("locations")
    val locations: List<Location>
)