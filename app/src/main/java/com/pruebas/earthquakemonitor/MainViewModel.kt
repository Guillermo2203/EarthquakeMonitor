package com.pruebas.earthquakemonitor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class MainViewModel: ViewModel() {
    private var _eqList = MutableLiveData<MutableList<Earthquake>>()
    val eqList: LiveData<MutableList<Earthquake>>
        get() = _eqList

    init {
        viewModelScope.launch {
            _eqList.value = fetchEarthquakes()
        }
    }

    private suspend fun fetchEarthquakes(): MutableList <Earthquake> {
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