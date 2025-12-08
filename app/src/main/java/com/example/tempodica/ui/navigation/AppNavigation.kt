package com.example.tempodica.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tempodica.ui.screens.DetailsScreen
import com.example.tempodica.ui.screens.HomeScreen

// Objeto para manter as rotas como constantes, evitando erros de digitação.
object AppDestinations {
    const val HOME_ROUTE = "home"
    const val DETAILS_ROUTE = "details"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME_ROUTE
    ) {
        // Define a HomeScreen como o destino inicial.
        composable(AppDestinations.HOME_ROUTE) {
            HomeScreen(
                onNavigateToDetails = {
                    // Define a ação de navegação para a tela de detalhes.
                    navController.navigate(AppDestinations.DETAILS_ROUTE)
                }
            )
        }

        // Define a rota para a DetailsScreen.
        composable(AppDestinations.DETAILS_ROUTE) {
            DetailsScreen(
                onNavigateUp = {
                    // Define a ação para voltar à tela anterior.
                    navController.navigateUp()
                }
            )
        }
    }
}