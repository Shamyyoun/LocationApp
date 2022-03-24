package com.nordsec.locationapp.domain.useCases

import com.nordsec.locationapp.domain.infra.LocationManager
import com.nordsec.locationapp.domain.models.Location
import com.nordsec.locationapp.domain.repositories.LocationsRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito
import java.io.IOException
import java.util.concurrent.TimeUnit

class GetLocationsSortedByDistanceUseCaseTest {
    private val locationsRepository = PowerMockito.mock(LocationsRepository::class.java)
    private val locationManager = PowerMockito.mock(LocationManager::class.java)
    private val useCase = GetLocationsSortedByDistanceUseCase(locationsRepository, locationManager)
    private val testScheduler = TestScheduler()
    private lateinit var testOriginLocation: Location

    @Before
    fun setUp() {
        testOriginLocation = Location(
            city = "Cairo",
            latitude = 0.155f,
            longitude = 0.166f
        )
    }

    @Test
    fun `useCase is sorting the locations by distance when receives valid locations from the repository`() {
        // Given
        val testLocations = listOf(
            Location(
                city = "Dubai",
                latitude = 0.111f,
                longitude = 0.122f
            ),
            Location(
                city = "Berlin",
                latitude = 0.133f,
                longitude = 0.144f
            ),
            Location(
                city = "Cairo",
                latitude = 0.155f,
                longitude = 0.166f
            )
        )
        val testObservable = Observable.fromIterable(testLocations)
        PowerMockito.doReturn(testObservable)
            .`when`(locationsRepository)
            .getLocations()

        // Consider distance from Cairo to Berlin > distance from Cairo to Dubai
        PowerMockito.doReturn(1000f)
            .`when`(locationManager)
            .distanceBetween(testOriginLocation, testLocations[0])
        PowerMockito.doReturn(2000f)
            .`when`(locationManager)
            .distanceBetween(testOriginLocation, testLocations[1])
        PowerMockito.doReturn(0f)
            .`when`(locationManager)
            .distanceBetween(testOriginLocation, testLocations[2])

        // When
        val locations = useCase.invoke(testOriginLocation)

        // Then
        locations.subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()
            .run {
                // Just created
                assertNotComplete()
                assertNoErrors()
                assertNoValues()

                // 1 second passed
                testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

                // Assert that sorting by distance is done successfully
                assertValue {
                    it[0].city == "Cairo"
                            && it[1].city == "Dubai"
                            && it[2].city == "Berlin"
                }
                assertComplete()
                dispose()
            }
    }

    @Test
    fun `useCase is returning Single of error when the repository throws and exception`() {
        // Given
        val testException = IOException("some error")
        PowerMockito.`when`(locationsRepository.getLocations()).thenAnswer {
            throw testException
        }

        // When
        val locations = useCase.invoke(testOriginLocation)

        // Then
        locations.subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()
            .run {
                // Just created
                assertNotComplete()
                assertNoErrors()
                assertNoValues()

                // 1 second passed
                testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

                // Assert that it's emitting error
                assertError(IOException::class.java)
                assertError {
                    testException.message == it.message
                }
                dispose()
            }
    }
}