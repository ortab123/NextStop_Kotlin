package com.example.final_nextstop.data.remote_db

data class ForecastResponse(
    val city: City,
    val list: List<ForecastItem>
)

data class City(
    val name: String
    )


data class ForecastItem(
    val dtTxt: String,
    val main: Main,
    val weather: List<Weather>
)
