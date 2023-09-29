package com.example.weatherapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiClient {
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/" // Базовый URL вашего API

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherApiService: WeatherApiService = retrofit.create(WeatherApiService::class.java)

    fun getWeather(cityName: String, apiKey: String): Call<WeatherData> {
        return weatherApiService.getWeather(cityName, apiKey)
    }
}