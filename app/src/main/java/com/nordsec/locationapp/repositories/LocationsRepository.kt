package com.nordsec.locationapp.repositories

import android.location.Location
import com.google.gson.Gson
import com.nordsec.locationapp.App
import com.nordsec.locationapp.data.Locations
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


/*
* The class Location repository to read location data and return parsed data
*
* The function getLocationsSortedByDistance should return list of location sorted
* by set custom location (user can choose any location)
*
* The function getLocationsSortedByDistance should rerun list of location sorted by city name
*
*/
class LocationsRepository {

    private val inputStream: InputStream = App.context.assets.open("data.json")
    private val bufferReader = BufferedReader(InputStreamReader(inputStream))
    private val locations: Locations = Gson().fromJson(bufferReader, Locations::class.java)

//    fun getLocationsSortedByDistance(location: Location): Observable<Locations> {
//        TODO return location sorted by distance of city location
//    }
//
//    fun getLocationsSortedByCityName(): Observable<Locations> {
//         TODO return location sorted by distance of city location
//    }
}