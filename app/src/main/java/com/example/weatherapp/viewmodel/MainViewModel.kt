package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.adapters.WeatherModel

class MainViewModel: ViewModel() {
    val liveData = MutableLiveData<WeatherModel>()
    val liveDataList = MutableLiveData<List<WeatherModel>>()

}