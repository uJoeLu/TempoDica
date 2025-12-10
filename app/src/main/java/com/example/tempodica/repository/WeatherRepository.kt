package com.example.tempodica.repository

import com.example.tempodica.data.remote.RetrofitInstance
import com.example.tempodica.data.model.WeatherResponse

/**
 * Repository responsável por acessar os dados da API de clima.
 */
class WeatherRepository {

    private val api = RetrofitInstance.api

    /**
     * Obtém a previsão do tempo para a latitude e longitude informadas.
     */
    suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double
    ): WeatherResponse {
        return api.getWeatherForecast(latitude, longitude)
    }
}
