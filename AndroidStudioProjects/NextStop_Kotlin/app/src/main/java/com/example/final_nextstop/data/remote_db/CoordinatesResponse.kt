package com.example.final_nextstop.data.remote_db

data class CoordinatesResponse(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)