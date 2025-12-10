package com.example.tempodica.data.model

import com.google.gson.annotations.SerializedName

/**
 * Representa a previsão horária contendo listas de horários, temperaturas e códigos de clima.
 */
data class HourlyForecast(
    /** Lista de horários correspondentes às previsões. */
    val time: List<String>,

    /** Lista de temperaturas por hora. */
    @SerializedName("temperature_2m")
    val temperatures: List<Double>,

    /** Lista de códigos meteorológicos por hora. */
    @SerializedName("weathercode")
    val weatherCodes: List<Int>
)
