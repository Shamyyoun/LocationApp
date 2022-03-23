package com.nordsec.locationapp.data.repositories

import com.nordsec.locationapp.data.dto.LocationsDto
import com.nordsec.locationapp.data.local.LocationsLocalDataSource
import com.nordsec.locationapp.domain.models.Location
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Test
import org.powermock.api.mockito.PowerMockito
import java.io.IOException
import java.util.concurrent.TimeUnit

class LocationsRepositoryImplTest {
    private val locationsLocalDS = PowerMockito.mock(LocationsLocalDataSource::class.java)
    private val locationsRepository = LocationsRepositoryImpl(locationsLocalDS)
    private val testScheduler = TestScheduler()

    @Test
    fun `getLocations() is returning valid Observable with successful locations from the data source`() {
        // Given
        val testLocations = LocationsDto(
            locations = listOf(
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
        )
        PowerMockito.doReturn(testLocations)
            .`when`(locationsLocalDS)
            .getLocations()

        // When
        val locations = locationsRepository.getLocations()

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
                testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)

                assertValueCount(testLocations.locations.size)
                assertValueAt(0) {
                    it.city == testLocations.locations[0].city
                            && it.latitude == testLocations.locations[0].latitude
                            && it.longitude == testLocations.locations[0].longitude
                }
                assertValueAt(1) {
                    it.city == testLocations.locations[1].city
                            && it.latitude == testLocations.locations[1].latitude
                            && it.longitude == testLocations.locations[1].longitude
                }
                assertComplete()
            }
    }

    @Test
    fun `getLocations() is throwing exception when the data source throws and exception`() {
        // Given
        val testException = IOException("some error")
        PowerMockito.`when`(locationsLocalDS.getLocations()).thenAnswer {
            throw testException
        }

        // When
        val exception = Assert.assertThrows(IOException::class.java) {
            locationsRepository.getLocations()
        }

        // Then
        Assert.assertEquals(testException.message, exception.message)
    }
}