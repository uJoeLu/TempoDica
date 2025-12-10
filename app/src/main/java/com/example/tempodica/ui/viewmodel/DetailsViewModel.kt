package com.example.tempodica.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tempodica.data.model.WeatherResponse
import com.example.tempodica.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DetailsUiState(
    val isLoading: Boolean = false,
    val weather: WeatherResponse? = null,
    val errorMessage: String? = null
)

class DetailsViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState

    fun fetchWeatherForecast(latitude: Double, longitude: Double) {
        _uiState.value = DetailsUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val response = repository.getWeatherForecast(latitude, longitude)

                _uiState.value = DetailsUiState(
                    isLoading = false,
                    weather = response
                )

            } catch (e: Exception) {
                _uiState.value = DetailsUiState(
                    isLoading = false,
                    errorMessage = "Não foi possível carregar os detalhes: ${e.localizedMessage}"
                )
            }
        }
    }
}
