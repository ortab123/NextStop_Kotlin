package com.example.final_nextstop.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "weather_items")
data class WeatherItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "cityName")
    val cityName: String,

    @ColumnInfo(name = "airPollution")
    val airPollution: String = "",

    @ColumnInfo(name = "temperature")
    val temperature: Double,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "searchType")
    val searchType: SearchType
) : Parcelable

enum class SearchType {
    TEMP_BY_COUNTRY,
    FORECAST_BY_COUNTRY,
    POLLUTION_BY_COUNTRY
}
