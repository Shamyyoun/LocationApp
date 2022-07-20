package com.nordsec.locationapp.presentation.locations

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nordsec.locationapp.RxImmediateSchedulerRule
import com.nordsec.locationapp.domain.models.Location
import com.nordsec.locationapp.domain.useCases.GetLocationsSortedByCityNameUseCase
import com.nordsec.locationapp.domain.useCases.GetLocationsSortedByDistanceUseCase
import com.nordsec.locationapp.getOrAwaitValue
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.powermock.api.mockito.PowerMockito
import java.io.IOException


class LocationsViewModelTest {
    @get:Rule
    val rxImmediateRule = RxImmediateSchedulerRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCases: LocationsUseCases
    private lateinit var viewModel: LocationsViewModel

    private lateinit var testOriginLocation: Location

    @Before
    fun setUp() {
        // Create use cases and the view model
        useCases = LocationsUseCases(
            getLocationsSortedByCityNameUseCase = PowerMockito.mock(
                GetLocationsSortedByCityNameUseCase::class.java
            ),
            getLocationsSortedByDistanceUseCase = PowerMockito.mock(
                GetLocationsSortedByDistanceUseCase::class.java
            )
        )
        viewModel = LocationsViewModel(useCases)

        // Create test origin
        testOriginLocation = Location(
            city = "Cairo",
            latitude = 0.155f,
            longitude = 0.166f
        )
    }

    @Test
    fun `getLocationsSortedByCityName() is emitting locations state when the useCase emits valid locations`() {
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
        val testObservable = Single.just(testLocations)
        PowerMockito.doReturn(testObservable)
            .`when`(useCases.getLocationsSortedByCityNameUseCase)
            .invoke()

        // When
        viewModel.getLocationsSortedByCityName()

        // Then
        // Assert that current view state is Locations
        assertTrue(viewModel.viewState.getOrAwaitValue() is LocationsViewState.Locations)

        val locationsState = viewModel.viewState.getOrAwaitValue() as LocationsViewState.Locations

        // Assert that emitted locations list size is the same as the test one
        assertEquals(testLocations.size, locationsState.locations.size)
        assertEquals(testLocations[0], locationsState.locations[0])
        assertEquals(testLocations[1], locationsState.locations[1])

        // Assert that sort by in the state is by city name
        assertEquals(LocationsSortBy.CityName, locationsState.sortBy)
    }

    @Test
    fun `getLocationsSortedByCityName() is emitting error state when the useCase emits error`() {
        // Given
        val testException = IOException("some error")
        val testObservable = Single.error<List<Location>>(testException)
        PowerMockito.doReturn(testObservable)
            .`when`(useCases.getLocationsSortedByCityNameUseCase)
            .invoke()

        // When
        viewModel.getLocationsSortedByCityName()

        // Then
        // Assert that current view state is Error
        assertTrue(viewModel.viewState.getOrAwaitValue() is LocationsViewState.Error)

        val errorState = viewModel.viewState.getOrAwaitValue() as LocationsViewState.Error

        // Assert that error message is correct
        assertEquals(testException.message, errorState.errorMsg)
    }

    @Test
    fun `getLocationsSortedByDistance() is emitting locations state when the useCase emits valid locations`() {
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
        val testObservable = Single.just(testLocations)
        PowerMockito.doReturn(testObservable)
            .`when`(useCases.getLocationsSortedByDistanceUseCase)
            .invoke(any())

        // When
        viewModel.getLocationsSortedByDistance(testOriginLocation)

        // Then
        // Assert that current view state is Locations
        assertTrue(viewModel.viewState.getOrAwaitValue() is LocationsViewState.Locations)

        val locationsState = viewModel.viewState.getOrAwaitValue() as LocationsViewState.Locations

        // Assert that emitted locations list size is the same as the test one
        assertEquals(testLocations.size, locationsState.locations.size)
        assertEquals(testLocations[0], locationsState.locations[0])
        assertEquals(testLocations[1], locationsState.locations[1])
        assertEquals(testLocations[2], locationsState.locations[2])

        // Assert that sort by in the state is by distance from city
        assertTrue(locationsState.sortBy is LocationsSortBy.DistanceFromCity)
        assertEquals(
            testOriginLocation,
            (locationsState.sortBy as LocationsSortBy.DistanceFromCity).location
        )
    }

    @Test
    fun `getLocationsSortedByDistance() is emitting error state when the useCase emits error`() {
        // Given
        val testException = IOException("some error")
        val testObservable = Single.error<List<Location>>(testException)
        PowerMockito.doReturn(testObservable)
            .`when`(useCases.getLocationsSortedByDistanceUseCase)
            .invoke(any())

        // When
        viewModel.getLocationsSortedByDistance(testOriginLocation)

        // Then
        // Assert that current view state is Error
        assertTrue(viewModel.viewState.getOrAwaitValue() is LocationsViewState.Error)

        val errorState = viewModel.viewState.getOrAwaitValue() as LocationsViewState.Error

        // Assert that error message is correct
        assertEquals(testException.message, errorState.errorMsg)
    }
}