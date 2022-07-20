package com.nordsec.locationapp.presentation.locations

import com.nordsec.locationapp.domain.useCases.GetLocationsSortedByCityNameUseCase
import com.nordsec.locationapp.domain.useCases.GetLocationsSortedByDistanceUseCase
import javax.inject.Inject

data class LocationsUseCases @Inject constructor(
    val getLocationsSortedByCityNameUseCase: GetLocationsSortedByCityNameUseCase,
    val getLocationsSortedByDistanceUseCase: GetLocationsSortedByDistanceUseCase
)