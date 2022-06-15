package com.pruebas.earthquakemonitor.main

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.pruebas.earthquakemonitor.Earthquake
import com.pruebas.earthquakemonitor.api.ApiResponseStatus
import com.pruebas.earthquakemonitor.database.getDatabase
import kotlinx.coroutines.*
import java.net.UnknownHostException

class MainViewModel (application: Application, private val sortType: Boolean): AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    private val _status = MutableLiveData<ApiResponseStatus>()
    val status: LiveData<ApiResponseStatus>
        get() = _status

    private var _eqList = MutableLiveData<MutableList<Earthquake>>()
    val eqList: LiveData<MutableList<Earthquake>>
        get() = _eqList

    init {
        realoadEqsFromDb(sortType)
    }

    private fun realoadEqs() {
        viewModelScope.launch {
            try {
                _status.value = ApiResponseStatus.LOADING
                _eqList.value = repository.fetchEarthquakes(sortType)
                _status.value = ApiResponseStatus.DONE
            } catch (e: UnknownHostException) {
                _status.value = ApiResponseStatus.ERROR
                Log.d(TAG, "No internet connection", e)
            }
        }
    }

    fun realoadEqsFromDb(sortByMag: Boolean) {
        viewModelScope.launch {
            _eqList.value = repository.fetchEarthquakes(sortByMag)
            if (_eqList.value.isNullOrEmpty()) {
                realoadEqs()
            }
        }
    }
}