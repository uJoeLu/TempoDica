package com.example.tempodica.data.model

import com.google.gson.annotations.SerializedName

/**
 * Representa a resposta completa da API contendo clima atual e previsão horária.
 */
data class WeatherResponse(
    /** Dados do clima atual. */
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather,

    /** Dados da previsão horária. */
    @SerializedName("hourly")
    val hourly: HourlyForecast
)
