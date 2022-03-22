package com.nordsec.locationapp.domain.useCases

import com.nordsec.locationapp.domain.models.Location
import com.nordsec.locationapp.domain.repositories.LocationsRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetLocationsSortedByCityNameUseCase
@Inject constructor(private val locationsRepository: LocationsRepository) {

    operator fun invoke(): Single<List<Location>> {
        return locationsRepository
            .getLocations()
            .sorted { location1, location2 ->
                location1.city.compareTo(location2.city)
            }
            .toList()
    }
}