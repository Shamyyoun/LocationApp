package com.nordsec.locationapp.presentation.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nordsec.locationapp.domain.models.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel
@Inject constructor(private val useCases: LocationsUseCases) : ViewModel() {
    private val _viewState = MutableLiveData<LocationsViewState>()
    val viewState: LiveData<LocationsViewState>
        get() = _viewState
    private val compositeDisposable = CompositeDisposable()

    fun getLocationsSortedByCityName() {
        val observer = object : SingleObserver<List<Location>> {
            override fun onSubscribe(d: Disposable) {
                _viewState.value = LocationsViewState.Loading
                compositeDisposable.add(d)
            }

            override fun onSuccess(locations: List<Location>) {
                _viewState.value = LocationsViewState.Locations(
                    locations = locations,
                    sortBy = LocationsSortBy.CityName
                )
            }

            override fun onError(e: Throwable) {
                _viewState.value = LocationsViewState.Error(errorMsg = e.message)
            }
        }

        useCases.getLocationsSortedByCityNameUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }

    fun getLocationsSortedByDistance(location: Location) {
        val observer = object : SingleObserver<List<Location>> {
            override fun onSubscribe(d: Disposable) {
                _viewState.value = LocationsViewState.Loading
                compositeDisposable.add(d)
            }

            override fun onSuccess(locations: List<Location>) {
                _viewState.value = LocationsViewState.Locations(
                    locations = locations,
                    sortBy = LocationsSortBy.DistanceFromCity(
                        location = location
                    )
                )
            }

            override fun onError(e: Throwable) {
                _viewState.value = LocationsViewState.Error(errorMsg = e.message)
            }
        }

        useCases.getLocationsSortedByDistanceUseCase(location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}