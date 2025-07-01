package com.example.final_nextstop.data.remote_db

import android.content.Context
import com.example.final_nextstop.R
import com.example.final_nextstop.util.Resource
import com.example.final_nextstop.util.safeCall
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRemoteDataSource @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val geoApiService: GeoApiService,
    @ApplicationContext private val context: Context


) : BaseDataSource(context) {
    suspend fun getWeatherByCity(city: String, apiKey: String) =
        getResult { weatherApiService.getWeatherByCity(city, apiKey) }

    suspend fun getFiveDayForecastByCity(city: String, apiKey: String) =
        getResult { weatherApiService.getFiveDayForecastByCity(city, apiKey) }

    suspend fun getAirPollution(
        lat: Double,
        lon: Double,
        apiKey: String
    ): Resource<AirPollutionResponse> {
        return safeCall {
            val response = weatherApiService.getAirPollutionByCoordinates(lat, lon, apiKey)
            if (response.isSuccessful) {
                response.body() ?: throw Exception(context.getString(R.string.response_body_is_null))
            } else {
                throw Exception("API error: ${response.code()} ${response.message()}")
            }
        }
    }

    suspend fun getCoordinates(
        city: String,
        apiKey: String
    ): Resource<List<CoordinatesResponse>> {
        return safeCall {
            val response = geoApiService.getCoordinatesByCity(city, apiKey = apiKey)
            if (response.isSuccessful) {
                response.body() ?: throw Exception(context.getString(R.string.response_body_is_null))
            } else {
                throw Exception("API error: ${response.code()} ${response.message()}")
            }
        }
    }
}