package com.example.dragonstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.dragonstats.ui.components.BottomNavigationBar
import com.example.dragonstats.ui.components.TopAppBarWithTheme
import com.example.dragonstats.ui.navigation.AppNavHost
import com.example.dragonstats.ui.theme.DragonStatsTheme
import com.example.dragonstats.ui.theme.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            DragonStatsTheme(darkTheme = isDarkMode) {
                DragonStatsApp(
                    isDarkMode = isDarkMode,
                    onToggleTheme = { themeViewModel.toggleTheme() }
                )
            }
        }
    }
}

@Composable
fun DragonStatsApp(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBarWithTheme(
                    isDarkMode = isDarkMode,
                    onToggleTheme = onToggleTheme
                )
            },
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }
        ) { paddingValues ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}