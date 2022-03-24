package com.nordsec.locationapp.presentation.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nordsec.locationapp.R
import com.nordsec.locationapp.databinding.ItemLocationBinding
import com.nordsec.locationapp.domain.models.Location

class LocationsAdapter(var locations: List<Location>) :
    RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

    var onSortByDistanceClickListener: ((Location) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(location: Location) = with(binding) {
            tvName.text = root.context.getString(R.string.city_name_s, location.city)
            tvLat.text = root.context.getString(R.string.lat_d, location.latitude)
            tvLng.text = root.context.getString(R.string.lng_d, location.longitude)

            // Set listeners
            btnSortByDistance.setOnClickListener {
                onSortByDistanceClickListener?.invoke(location)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(locations[position])
    }

    override fun getItemCount(): Int {
        return locations.size
    }
}