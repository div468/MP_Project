package com.example.dragonstats.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dragonstats.ui.screens.CalendarioScreen
import com.example.dragonstats.ui.screens.EquiposScreen
import com.example.dragonstats.ui.screens.GruposScreen
import com.example.dragonstats.ui.screens.PartidoDetailsScreen

sealed class Screen(val route: String, val title: String) {
    object Calendario : Screen("calendario", "Calendario")
    object Grupos : Screen("grupos", "Grupos")
    object Equipos : Screen("equipos", "Equipos")
    object PartidoDetails : Screen("partido_details", "Partido Details")
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Calendario.route,
        modifier = modifier
    ) {
        composable(Screen.Calendario.route) {
            CalendarioScreen(onPartidoClick = {
                navController.navigate(Screen.PartidoDetails.route)
            })
        }

        composable(Screen.Grupos.route) {
            GruposScreen()
        }

        composable(Screen.Equipos.route) {
            EquiposScreen()
        }

        composable(Screen.PartidoDetails.route) {
            PartidoDetailsScreen(onBackClick = {
                navController.navigate(Screen.Calendario.route) {
                    popUpTo(Screen.Calendario.route) { inclusive = true }
                }
            })
        }
    }
}