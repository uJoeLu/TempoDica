package com.example.tempodica.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tempodica.repository.WeatherRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Estado da UI: representa tudo que a tela pode exibir.
data class WeatherUiState(
    val isLoading: Boolean = false,
    val temperature: String = "",
    val weatherDescription: String = "",
    val suggestion: String = "",
    val errorMessage: String? = null
)

// Eventos únicos: para ações que acontecem uma vez (ex: mostrar Toast).
sealed interface UiEvent {
    data class ShowToast(val message: String) : UiEvent
}

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    // StateFlow para o estado da UI. É privado para só o ViewModel poder alterá-lo.
    private val _uiState = MutableStateFlow(WeatherUiState())
    // Expõe o StateFlow como somente leitura para a UI.
    val uiState = _uiState.asStateFlow()

    // SharedFlow para eventos únicos.
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        // Inicia um fluxo que busca o clima repetidamente
        startWeatherUpdates()
    }

    private fun startWeatherUpdates() {
        viewModelScope.launch {
            // Cria um fluxo que emite um valor a cada 15 minutos (900.000 milissegundos)
            flow {
                while (true) {
                    emit(Unit) // Emite um valor para acionar a busca
                    delay(600_000L) // Aguarda 10 minutos
                }
            }.collect {
                // Cada vez que o fluxo emitir, chama a função para buscar o clima
                fetchWeather()
            }
        }
    }

    fun fetchWeather() {
        // viewModelScope garante que a coroutine será cancelada se o ViewModel for destruído.
        viewModelScope.launch {
            // Atualiza o estado para "carregando".
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Coordenadas fixas de Palmares para o exemplo.
            val latitude = -8.69
            val longitude = -35.59

            try {
                val weatherResponse = repository.getWeatherForecast(latitude, longitude)
                val weather = weatherResponse.currentWeather
                val description = getWeatherDescription(weather.weatherCode)
                val suggestion = getSuggestion(weather.temperature, weather.weatherCode)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        temperature = "${weather.temperature}°C",
                        weatherDescription = description,
                        suggestion = suggestion
                    )
                }
            } catch (e: IOException) {
                // Erro de rede: Ocorreu um problema de conexão com o servidor.
                // Isso acontece se o dispositivo estiver offline ou a rede instável.
                val errorMessage = "Erro de conexão. Verifique sua internet."
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
                _uiEvent.emit(UiEvent.ShowToast(errorMessage))

            } catch (e: HttpException) {
                // Erro HTTP: O servidor respondeu, mas com um código de erro (ex: 404, 500).
                // Isso significa que a requisição em si estava malformada ou o servidor teve um problema.
                val errorMessage = "O serviço está indisponível no momento. Tente mais tarde."
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
                _uiEvent.emit(UiEvent.ShowToast(errorMessage))

            } catch (e: Exception) {
                // Erro genérico: Qualquer outra exceção inesperada que não foi capturada acima.
                // Pode ser um erro de parsing do JSON (se a resposta do servidor mudar), etc.
                val errorMessage = "Ocorreu um erro inesperado."
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
                _uiEvent.emit(UiEvent.ShowToast(errorMessage))
            }
        }
    }

    // Lógica para traduzir o código do clima em uma descrição.
    private fun getWeatherDescription(code: Int): String {
        return when (code) {
            0 -> "Céu limpo"
            1, 2, 3 -> "Parcialmente nublado"
            45, 48 -> "Nevoeiro"
            51, 53, 55 -> "Garoa"
            61, 63, 65 -> "Chuva"
            80, 81, 82 -> "Pancadas de chuva"
            95 -> "Tempestade"
            else -> "Condição desconhecida"
        }
    }

    // Lógica para gerar sugestões baseadas no clima.
    private fun getSuggestion(temp: Double, code: Int): String {
        return when {
            temp > 28 -> "Muito quente! Beba bastante água e procure uma sombra."
            code in listOf(61, 63, 65, 80, 81, 82, 95) -> "Está chovendo. Que tal um filme em casa?"
            code == 0 && temp > 18 -> "Dia de sol! Ótimo para uma caminhada no parque."
            temp < 12 -> "Está frio! Perfeito para um chocolate quente."
            else -> "Aproveite o dia!"
        }
    }
}
