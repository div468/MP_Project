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
private const val CALENDARIO_PARTIDO_DETAILS_ROUTE = "calendario/partido/{matchId}"
private const val GRUPOS_PARTIDO_DETAILS_ROUTE = "grupos/partido/{matchId}"

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
                    navController.navigate("calendario/partido/$matchId")
                },
                initialJornada = jornada
            )
        }

        composable(
            route = CALENDARIO_PARTIDO_DETAILS_ROUTE,
            arguments = listOf(navArgument("matchId") { type = NavType.IntType })
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getInt("matchId") ?: 1
            PartidoDetailsScreen(
                onBackClick = { navController.popBackStack() },
                matchId = matchId
            )
        }

        gruposGraph(navController)

        equiposGraph(navController)
    }
}

fun NavGraphBuilder.gruposGraph(navController: NavHostController) {
    navigation(startDestination = Screen.Grupos.route, route = "grupos_graph") {
        composable(Screen.Grupos.route) {
            GruposScreen(navController = navController)
        }
        composable(
            route = GRUPOS_PARTIDO_DETAILS_ROUTE,
            arguments = listOf(navArgument("matchId") { type = NavType.IntType })
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getInt("matchId") ?: 1
            PartidoDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                matchId = matchId
            )
        }
    }
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
            ListadoScreen(e = equipoId, navController = navController)
        }
    }
}
