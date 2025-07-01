package com.example.final_nextstop.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.final_nextstop.R
import com.example.final_nextstop.data.local_db.WeatherItemDao
import com.example.final_nextstop.data.model.SearchType
import com.example.final_nextstop.data.model.WeatherItem
import com.example.final_nextstop.data.remote_db.WeatherRemoteDataSource
import com.example.final_nextstop.data.remote_db.WeatherResponse
import com.example.final_nextstop.util.Constants
import com.example.final_nextstop.util.Resource
import com.example.final_nextstop.util.Success
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class WeatherItemRepository @Inject constructor(
    private val weatherDao: WeatherItemDao,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    @ApplicationContext private val context: Context
) {

    fun getAllWeatherItems(): LiveData<List<WeatherItem>> = weatherDao.getAllWeatherItems()

    suspend fun addWeatherItem(item: WeatherItem) = weatherDao.insertWeatherItem(item)

    suspend fun deleteWeatherItem(item: WeatherItem) = weatherDao.deleteWeatherItem(item)

    suspend fun deleteAllWeatherItems() = weatherDao.deleteAllWeatherItems()

    suspend fun updateWeatherItem(item: WeatherItem) = weatherDao.updateWeatherItem(item)

    suspend fun fetchWeatherFromApi(city: String): Resource<Unit> {
        val result = weatherRemoteDataSource.getWeatherByCity(city, Constants.API_KEY)

        return when (val status = result.status) {
            is Success<WeatherResponse> -> {
                val response = status.data!!
                val item = WeatherItem(
                    cityName = response.name,
                    temperature = response.main.temp,
                    description = "",
                    searchType = SearchType.TEMP_BY_COUNTRY
                )
                weatherDao.insertWeatherItem(item)
                Resource.success(Unit)
            }

            is Error -> {
                Resource.error(status.message ?: context.getString(R.string.unknown_error))
            }

            else -> Resource.loading()
        }
    }

    suspend fun fetchFiveDayForecast(city: String): Resource<Unit> {
        val result = weatherRemoteDataSource.getFiveDayForecastByCity(city, Constants.API_KEY)

        return when (val status = result.status) {
            is Success -> {
                val response = status.data!!
                val currentDescription = response.list.firstOrNull()?.weather?.firstOrNull()?.description ?: "N/A"
                val descriptionWithEmoji = getLocalizedWeatherDescription(currentDescription)

                val item = WeatherItem(
                    cityName = response.city.name,
                    temperature = 0.0,
                    description = context.getString(
                        R.string.forecast,
                        descriptionWithEmoji
                    )
                    ,
                    searchType = SearchType.FORECAST_BY_COUNTRY
                )
                weatherDao.insertWeatherItem(item)
                Resource.success(Unit)
            }

            is Error -> Resource.error(status.message ?: context.getString(R.string.error_fetching_forecast))
            else -> Resource.loading()
        }
    }

    suspend fun fetchAirPollutionByCountry(city: String): Resource<Unit> {
        val coordResult = weatherRemoteDataSource.getCoordinates(city, Constants.API_KEY)

        return when (val coordStatus = coordResult.status) {
            is Success -> {
                val coord = coordStatus.data?.firstOrNull()
                    ?: return Resource.error(context.getString(R.string.no_coordinates_found_for_city, city))

                val airResult = weatherRemoteDataSource.getAirPollution(coord.lat, coord.lon, Constants.API_KEY)

                when (val airStatus = airResult.status) {
                    is Success -> {
                        val aqi = airStatus.data?.list?.firstOrNull()?.main?.aqi
                            ?: return Resource.error(context.getString(R.string.unable_to_read_pollution_level_aqi))

                        val level = when (aqi) {
                            1 -> context.getString(R.string.very_good)
                            2 -> context.getString(R.string.good)
                            3 -> context.getString(R.string.moderate)
                            4 -> context.getString(R.string.unhealthy)
                            5 -> context.getString(R.string.dangerous)
                            else -> context.getString(R.string.unknown)
                        }

                        val item = WeatherItem(
                            cityName = city,
                            temperature = 0.0,
                            airPollution = context.getString(R.string.air_pollution_level, level),
                            description = "",
                            searchType = SearchType.POLLUTION_BY_COUNTRY
                        )

                        weatherDao.insertWeatherItem(item)
                        Resource.success(Unit)
                    }

                    is Error -> Resource.error(airStatus.message ?: context.getString(R.string.error_retrieving_air_pollution_data))
                    else -> Resource.loading()
                }
            }

            is Error -> Resource.error(coordStatus.message ?: context.getString(R.string.error_locating_coordinates))
            else -> Resource.loading()
        }
    }

    private fun getLocalizedWeatherDescription(description: String): String {
        val desc = description.lowercase(Locale.ROOT)

        return if (Locale.getDefault().language == "iw") {
            when (desc) {
                "clear sky" -> "שמיים בהירים ☀️"
                "few clouds" -> "מעט עננים 🌤️"
                "scattered clouds", "broken clouds", "overcast clouds" -> "מעונן ☁️"
                "light rain", "drizzle" -> "גשם קל 🌦️"
                "moderate rain", "heavy intensity rain" -> "גשם חזק 🌧️"
                "thunderstorm" -> "סופה 🌩️"
                "snow" -> "שלג ❄️"
                "mist", "fog", "haze" -> "ערפל 🌫️"
                else -> "לא ידוע ❓"
            }
        } else {
            when (desc) {
                "clear sky" -> "Clear sky ☀️"
                "few clouds" -> "Few clouds 🌤️"
                "scattered clouds", "broken clouds", "overcast clouds" -> "Cloudy ☁️"
                "light rain", "drizzle" -> "Light rain 🌦️"
                "moderate rain", "heavy intensity rain" -> "Heavy rain 🌧️"
                "thunderstorm" -> "Thunderstorm 🌩️"
                "snow" -> "Snow ❄️"
                "mist", "fog", "haze" -> "Fog 🌫️"
                else -> "Unknown ❓"
            }
        }
    }
}
