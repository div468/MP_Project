package com.example.dragonstats.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dragonstats.R
import com.example.dragonstats.ui.navigation.Screen

data class BottomNavItem(
    val screen: Screen,
    val iconRes: Int,
    val titleRes: Int
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(Screen.Calendario, R.drawable.ic_calendar, R.string.app_name),
        BottomNavItem(Screen.Grupos, R.drawable.ic_schedule, R.string.app_name),
        BottomNavItem(Screen.Equipos, R.drawable.ic_teams, R.string.app_name)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF4CAF50) // green_primary
    ) {
        items.forEach { item ->
            val isCalendario = item.screen == Screen.Calendario
            val isSelected = if (isCalendario) {
                currentRoute?.startsWith("calendario") == true
            } else {
                currentRoute == item.screen.route
            }

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.screen.title
                    )
                },
                label = {
                    Text(text = item.screen.title)
                },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        val route = if (isCalendario) {
                            Screen.Calendario.createRoute(1)
                        } else {
                            item.screen.route
                        }

                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color(0xFF888888),
                    unselectedTextColor = Color(0xFF888888),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}