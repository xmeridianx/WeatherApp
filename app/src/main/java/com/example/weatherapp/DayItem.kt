package com.example.weatherapp

data class DayItem(
    val city: String,
    val date: String,
    val condition: String,
    val imageUrl: String,
    val currentTemperature: String,
    val maxTemperature: String,
    val minTemperature: String,
    val hours: String,
)
