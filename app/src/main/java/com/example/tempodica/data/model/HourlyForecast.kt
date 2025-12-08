package com.example.tempodica.data.model

import com.google.gson.annotations.SerializedName

data class HourlyForecast(
    val time: List<String>,
    @SerializedName("temperature_2m")
    val temperatures: List<Double>,
    @SerializedName("weathercode")
    val weatherCodes: List<Int>
)
