package com.example.dragonstats.data.local

import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import kotlinx.coroutines.flow.Flow
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

//DataStore para guardar los equipos favoritos en cache
// At the top level of your kotlin file:
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favoritos")

class FavoritosDataStore(private val context: Context){
    companion object{
        private val FAVORITOS_KEY = stringSetPreferencesKey("equipos_favoritos")
    }

    val favoritosFlow: Flow<Set<String>> = context.dataStore.data
        .map{preferences ->
            preferences[FAVORITOS_KEY] ?: emptySet()
        }

    //Para alternar el estado del favorito
    suspend fun toggleFavorito(nombreEquipo: String){
        context.dataStore.edit {preferences ->
            val favoritosActuales = preferences[FAVORITOS_KEY]?.toMutableSet() ?: mutableSetOf()
            if (favoritosActuales.contains(nombreEquipo)){
                favoritosActuales.remove(nombreEquipo)
            }else{
                favoritosActuales.add(nombreEquipo)
            }
            preferences[FAVORITOS_KEY] = favoritosActuales
        }
    }

}