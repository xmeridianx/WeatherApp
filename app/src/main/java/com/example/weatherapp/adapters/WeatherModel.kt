package com.example.weatherapp.adapters

data class WeatherModel (
    val city: String,
    val time: String,
    val imageUrl: String,
    val temperature: String,
    val maxTemperature: String,
    val minTemperature: String
)