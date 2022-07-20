package com.nordsec.locationapp.presentation.locations

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nordsec.locationapp.R
import com.nordsec.locationapp.databinding.ActivityLocationsBinding
import com.nordsec.locationapp.utils.showItAndHideOthers
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationsActivity : AppCompatActivity() {
    private val viewModel: LocationsViewModel by viewModels()

    private lateinit var binding: ActivityLocationsBinding
    private lateinit var locationsAdapter: LocationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_LocationApp)

        binding = ActivityLocationsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        createLocationsAdapter()
        configLocationsRv()
        observeViewModel()

        viewModel.getLocationsSortedByCityName()
    }

    private fun createLocationsAdapter() {
        locationsAdapter = LocationsAdapter(emptyList()).apply {
            onSortByDistanceClickListener = viewModel::getLocationsSortedByDistance
        }
    }

    private fun configLocationsRv() = binding.rvLocations.apply {
        LocationsAdapter(emptyList())

        addItemDecoration(
            DividerItemDecoration(
                this@LocationsActivity,
                LinearLayoutManager.VERTICAL
            )
        )

        adapter = locationsAdapter
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(this) {
            when (it) {
                is LocationsViewState.Locations -> renderLocations(it)
                is LocationsViewState.Loading -> renderLoading()
                is LocationsViewState.Error -> renderError(it)
            }
        }
    }

    private fun renderLocations(state: LocationsViewState.Locations) = with(binding) {
        // Change locations adapter data
        locationsAdapter.locations = state.locations
        locationsAdapter.notifyDataSetChanged()

        // Set the new sort by message
        tvSortedByMsg.text = when (state.sortBy) {
            is LocationsSortBy.CityName -> getString(R.string.sorted_by_city_name)
            is LocationsSortBy.DistanceFromCity -> getString(
                R.string.sorted_by_distance_from_s,
                state.sortBy.location.city
            )
        }

        // Show the view
        groupMainView.showItAndHideOthers(progressBar, tvErrorMsg)
    }

    private fun renderLoading() = with(binding) {
        progressBar.showItAndHideOthers(groupMainView, tvErrorMsg)
    }

    private fun renderError(error: LocationsViewState.Error) = with(binding) {
        val errorMsg = error.errorMsg
            ?: error.errorMsgId?.let { getString(it) }
            ?: getString(R.string.something_went_wrong)

        tvErrorMsg.text = errorMsg
        tvErrorMsg.showItAndHideOthers(groupMainView, progressBar)
    }
}