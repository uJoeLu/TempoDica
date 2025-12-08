package com.example.tempodica.data.model

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("temperature")
    val temperature: Double,

    @SerializedName("windspeed")
    val windspeed: Double,

    @SerializedName("weathercode")
    val weatherCode: Int
)