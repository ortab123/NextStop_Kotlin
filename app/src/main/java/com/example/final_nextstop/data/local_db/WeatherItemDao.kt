package com.example.final_nextstop.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.final_nextstop.data.model.WeatherItem

@Dao
interface WeatherItemDao {
    @Query("SELECT * FROM weather_items")
    fun getAllWeatherItems(): LiveData<List<WeatherItem>>

    @Query("SELECT * FROM weather_items WHERE id = :id")
    fun getWeatherItem(id: Int): LiveData<WeatherItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherItem(item: WeatherItem)

    @Delete
    suspend fun deleteWeatherItem(item: WeatherItem)

    @Query("DELETE FROM weather_items")
    suspend fun deleteAllWeatherItems()

    @Update
    suspend fun updateWeatherItem(item: WeatherItem)
}