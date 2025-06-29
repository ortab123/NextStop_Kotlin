package com.example.final_nextstop.data.remote_db

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double
)

data class Weather(
    val description: String
)


data class AirPollutionResponse(
    val list: List<AirPollutionData>
)

data class AirPollutionData(
    val main: AirQuality
)

data class AirQuality(
    val aqi: Int
)
