package com.example.weatherapp.data

import com.example.weatherapp.data.model.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast.json")
    fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String
    ): Call<WeatherData>
}