package com.example.dragonstats.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.dragonstats.ui.screens.CalendarioScreen
import com.example.dragonstats.ui.screens.EquiposScreen
import com.example.dragonstats.ui.screens.GruposScreen
import com.example.dragonstats.ui.screens.MatchDetailScreen

sealed class Screen(val route: String, val title: String) {
    object Calendario : Screen("calendario", "Calendario")
    object Grupos : Screen("grupos", "Grupos")
    object Equipos : Screen("equipos", "Equipos")
    object MatchDetail : Screen("match_detail/{matchId}", "Detalle del Partido") {
        fun createRoute(matchId: Int) = "match_detail/$matchId"
    }
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
            CalendarioScreen(navController = navController)
        }

        composable(Screen.Grupos.route) {
            GruposScreen()
        }

        composable(Screen.Equipos.route) {
            EquiposScreen()
        }

        composable(
            route = Screen.MatchDetail.route,
            arguments = listOf(navArgument("matchId") { type = NavType.IntType })
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getInt("matchId") ?: 1
            MatchDetailScreen(
                navController = navController,
                matchId = matchId
            )
        }
    }
}