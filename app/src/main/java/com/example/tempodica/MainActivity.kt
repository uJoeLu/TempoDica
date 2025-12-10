package com.example.tempodica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.tempodica.ui.navigation.AppNavigation
import com.example.tempodica.ui.theme.TempoDicaTheme

/**
 * Activity principal do aplicativo.
 *
 * - Usa Jetpack Compose para a UI.
 * - Ativa edge-to-edge para que o tema gerencie as barras do sistema.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita layout edge-to-edge (barra de status/navigation controlada pelo tema).
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TempoDicaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // O NavHost dentro do AppNavigation controla qual tela mostrar.
                    AppNavigation()
                }
            }
        }
    }
}
