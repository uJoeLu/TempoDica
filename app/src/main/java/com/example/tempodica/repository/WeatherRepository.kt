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
    private val dao: WeatherDao? = null,
    /**
     * Se true, força a devolução de um WeatherResponse fake (útil para máquinas sem rede).
     */
    private val useFake: Boolean = false,
    /**
     * Se true, tenta a rede e em caso de falha cai para o fake.
     */
    private val useFakeFallback: Boolean = true
) {

    private val api = RetrofitInstance.api

    /**
     * Obtém a previsão do tempo para a latitude e longitude informadas.
     */
    suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double
    ): WeatherResponse {
        if (useFake) return makeFakeResponse()

        return try {
            api.getWeatherForecast(latitude, longitude)
        } catch (e: Exception) {
            if (useFakeFallback) makeFakeResponse() else throw e
        }
    }

    private fun makeFakeResponse(): WeatherResponse {
        // cria um CurrentWeather simples e uma HourlyForecast com alguns pontos
        val current = com.example.tempodica.data.model.CurrentWeather(
            temperature = 25.0,
            windSpeed = 5.0,
            weatherCode = 0
        )

        val times = List(12) { idx ->
            // ISO-like timestamps, mas a tela trata bem strings variadas
            java.time.Instant.now().plusSeconds((idx * 3600).toLong()).toString()
        }
        val temps = List(12) { idx -> 20.0 + idx }
        val codes = List(12) { 0 }

        val hourly = com.example.tempodica.data.model.HourlyForecast(
            time = times,
            temperatures = temps,
            weatherCodes = codes
        )

        return com.example.tempodica.data.model.WeatherResponse(currentWeather = current, hourly = hourly)
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
