package com.example.tempodica.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert
    suspend fun insert(measure: MeasuredWeather)

    // Retorna as últimas N medições ordenadas por timestamp desc
    @Query("SELECT * FROM measured_weather ORDER BY timestamp DESC LIMIT :limit")
    fun getLastMeasurements(limit: Int): Flow<List<MeasuredWeather>>

    // Opcional: para keep simple, limpar mais antigas se quiser
    @Query("DELETE FROM measured_weather WHERE id IN (SELECT id FROM measured_weather ORDER BY timestamp DESC LIMIT -1 OFFSET :keep)")
    suspend fun deleteOlderThanKeep(keep: Int)
}
