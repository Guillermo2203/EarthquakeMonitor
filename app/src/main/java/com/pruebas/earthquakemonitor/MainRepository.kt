package com.pruebas.earthquakemonitor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository {
    suspend fun fetchEarthquakes(): MutableList <Earthquake> {
        return withContext(Dispatchers.IO) {
            val eqResponse = service.getLastHourEarthquakes()
            val eqList = parseEqResult(eqResponse)
            eqList
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