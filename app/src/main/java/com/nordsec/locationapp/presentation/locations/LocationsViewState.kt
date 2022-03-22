package com.nordsec.locationapp.presentation.locations

import com.nordsec.locationapp.domain.models.Location

sealed class LocationsViewState {
    data class Locations(val locations: List<Location>) : LocationsViewState()
    object Loading : LocationsViewState()
    data class Error(
        val errorMsg: String? = null,
        val errorMsgId: Int? = null
    ) : LocationsViewState()
}
