package com.example.tempodica.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempodica.ui.viewmodel.DetailsViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

// Modelo UI-friendly interno
private data class HourlyItemUi(
    val timeLabel: String,
    val temperature: Int,
    val icon: ImageVector,
    val description: String
)

private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

private fun isoToLocalTimeLabel(iso: String, zone: ZoneId = ZoneId.systemDefault()): String {
    // tenta parse com Instant; se falhar, tenta acrescentar 'Z' como fallback; se ainda falhar, tenta extrair parte HH:mm
    return try {
        val instant = Instant.parse(iso)
        instant.atZone(zone).toLocalTime().format(timeFormatter)
    } catch (_: Exception) {
        try {
            val withZ = if (iso.endsWith("Z")) iso else "${iso}Z"
            Instant.parse(withZ).atZone(zone).toLocalTime().format(timeFormatter)
        } catch (_: Exception) {
            // fallback simples: pegar últimos 5 caracteres se contêm "HH:mm" ou retornar a string inteira
            iso.takeLast(5).takeIf { it.contains(":") } ?: iso
        }
    }
}

private fun weatherInfoFor(code: Int): Pair<ImageVector, String> =
    when (code) {
        0 -> Icons.Default.WbSunny to "Céu limpo"
        1, 2, 3 -> Icons.Default.WbCloudy to "Parcialmente nublado"
        45, 48 -> Icons.Default.WbCloudy to "Nevoeiro"
        51, 53, 55 -> Icons.Default.WaterDrop to "Chuvisco"
        61, 63, 65 -> Icons.Default.WaterDrop to "Chuva"
        else -> Icons.Default.WbSunny to "Desconhecido"
    }

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    onNavigateUp: () -> Unit,
    detailsViewModel: DetailsViewModel = viewModel()
) {
    // usa o estado do ViewModel (DetailsUiState)
    val uiState by detailsViewModel.uiState.collectAsState()

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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Clima por Hora",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.semantics { contentDescription = "Carregando previsão horária" }
                        )
                    }
                }

                else -> {
                    // captura locais para evitar problemas de smart cast
                    val errorMsg = uiState.errorMessage
                    val weather = uiState.weather

                    if (errorMsg != null) {
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.semantics { contentDescription = "Erro: $errorMsg" }
                        )
                    } else if (weather != null) {
                        // monta lista horária de forma segura
                        val hourly = weather.hourly
                        val times = hourly.time
                        val temps = hourly.temperatures
                        val codes = hourly.weatherCodes

                        val size = listOf(times.size, temps.size, codes.size).minOrNull() ?: 0
                        if (size <= 0) {
                            Text(
                                text = "Dados horários não disponíveis",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.semantics { contentDescription = "Dados horários não disponíveis" }
                            )
                        } else {
                            val now = Instant.now()
                            val startIndex = (0 until size).indexOfFirst { i ->
                                try {
                                    Instant.parse(times[i]) >= now
                                } catch (_: Exception) {
                                    false
                                }
                            }.let { if (it == -1) 0 else it }

                            val end = (startIndex + 12).coerceAtMost(size)
                            val items = (startIndex until end).map { i ->
                                val iso = times[i]
                                val temp = temps.getOrNull(i) ?: 0.0
                                val code = codes.getOrNull(i) ?: -1
                                val (icon, desc) = weatherInfoFor(code)
                                HourlyItemUi(
                                    timeLabel = isoToLocalTimeLabel(iso),
                                    temperature = temp.roundToInt(),
                                    icon = icon,
                                    description = desc
                                )
                            }

                            if (items.isEmpty()) {
                                Text(
                                    text = "Nenhuma previsão disponível",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.semantics { contentDescription = "Nenhuma previsão disponível" }
                                )
                            } else {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.semantics { contentDescription = "Previsão horária, deslize para ver mais" }
                                ) {
                                    items(items) { it ->
                                        HourlyCard(it)
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Sem dados",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.semantics { contentDescription = "Sem dados disponíveis" }
                        )
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
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.semantics { contentDescription = "Recomendação de hidratação: ao menos 2,5 litros de água" }
            )
        }
    }
}

@Composable
private fun HourlyCard(item: HourlyItemUi) {
    Card(
        modifier = Modifier.semantics {
            contentDescription = "Previsão às ${item.timeLabel}: ${item.temperature}°C, ${item.description}"
        }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = item.timeLabel, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Icon(imageVector = item.icon, contentDescription = item.description, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${item.temperature}°C", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
