package com.example.dragonstats.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.dragonstats.ui.screens.CalendarioScreen
import com.example.dragonstats.ui.screens.EquiposScreen
import com.example.dragonstats.ui.screens.GruposScreen
import com.example.dragonstats.ui.screens.ListadoScreen
import com.example.dragonstats.ui.screens.PartidoDetailsScreen

sealed class Screen(val route: String, val title: String) {
    object Calendario : Screen("calendario/{jornada}", "Calendario") {
        fun createRoute(jornada: Int = 1) = "calendario/$jornada"
    }
    object Grupos : Screen("grupos", "Grupos")
    object EquiposGraph : Screen("equipos_graph", "Equipos")
}

private const val EQUIPOS_LIST_ROUTE = "equipos_list"
private const val EQUIPOS_DETAIL_ROUTE = "listadoJ/{equipoID}"

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Calendario.createRoute(1),
        modifier = modifier
    ) {
        composable(
            route = "calendario/{jornada}",
            arguments = listOf(navArgument("jornada") {
                type = NavType.IntType
                defaultValue = 1
            })
        ) { backStackEntry ->
            val jornada = backStackEntry.arguments?.getInt("jornada") ?: 1
            CalendarioScreen(
                onPartidoClick = { matchId ->
                    navController.navigate("partido_details/$matchId")
                },
                initialJornada = jornada
            )
        }

        composable(Screen.Grupos.route) {
            GruposScreen()
        }


        equiposGraph(navController)

        composable(
            route = "partido_details/{matchId}",
            arguments = listOf(navArgument("matchId") { type = NavType.IntType })
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getInt("matchId") ?: 1
            PartidoDetailsScreen(
                onBackClick = { matchId ->
                    // Obtener la jornada del partido y navegar de vuelta
                    val jornada = com.example.dragonstats.data.model.CalendarioData
                        .obtenerEncuentroPorId(matchId)?.jornada ?: 1
                    navController.navigate(Screen.Calendario.createRoute(jornada)) {
                        popUpTo(Screen.Calendario.createRoute(1)) { inclusive = true }
                    }
                },
                matchId = matchId
            )
}

fun NavGraphBuilder.equiposGraph(navController: NavHostController) {
    navigation(startDestination = EQUIPOS_LIST_ROUTE, route = Screen.EquiposGraph.route) {
        composable(EQUIPOS_LIST_ROUTE) {
            EquiposScreen(onEquipoClick = { equipoId ->
                navController.navigate("listadoJ/$equipoId")
            })
        }
        composable(
            route = EQUIPOS_DETAIL_ROUTE,
            arguments = listOf(navArgument("equipoID") { type = NavType.IntType })
        ) { backStackEntry ->
            val equipoId = backStackEntry.arguments?.getInt("equipoID") ?: 0
            ListadoScreen(equipoId, navController)
        }
    }
}
