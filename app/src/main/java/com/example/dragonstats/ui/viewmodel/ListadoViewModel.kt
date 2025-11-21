package com.example.dragonstats.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonstats.data.local.FavoritosDataStore
import com.example.dragonstats.data.model.Equipo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ListadoUiState{
    object Loading : ListadoUiState()
    data class Success(val equipo: Equipo) : ListadoUiState()
    data class Error(val message: String): ListadoUiState()
}

class ListadoViewModel(
    application: Application,
    private val equipo: Equipo
): AndroidViewModel(application){

    private val favoritosDataStore = FavoritosDataStore(application)
    private val _uiState = MutableStateFlow<ListadoUiState>(ListadoUiState.Loading)
    val uiState: StateFlow<ListadoUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    init{
        loadEquipoData()
        loadFavoriteStatus()
    }

    private fun loadEquipoData(){
        _uiState.value = ListadoUiState.Success(equipo)
    }
    private fun loadFavoriteStatus(){
        viewModelScope.launch {
            favoritosDataStore.favoritosFlow.collect { favoritos ->
                _isFavorite.value = favoritos.contains(equipo.nombre)
            }
        }
    }

    fun toogleFavorite(){
        viewModelScope.launch {
            favoritosDataStore.toggleFavorito(equipo.nombre)
        }
    }
}