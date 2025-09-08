package com.example.dragonstats.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dragonstats.ui.screens.CalendarioScreen
import com.example.dragonstats.ui.screens.EquiposScreen
import com.example.dragonstats.ui.screens.GruposScreen

sealed class Screen(val route: String, val title: String) {
    object Calendario : Screen("calendario", "Calendario")
    object Grupos : Screen("grupos", "Grupos")
    object Equipos : Screen("equipos", "Equipos")
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
            CalendarioScreen()
        }

        composable(Screen.Grupos.route) {
            GruposScreen()
        }

        composable(Screen.Equipos.route) {
            EquiposScreen()
        }
    }
}