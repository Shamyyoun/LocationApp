package com.nordsec.locationapp.presentation.locations

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nordsec.locationapp.domain.models.Location
import com.nordsec.locationapp.domain.useCases.GetLocationsSortedByCityNameUseCase
import com.nordsec.locationapp.domain.useCases.GetLocationsSortedByDistanceUseCase
import com.nordsec.locationapp.getOrAwaitValue
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.powermock.api.mockito.PowerMockito
import java.io.IOException
import java.util.concurrent.TimeUnit


class LocationsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val useCases = LocationsUseCases(
        getLocationsSortedByCityNameUseCase = PowerMockito.mock(GetLocationsSortedByCityNameUseCase::class.java),
        getLocationsSortedByDistanceUseCase = PowerMockito.mock(GetLocationsSortedByDistanceUseCase::class.java)
    )
    private val viewModel = LocationsViewModel(useCases)
    private val testScheduler = TestScheduler()

    @Before
    fun setUp() {
        RxJavaPlugins.setInitIoSchedulerHandler { testScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }
    }

    @Test
    fun `getLocationsSortedByCityName() is emitting loading state when subscribing to the useCase observable`() {
        // Given
        val testObservable = Single.just(emptyList<Location>())
        PowerMockito.doReturn(testObservable)
            .`when`(useCases.getLocationsSortedByCityNameUseCase)
            .invoke()

        // When
        viewModel.getLocationsSortedByCityName()

        // Then
        assertEquals(LocationsViewState.Loading, viewModel.viewState.getOrAwaitValue())
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
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS) // Move 1 second

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
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS) // Move 1 second

        // Then
        // Assert that current view state is Error
        assertTrue(viewModel.viewState.getOrAwaitValue() is LocationsViewState.Error)

        val errorState = viewModel.viewState.getOrAwaitValue() as LocationsViewState.Error

        // Assert that error message is correct
        assertEquals(testException.message, errorState.errorMsg)
    }
}