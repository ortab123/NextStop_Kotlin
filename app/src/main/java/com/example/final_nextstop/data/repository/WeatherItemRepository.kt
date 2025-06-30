package com.example.final_nextstop.data.repository

import androidx.lifecycle.LiveData
import com.example.final_nextstop.data.local_db.WeatherItemDao
import com.example.final_nextstop.data.model.SearchType
import com.example.final_nextstop.data.model.WeatherItem
import com.example.final_nextstop.data.remote_db.WeatherRemoteDataSource
import com.example.final_nextstop.data.remote_db.WeatherResponse
import com.example.final_nextstop.util.CapitalResolver
import com.example.final_nextstop.util.Constants
import com.example.final_nextstop.util.Loading
import com.example.final_nextstop.util.Resource
import com.example.final_nextstop.util.Success
import com.example.final_nextstop.util.safeCall
import javax.inject.Inject

class WeatherItemRepository @Inject constructor(
    private val weatherDao: WeatherItemDao,
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) {

    fun getAllWeatherItems(): LiveData<List<WeatherItem>> {
        return weatherDao.getAllWeatherItems()
    }

    suspend fun addWeatherItem(item: WeatherItem) {
        weatherDao.insertWeatherItem(item)
    }

    suspend fun deleteWeatherItem(item: WeatherItem) {
        weatherDao.deleteWeatherItem(item)
    }

    suspend fun deleteAllWeatherItems() {
        weatherDao.deleteAllWeatherItems()
    }

    suspend fun updateWeatherItem(item: WeatherItem) {
        weatherDao.updateWeatherItem(item)
    }

    suspend fun fetchWeatherFromApi(city: String): Resource<Unit> {
        val result = weatherRemoteDataSource.getWeatherByCity(city, Constants.API_KEY)

        return when (val status = result.status) {
            is Success<WeatherResponse> -> {
                val response = status.data!!
                val item = WeatherItem(
                    countryName = response.name,
                    temperature = response.main.temp,
                    description = "",
                    searchType = SearchType.TEMP_BY_COUNTRY
                )
                weatherDao.insertWeatherItem(item)
                Resource.success(Unit)
            }

            is Error -> {
                Resource.error(status.message ?: "Unknown error")
            }

            else -> {
                Resource.loading()
            }
        }
    }

    suspend fun fetchFiveDayForecast(city: String): Resource<Unit> {
        val result = weatherRemoteDataSource.getFiveDayForecastByCity(city, Constants.API_KEY)

        return when (val status = result.status) {
            is Success -> {
                val response = status.data!!
                val item = WeatherItem(
                    countryName = response.city.name,
                    temperature = 0.0, // או ממוצע או 0
                    description = "Forecast: ${response.list.firstOrNull()?.weather?.firstOrNull()?.description ?: "N/A"}",
                    searchType = SearchType.FORECAST_BY_COUNTRY
                )
                weatherDao.insertWeatherItem(item)
                Resource.success(Unit)
            }
            is Error -> Resource.error(status.message ?: "שגיאה בבקשת התחזית")
            else -> Resource.loading()
        }
    }

    suspend fun fetchAirPollutionByCountry(city: String): Resource<Unit> {

        val coordResult = weatherRemoteDataSource.getCoordinates(city, Constants.API_KEY)

        return when (val coordStatus = coordResult.status) {
            is Success -> {
                val coord = coordStatus.data?.firstOrNull()
                    ?: return Resource.error("לא נמצאו קואורדינטות לעיר: $city")

                val airResult = weatherRemoteDataSource.getAirPollution(coord.lat, coord.lon, Constants.API_KEY)

                when (val airStatus = airResult.status) {
                    is Success -> {
                        val aqi = airStatus.data?.list?.firstOrNull()?.main?.aqi
                            ?: return Resource.error("לא ניתן לקרוא את רמת הזיהום (AQI)")

                        val level = when (aqi) {
                            1 -> "very good"
                            2 -> "good"
                            3 -> "moderate"
                            4 -> "unhealthy"
                            5 -> "dangerous"
                            else -> "Unknown"
                        }

                        val item = WeatherItem(
                            countryName = city,//need to implement with response
                            temperature = 0.0,
                            airPollution = "Air pollution: ${level}",
                            description = "",
                            searchType = SearchType.POLLUTION_BY_COUNTRY
                        )

                        weatherDao.insertWeatherItem(item)
                        Resource.success(Unit)
                    }

                    is Error -> Resource.error(airStatus.message ?: "שגיאה בקבלת נתוני זיהום אוויר")
                    else -> Resource.loading()
                }
            }

            is Error -> Resource.error(coordStatus.message ?: "שגיאה באיתור קואורדינטות")
            else -> Resource.loading()
        }
    }
}