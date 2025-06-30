package com.example.final_nextstop.data.remote_db

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApiService {
    @GET("direct")
    suspend fun getCoordinatesByCity(
        @Query("q") city: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): Response<List<CoordinatesResponse>>
}