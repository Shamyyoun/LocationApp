package com.nordsec.locationapp.presentation.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nordsec.locationapp.domain.models.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel
@Inject constructor(private val useCases: LocationsUseCases) : ViewModel() {
    private val _viewState = MutableLiveData<LocationsViewState>()
    val viewState: LiveData<LocationsViewState>
        get() = _viewState

    //    fun getLocationsSortedByDistance() {
//
//    }
//
    fun getLocationsSortedByCityName() {
        useCases.getLocationsSortedByCityNameUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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