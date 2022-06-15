package com.pruebas.earthquakemonitor.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


/*
interface EqApiService {
    @GET("//all_day.geojson")
    suspend fun getLastHourEarthquakes(): String
}
*/
interface EqApiService {
    @GET("all_hour.geojson")
    suspend fun getLastHourEarthquakes(): EqJsonResponse
}

private var retrofit = Retrofit.Builder()
    .baseUrl("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

var service: EqApiService = retrofit.create<EqApiService>(EqApiService::class.java)