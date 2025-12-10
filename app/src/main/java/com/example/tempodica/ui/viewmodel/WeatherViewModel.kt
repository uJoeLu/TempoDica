package com.example.tempodica.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tempodica.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// ---- UI STATE ----
data class WeatherUiState(
    val isLoading: Boolean = false,
    val temperature: String = "",
    val weatherDescription: String = "",
    val suggestion: String = "",
    val errorMessage: String? = null
)

// ---- UI EVENTS ----
sealed interface UiEvent {
    data class ShowToast(val message: String) : UiEvent
}

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    companion object {
        private const val DEFAULT_LAT = -8.69
        private const val DEFAULT_LON = -35.59
    }

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        // Busca inicial ao criar o ViewModel
        fetchWeather()
    }

    /**
     * Busca o clima do repositório e atualiza o estado.
     * Trata erros de rede e de HTTP separadamente para mensagens mais amigáveis.
     */
    fun fetchWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val response = repository.getWeatherForecast(DEFAULT_LAT, DEFAULT_LON)
                val current = response.currentWeather

                // Em caso de resposta inesperada, proteja-se (defensive coding)
                if (current == null) {
                    val msg = "Resposta inválida do servidor."
                    _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                    _uiEvent.emit(UiEvent.ShowToast(msg))
                    return@launch
                }

                val description = getWeatherDescription(current.weatherCode)
                val suggestion = getSuggestion(current.temperature, current.weatherCode)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        temperature = "${current.temperature}°C",
                        weatherDescription = description,
                        suggestion = suggestion,
                        errorMessage = null
                    )
                }

            } catch (e: IOException) {
                // Erro de I/O: geralmente problema de rede
                val msg = "Falha de rede. Verifique sua conexão."
                _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                _uiEvent.emit(UiEvent.ShowToast(msg))

            } catch (e: HttpException) {
                // Erro HTTP: servidor retornou erro
                val msg = "Erro no servidor (${e.code()}). Tente novamente mais tarde."
                _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                _uiEvent.emit(UiEvent.ShowToast(msg))

            } catch (e: Exception) {
                // Erro genérico
                val msg = "Erro ao buscar dados do clima. Tente novamente."
                _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                _uiEvent.emit(UiEvent.ShowToast(msg))
            }
        }
    }

    // ---- DESCRIÇÕES DE CLIMA ----
    private fun getWeatherDescription(code: Int): String = when (code) {
        0 -> "Céu limpo"
        1, 2, 3 -> "Parcialmente nublado"
        45, 48 -> "Nevoeiro"
        51, 53, 55 -> "Garoa"
        61, 63, 65 -> "Chuva"
        80, 81, 82 -> "Pancadas de chuva"
        95 -> "Tempestade"
        else -> "Condição desconhecida"
    }

    // ---- SUGESTÕES ----
    private fun getSuggestion(temp: Double, code: Int): String {
        return when {
            temp > 28 -> "Muito quente! Beba bastante água e evite sol forte."
            code in setOf(61, 63, 65, 80, 81, 82, 95) ->
                "Dia chuvoso. Que tal ver um filme ou série?"
            code == 0 && temp > 18 ->
                "Clima perfeito para uma caminhada ao ar livre!"
            temp < 12 ->
                "Friozinho! Um chocolate quente cai bem."
            else -> "Aproveite o dia da melhor forma!"
        }
    }
}
