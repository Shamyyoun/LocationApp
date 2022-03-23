package com.nordsec.locationapp.data.local

import com.google.gson.Gson
import com.nordsec.locationapp.core.App
import com.nordsec.locationapp.data.dto.LocationsDto
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

class LocationsLocalDataSourceImpl @Inject constructor() : LocationsLocalDataSource {
    private val inputStream: InputStream = App.context.assets.open("data.json")
    private val bufferReader = BufferedReader(InputStreamReader(inputStream))
    private val locationsDto: LocationsDto = Gson().fromJson(bufferReader, LocationsDto::class.java)

    override fun getLocations(): LocationsDto {
        return locationsDto
    }
}