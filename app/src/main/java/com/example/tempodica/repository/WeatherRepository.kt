package com.example.tempodica.repository

import com.example.tempodica.data.model.WeatherResponse
import com.example.tempodica.data.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository {
    private val apiService = RetrofitInstance.api

    // Usamos withContext(Dispatchers.IO) para garantir que a chamada de rede
    // aconteça em uma thread de background, não bloqueando a UI.
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): Result<WeatherResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCurrentWeather(latitude, longitude)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}