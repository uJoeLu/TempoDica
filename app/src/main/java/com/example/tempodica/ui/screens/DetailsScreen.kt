package com.example.tempodica.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempodica.ui.viewmodel.DetailsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Data class para representar a previsão do tempo por hora
data class HourlyWeather(
    val time: String,
    val temperature: Int,
    val icon: ImageVector,
    val description: String
)

fun getWeatherInfo(weatherCode: Int): Pair<ImageVector, String> {
    return when (weatherCode) {
        0 -> Icons.Default.WbSunny to "Céu limpo"
        1, 2, 3 -> Icons.Default.WbCloudy to "Parcialmente nublado"
        45, 48 -> Icons.Default.WbCloudy to "Nevoeiro"
        51, 53, 55 -> Icons.Default.WaterDrop to "Chuvisco"
        61, 63, 65 -> Icons.Default.WaterDrop to "Chuva"
        else -> Icons.Default.WbSunny to "Desconhecido"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    onNavigateUp: () -> Unit,
    detailsViewModel: DetailsViewModel = viewModel()
) {
    val weatherState by detailsViewModel.weatherState.collectAsState()

    LaunchedEffect(Unit) {
        detailsViewModel.fetchWeatherForecast(-23.5505, -46.6333)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Clima por Hora",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            weatherState?.let { weatherResponse ->
                val hourlyData = weatherResponse.hourly
                val now = LocalDateTime.now()
                val startIndex = hourlyData.time.indexOfFirst { LocalDateTime.parse(it) >= now }
                if (startIndex != -1) {
                    val sublist = hourlyData.time.subList(startIndex, (startIndex + 12).coerceAtMost(hourlyData.time.size))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(sublist.size) { index ->
                            val time = hourlyData.time[startIndex + index]
                            val temperature = hourlyData.temperatures[startIndex + index]
                            val weatherCode = hourlyData.weatherCodes[startIndex + index]
                            val (icon, description) = getWeatherInfo(weatherCode)
                            val formattedTime = LocalDateTime.parse(time).format(DateTimeFormatter.ofPattern("HH:mm"))

                            HourlyWeatherItem(weather = HourlyWeather(formattedTime, temperature.toInt(), icon, description))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Hidratação Ideal",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Com base no clima de hoje, recomendamos que você beba pelo menos 2,5 litros de água para se manter hidratado.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun HourlyWeatherItem(weather: HourlyWeather) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = weather.time)
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                imageVector = weather.icon,
                contentDescription = weather.description
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${weather.temperature}°C")
        }
    }
}
