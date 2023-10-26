package com.example.weatherapp.view.model

import com.example.weatherapp.adapters.WeatherModel

data class WeatherState (
    val isLoading: Boolean,
    val responceWeather: WeatherModel,
    val error: Throwable? = null
)