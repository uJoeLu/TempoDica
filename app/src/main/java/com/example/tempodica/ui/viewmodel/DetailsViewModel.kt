package com.example.tempodica.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tempodica.data.model.WeatherResponse
import com.example.tempodica.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository()

    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState: StateFlow<WeatherResponse?> = _weatherState

    fun fetchWeatherForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val response = weatherRepository.getWeatherForecast(latitude, longitude)
                _weatherState.value = response
            } catch (e: Exception) {
                // Tratar o erro de forma apropriada
            }
        }
    }
}
