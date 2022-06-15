package com.pruebas.earthquakemonitor.main

import androidx.lifecycle.LiveData
import com.pruebas.earthquakemonitor.Earthquake
import com.pruebas.earthquakemonitor.api.EqJsonResponse
import com.pruebas.earthquakemonitor.api.service
import com.pruebas.earthquakemonitor.database.EqDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository (private val database: EqDatabase) {

    suspend fun fetchEarthquakes(sortByMagnitude: Boolean): MutableList<Earthquake> {
        return withContext(Dispatchers.IO) {
            val eqResponse = service.getLastHourEarthquakes()
            val eqList = parseEqResult(eqResponse)

            database.eqDao.insertAll(eqList)

            fetchEqsFromDb(sortByMagnitude)
        }
    }

    suspend fun fetchEqsFromDb(sortByMagnitud: Boolean): MutableList<Earthquake> {
        return withContext(Dispatchers.IO) {
            if (sortByMagnitud) {
                database.eqDao.getEarthquakesWithMagnitude()
            } else {
                database.eqDao.getEarthquakes()
            }
        }
    }

    private fun parseEqResult(eqJsonResponse: EqJsonResponse): MutableList<Earthquake> {
        val eqJsonResponseList = mutableListOf<Earthquake>()
        val featureList = eqJsonResponse.features

        for (feature in featureList) {
            val id = feature.id
            val properties = feature.properties
            val geometry = feature.geometry
            val eq = Earthquake(
                id,
                properties.place,
                properties.mag,
                properties.time,
                geometry.coordinates[0],
                geometry.coordinates[1]
            )
            eqJsonResponseList.add(eq)
        }
        return eqJsonResponseList
    }
}