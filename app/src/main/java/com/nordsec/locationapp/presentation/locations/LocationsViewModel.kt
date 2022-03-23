package com.nordsec.locationapp.presentation.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nordsec.locationapp.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel
@Inject constructor(private val useCases: LocationsUseCases) : BaseViewModel() {
    private val _viewState = MutableLiveData<LocationsViewState>()
    val viewState: LiveData<LocationsViewState>
        get() = _viewState

    //    fun getLocationsSortedByDistance() {
//
//    }
//
    fun getLocationsSortedByCityName() {
        useCases.getLocationsSortedByCityNameUseCase()
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .doOnSubscribe {
                _viewState.value = LocationsViewState.Loading
            }
            .doOnSuccess {
                _viewState.value = LocationsViewState.Locations(it)
            }
            .doOnError {
                _viewState.value = LocationsViewState.Error()
            }
            .subscribe()
    }
}