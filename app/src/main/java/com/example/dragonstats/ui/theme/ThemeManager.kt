package com.example.dragonstats.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear el DataStore
private val Context.themeDataStore by preferencesDataStore(name = "theme_preferences")

class ThemeManager(private val context: Context) {

    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    // Flow que emite el estado actual del tema
    val isDarkMode: Flow<Boolean> = context.themeDataStore.data
        .map { preferences ->
            // Por defecto es modo oscuro (true)
            preferences[DARK_MODE_KEY] ?: true
        }

    // Función para alternar entre modo claro y oscuro
    suspend fun toggleTheme() {
        context.themeDataStore.edit { preferences ->
            val current = preferences[DARK_MODE_KEY] ?: true
            preferences[DARK_MODE_KEY] = !current
        }
    }
}