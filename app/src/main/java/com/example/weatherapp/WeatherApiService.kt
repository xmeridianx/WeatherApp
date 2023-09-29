package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("213357a3c37091bf50367c83c51b2b2c") // Здесь укажите путь к конечной точке API
    fun getWeather(
        @Query("q") cityName: String, // Параметр для передачи имени города
        @Query("appid") apiKey: String // Ваш API ключ для доступа к данным о погоде
    ): Call<WeatherData> // WeatherData
}