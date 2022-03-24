package com.nordsec.locationapp.presentation.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nordsec.locationapp.core.BaseViewModel
import com.nordsec.locationapp.domain.models.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel
@Inject constructor(private val useCases: LocationsUseCases) : BaseViewModel() {
    private val _viewState = MutableLiveData<LocationsViewState>()
    val viewState: LiveData<LocationsViewState>
        get() = _viewState
    private val compositeDisposable = CompositeDisposable()

    //    fun getLocationsSortedByDistance() {
//
//    }
//

    private val locationsObserver = object : SingleObserver<List<Location>> {
        override fun onSubscribe(d: Disposable) {
            _viewState.value = LocationsViewState.Loading
            compositeDisposable.add(d)
        }

        override fun onSuccess(locations: List<Location>) {
            _viewState.value = LocationsViewState.Locations(locations)
        }

        override fun onError(e: Throwable) {
            _viewState.value = LocationsViewState.Error(errorMsg = e.message)
        }
    }

    fun getLocationsSortedByCityName() {
        useCases.getLocationsSortedByCityNameUseCase()
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .subscribe(locationsObserver)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}