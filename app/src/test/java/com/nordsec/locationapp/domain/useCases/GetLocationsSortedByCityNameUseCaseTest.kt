package com.nordsec.locationapp.domain.useCases

import com.nordsec.locationapp.domain.models.Location
import com.nordsec.locationapp.domain.repositories.LocationsRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Test
import org.powermock.api.mockito.PowerMockito
import java.io.IOException
import java.util.concurrent.TimeUnit

class GetLocationsSortedByCityNameUseCaseTest {
    private val locationsRepository = PowerMockito.mock(LocationsRepository::class.java)
    private val useCase = GetLocationsSortedByCityNameUseCase(locationsRepository)
    private val testScheduler = TestScheduler()

    @Test
    fun `useCase is sorting the locations when receives valid locations from the repository`() {
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
            )
        )
        val testObservable = Observable.fromIterable(testLocations)
        PowerMockito.doReturn(testObservable)
            .`when`(locationsRepository)
            .getLocations()

        // When
        val locations = useCase.invoke()

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

                // Assert that sorting by name is done successfully
                assertValue {
                    it[0].city == "Berlin" && it[1].city == "Dubai"
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
        val locations = useCase.invoke()

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