package com.example.tempodica.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade mínima para armazenar uma medição de clima.
 * Guardaremos temperatura, descrição e timestamp.
 */
@Entity(tableName = "measured_weather")
data class MeasuredWeather(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val temperature: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
