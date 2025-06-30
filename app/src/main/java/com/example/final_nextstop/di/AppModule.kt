package com.example.final_nextstop.di

import android.content.Context
import androidx.room.Room
import com.example.final_nextstop.data.local_db.PostDao
import com.example.final_nextstop.data.local_db.AppDataBase
import com.example.final_nextstop.data.local_db.WeatherItemDao
import com.example.final_nextstop.data.remote_db.GeoApiService
import com.example.final_nextstop.data.remote_db.WeatherApiService
import com.example.final_nextstop.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    //room
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase =
        Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "app_db"
        ).build()

    @Provides
    fun providePostDao(database: AppDataBase): PostDao =
        database.postsDao()

    @Provides
    fun provideWeatherItemDao(database: AppDataBase): WeatherItemDao =
        database.weatherItemsDao()

    //retrofit
    @Provides
    @Singleton
    @Named("weather")
    fun provideWeatherRetrofit(gson: Gson) : Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL_FOR_WEATHER)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }


    @Provides
    @Singleton
    @Named("geo")
    fun provideGeoRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_FOR_COUNTRY_NAME)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideGson() : Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideWeatherApiService(@Named("weather")retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGeoApiService(@Named("geo")retrofit: Retrofit): GeoApiService {
        return retrofit.create(GeoApiService::class.java)
    }
}