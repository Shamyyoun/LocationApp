package com.nordsec.locationapp.domain.useCases

import com.nordsec.locationapp.domain.models.Location
import com.nordsec.locationapp.domain.repositories.LocationsRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetLocationsSortedByDistanceUseCase
@Inject constructor(private val locationsRepository: LocationsRepository) {

    operator fun invoke(location: Location): Observable<Location> {
        TODO("To be implemented")
    }
}