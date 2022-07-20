package com.nordsec.locationapp.domain.useCases

import com.nordsec.locationapp.domain.infra.LocationManager
import com.nordsec.locationapp.domain.models.Location
import com.nordsec.locationapp.domain.repositories.LocationsRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetLocationsSortedByDistanceUseCase @Inject constructor(
    private val locationsRepository: LocationsRepository,
    private val locationManager: LocationManager
) {

    operator fun invoke(location: Location): Single<List<Location>> {
        return try {
            locationsRepository
                .getLocations()
                .sorted { location1, location2 ->
                    val result = locationManager.distanceBetween(location, location1) -
                            locationManager.distanceBetween(location, location2)

                    result.toInt()
                }
                .toList()
        } catch (e: Throwable) {
            Single.error(e)
        }
    }
}