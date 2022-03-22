package com.nordsec.locationapp.presentation.locations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nordsec.locationapp.R
import com.nordsec.locationapp.databinding.ActivityLocationsBinding
import com.nordsec.locationapp.domain.models.Location
import com.nordsec.locationapp.utils.showItAndHideOthers
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationsActivity : AppCompatActivity() {
    val viewModel: LocationsViewModel by viewModels()

    private lateinit var binding: ActivityLocationsBinding
    private val locationsAdapter = LocationsAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_LocationApp)

        binding = ActivityLocationsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        configLocationsRv()
        observeViewModel()

        viewModel.getLocationsSortedByCityName()
    }

    private fun configLocationsRv() = binding.rvLocations.apply {
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
                is LocationsViewState.Locations -> renderLocations(it.locations)
                is LocationsViewState.Loading -> renderLoading()
                is LocationsViewState.Error -> renderError(it)
            }
        }
    }

    private fun renderLocations(locations: List<Location>) = with(binding) {
        locationsAdapter.locations = locations
        locationsAdapter.notifyDataSetChanged()

        rvLocations.showItAndHideOthers(progressBar, tvErrorMsg)
    }

    private fun renderLoading() = with(binding) {
        progressBar.showItAndHideOthers(rvLocations, tvErrorMsg)
    }

    private fun renderError(error: LocationsViewState.Error) = with(binding) {
        val errorMsg = error.errorMsg
            ?: error.errorMsgId?.let { getString(it) }
            ?: getString(R.string.something_went_wrong)

        tvErrorMsg.text = errorMsg
        tvErrorMsg.showItAndHideOthers(rvLocations, progressBar)
    }
}