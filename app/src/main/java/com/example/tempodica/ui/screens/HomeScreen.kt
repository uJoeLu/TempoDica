package com.example.tempodica.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempodica.ui.viewmodel.UiEvent
import com.example.tempodica.ui.viewmodel.WeatherViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetails: () -> Unit
) {
    val context = LocalContext.current
    val factory = com.example.tempodica.ui.viewmodel.WeatherViewModelFactory(context.applicationContext)
    val viewModel: WeatherViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect one-shot events and show a Snackbar (with fallback to Toast).
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    val result = snackbarHostState.showSnackbar(event.message)
                    if (result == SnackbarResult.Dismissed) {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Tempo Agora") })
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            // Usamos LocalConfiguration para medir largura da tela (evita BoxWithConstraints warning)
            val configuration = LocalConfiguration.current
            val screenWidthDp = configuration.screenWidthDp
            val isCompact = screenWidthDp < 600

            // Capture local copies to avoid smart-cast issues later
            val errorMsg = uiState.errorMessage
            val temperature = uiState.temperature
            val description = uiState.weatherDescription
            val suggestion = uiState.suggestion

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .semantics { contentDescription = "Carregando dados do tempo" },
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                errorMsg != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .semantics { contentDescription = "Erro ao carregar: $errorMsg" },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            style = if (isCompact) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.fetchWeather() }) {
                            Text("Tentar novamente")
                        }
                    }
                }

                else -> {
                    WeatherContent(
                        temperature = temperature,
                        description = description,
                        suggestion = suggestion,
                        onNavigateToDetails = onNavigateToDetails,
                        isCompact = isCompact
                    )
                    // exibe últimas medições salvas (se houver)
                    val last by viewModel.lastMeasurements.collectAsState()
                    if (last.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        androidx.compose.material3.Text(text = "Últimas medições:", style = MaterialTheme.typography.titleSmall)
                        androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
                            items(last) { m ->
                                androidx.compose.material3.Card { 
                                    androidx.compose.foundation.layout.Column(modifier = Modifier.padding(8.dp)) {
                                        androidx.compose.material3.Text(text = "${'$'}{m.temperature}°C")
                                        androidx.compose.material3.Text(text = m.description, style = MaterialTheme.typography.bodySmall)
                                        androidx.compose.material3.Text(text = java.time.Instant.ofEpochMilli(m.timestamp).atZone(java.time.ZoneId.systemDefault()).toLocalDateTime().toString(), style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherContent(
    temperature: String,
    description: String,
    suggestion: String,
    onNavigateToDetails: () -> Unit,
    isCompact: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .semantics {
                contentDescription =
                    "Temperatura atual: $temperature. Condição: $description. Dica: $suggestion"
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tempo",
            style = if (isCompact) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = temperature,
            style = if (isCompact) MaterialTheme.typography.displaySmall else MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = if (isCompact) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Dica para hoje",
            style = if (isCompact) MaterialTheme.typography.titleMedium else MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = suggestion,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .semantics { contentDescription = "Sugestão do dia: $suggestion" },
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onNavigateToDetails,
            modifier = Modifier
                .widthIn(min = 160.dp)
                .semantics { contentDescription = "Ver mais detalhes" }
        ) {
            Text("Ver mais detalhes")
        }
    }
}
