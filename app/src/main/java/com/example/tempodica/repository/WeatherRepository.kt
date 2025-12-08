package com.example.tempodica.repository

import com.example.tempodica.data.remote.RetrofitInstance
import com.example.tempodica.data.model.WeatherResponse

class WeatherRepository {
    private val weatherApiService = RetrofitInstance.api

    suspend fun getWeatherForecast(latitude: Double, longitude: Double): WeatherResponse {
        return weatherApiService.getWeatherForecast(latitude, longitude)
    }
}
