package com.example.tempodica.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempodica.ui.viewmodel.UiEvent
import com.example.tempodica.ui.viewmodel.WeatherViewModel

@Composable
fun HomeScreen(
    viewModel: WeatherViewModel = viewModel(),
    onNavigateToDetails: () -> Unit // Parâmetro para a ação de navegação
) {
    // Coleta o estado da UI do ViewModel. O app se redesenha quando o estado muda.
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // LaunchedEffect escuta eventos únicos do SharedFlow.
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoading) {
                // Exibe um indicador de progresso enquanto os dados são carregados.
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.errorMessage != null) {
                // Exibe uma mensagem de erro se algo falhar.
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.fetchWeather() }) {
                        Text("Tentar novamente")
                    }
                }
            } else {
                // Exibe os dados do tempo quando o carregamento for bem-sucedido.
                WeatherContent(
                    temperature = uiState.temperature,
                    description = uiState.weatherDescription,
                    suggestion = uiState.suggestion,
                    onNavigateToDetails = onNavigateToDetails // Passa a ação para o conteúdo
                )
            }
        }
    }
}

@Composable
fun WeatherContent(
    temperature: String,
    description: String,
    suggestion: String,
    onNavigateToDetails: () -> Unit // Recebe a ação de navegação
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            // Semantics para acessibilidade (TalkBack).
            .semantics(mergeDescendants = true) {
                contentDescription =
                    "A temperatura atual é $temperature. A condição do tempo é $description. Sugestão: $suggestion"
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tempo",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = temperature,
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = description,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Dica para hoje",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = suggestion,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Botão para navegar para a tela de detalhes
        Button(onClick = onNavigateToDetails) {
            Text("Ver mais detalhes")
        }
    }
}