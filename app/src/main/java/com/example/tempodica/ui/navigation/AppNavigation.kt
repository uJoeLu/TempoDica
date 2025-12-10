package com.example.tempodica.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tempodica.ui.screens.DetailsScreen
import com.example.tempodica.ui.screens.HomeScreen

/**
 * Objeto que centraliza as rotas da aplicação.
 * Facilita manutenção e evita erros de digitação.
 */
object AppDestinations {
    const val HOME = "home"
    const val DETAILS = "details"
}

/**
 * Gerencia a navegação principal do aplicativo.
 */
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME
    ) {

        /** Tela inicial (Home). */
        composable(route = AppDestinations.HOME) {
            HomeScreen(
                onNavigateToDetails = {
                    navController.navigate(AppDestinations.DETAILS)
                }
            )
        }

        /** Tela de detalhes. */
        composable(route = AppDestinations.DETAILS) {
            DetailsScreen(
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}
