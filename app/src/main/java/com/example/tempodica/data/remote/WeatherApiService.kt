package com.example.tempodica.data.remote

import com.example.tempodica.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Serviço responsável pelas chamadas à API de clima (Open-Meteo).
 */
interface WeatherApiService {

    /**
     * Obtém o clima atual e a previsão horária.
     *
     * @param latitude Latitude do local.
     * @param longitude Longitude do local.
     * @param currentWeather Indica se deve retornar dados do clima atual.
     * @param hourly Parâmetros da previsão horária a serem retornados.
     */
    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: Boolean = true,
        @Query("hourly") hourly: String = "temperature_2m,weathercode"
    ): WeatherResponse
}
