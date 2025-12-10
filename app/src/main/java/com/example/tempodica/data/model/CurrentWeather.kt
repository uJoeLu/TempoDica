package com.example.tempodica.data.model

import com.google.gson.annotations.SerializedName

/**
 * Representa o clima atual retornado pela API.
 *
 * @property temperature Temperatura em graus Celsius.
 * @property windSpeed Velocidade do vento em km/h.
 * @property weatherCode Código numérico que representa o tipo de clima.
 */
data class CurrentWeather(

    @SerializedName("temperature")
    val temperature: Double = 0.0,

    @SerializedName("windspeed")
    val windSpeed: Double = 0.0,

    @SerializedName("weathercode")
    val weatherCode: Int = -1
) {
    /**
     * Retorna uma descrição amigável baseada no código de clima.
     */
    val description: String
        get() = when (weatherCode) {
            0 -> "Céu limpo"
            1, 2, 3 -> "Parcialmente nublado"
            in 45..48 -> "Névoa"
            in 51..67 -> "Chuva fraca a moderada"
            in 71..77 -> "Neve"
            in 80..82 -> "Pancadas de chuva"
            in 95..99 -> "Tempestade"
            else -> "Clima desconhecido"
        }
}
