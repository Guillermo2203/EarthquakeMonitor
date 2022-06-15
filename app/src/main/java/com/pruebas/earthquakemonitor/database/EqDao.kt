package com.pruebas.earthquakemonitor.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pruebas.earthquakemonitor.Earthquake

@Dao
interface EqDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(eqList: MutableList<Earthquake>)

    @Query("SELECT * FROM earthquakes")
    fun getEarthquakes(): MutableList<Earthquake>

    @Query("SELECT * FROM earthquakes order by magnitude ASC")
    fun getEarthquakesWithMagnitude(): MutableList<Earthquake>

    /*
    @UPDATE
    fun updateEarthquake(earthquake: Earthquake)

    @DELETE
    fun deleteEarthquake(earthquake: Earthquake)

     */
}