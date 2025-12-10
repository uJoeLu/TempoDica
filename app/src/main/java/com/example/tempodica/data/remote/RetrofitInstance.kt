package com.example.tempodica.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto responsável por fornecer a instância única do Retrofit
 * usada para acessar a API de clima.
 */
object RetrofitInstance {

    private const val BASE_URL = "https://api.open-meteo.com/"

    /** Instância do Retrofit criada de forma lazy para melhor desempenho. */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** Serviço da API exposto como singleton. */
    val api: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}
