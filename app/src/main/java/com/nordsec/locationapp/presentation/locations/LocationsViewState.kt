package com.nordsec.locationapp.presentation.locations

import com.nordsec.locationapp.domain.models.Location

sealed class LocationsViewState {
    object Loading : LocationsViewState()

    data class Locations(
        val locations: List<Location>,
        val sortBy: LocationsSortBy
    ) : LocationsViewState()

    data class Error(
        val errorMsg: String? = null,
        val errorMsgId: Int? = null
    ) : LocationsViewState()
}

sealed class LocationsSortBy {
    object CityName : LocationsSortBy()

    data class DistanceFromCity(val location: Location) : LocationsSortBy()
}