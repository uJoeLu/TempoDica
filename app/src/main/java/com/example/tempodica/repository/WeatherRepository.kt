package com.example.tempodica.repository

import com.example.tempodica.data.local.MeasuredWeather
import com.example.tempodica.data.local.WeatherDao
import com.example.tempodica.data.remote.RetrofitInstance
import com.example.tempodica.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repository responsável por acessar os dados da API de clima e, de forma opcional,
 * persistir medições localmente via Room.
 */
class WeatherRepository(
    private val dao: WeatherDao? = null
) {

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

    // Persistir uma medicao (se o dao foi injetado)
    suspend fun saveMeasurement(temperature: Double, description: String) {
        dao?.insert(MeasuredWeather(temperature = temperature, description = description))
        // manter apenas 5 registros: apagar mais antigas
        dao?.deleteOlderThanKeep(5)
    }

    // Retornar as últimas N medições como Flow
    fun getLastMeasurements(limit: Int = 5): Flow<List<MeasuredWeather>>? = dao?.getLastMeasurements(limit)
}
import com.example.tempodica.data.local.MeasuredWeather
import com.example.tempodica.data.local.WeatherDao
import com.example.tempodica.data.remote.RetrofitInstance
import com.example.tempodica.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repository responsável por acessar os dados da API de clima e, de forma opcional,
 * persistir medições localmente via Room.
 */
class WeatherRepository(
    private val dao: WeatherDao? = null
) {

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

    // Persistir uma medicao (se o dao foi injetado)
    suspend fun saveMeasurement(temperature: Double, description: String) {
        dao?.insert(MeasuredWeather(temperature = temperature, description = description))
        // manter apenas 5 registros: apagar mais antigas
        dao?.deleteOlderThanKeep(5)
    }

    // Retornar as últimas N medições como Flow
    fun getLastMeasurements(limit: Int = 5): Flow<List<MeasuredWeather>>? = dao?.getLastMeasurements(limit)
}
