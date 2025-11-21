package com.example.dragonstats.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonstats.data.repository.EquipoRepository
import com.example.dragonstats.data.model.Equipo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.dragonstats.data.local.FavoritosDataStore

enum class TipoOrden {
    FAVORITOS,
    NOMBREASCENDENTE,
    NOMBREDESCENDENTE,
    PTSASCENDENTE,
    PTSDESCENDENTE;

    fun toText(): String {
        return when (this){
            FAVORITOS -> "Favoritos primero"
            NOMBREASCENDENTE -> "Nombre: Ascendente de A-Z"
            NOMBREDESCENDENTE -> "Nombre: Descendente de Z-A"
            PTSASCENDENTE -> "Puntaje: Menor a mayor"
            PTSDESCENDENTE -> "Puntaje: Mayor a menor"
        }
    }
}

sealed class EquiposUiState {
    object Loading : EquiposUiState()
    data class Success(val equipo: List<Equipo>) : EquiposUiState()
    data class Error(val message: String) : EquiposUiState()
}

class EquiposListadoViewModel(
    application: Application
) : AndroidViewModel(application){
    private val repository: EquipoRepository = EquipoRepository()
    private val favoritosDataStore = FavoritosDataStore(application)

    private val _uiState = MutableStateFlow<EquiposUiState>(EquiposUiState.Loading)
    val uiState: StateFlow<EquiposUiState> = _uiState.asStateFlow()

    private val _equiposFavoritos = MutableStateFlow<Set<String>>(emptySet())
    val equiposFavoritos: StateFlow<Set<String>> = _equiposFavoritos.asStateFlow()

    private val _tipoOrden = MutableStateFlow(TipoOrden.FAVORITOS)
    val tipoOrden: StateFlow<TipoOrden> = _tipoOrden.asStateFlow()

    private var equiposOriginal : List<Equipo> = emptyList()
    init {
        cargarEquipos()
        viewFavoritos()
    }

    private fun viewFavoritos(){
        viewModelScope.launch {
            favoritosDataStore.favoritosFlow.collect { favoritos ->
                _equiposFavoritos.value = favoritos
                reordenarEquipos()
            }
        }
    }
    fun cargarEquipos(){
        viewModelScope.launch {
            _uiState.value = EquiposUiState.Loading

            val result = repository.getEquiposOrdenados()
            if (result.isSuccess){
                equiposOriginal = result.getOrNull() ?: emptyList()
                reordenarEquipos()
            }else {
                _uiState.value = EquiposUiState.Error(
                    result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    fun toggleFavorito(nombreEquipo: String){
        viewModelScope.launch {
            favoritosDataStore.toggleFavorito(nombreEquipo)
        }
    }

    fun cambiardeOrden(nuevoTipo : TipoOrden){
        _tipoOrden.value = nuevoTipo
        reordenarEquipos()
    }

    private fun reordenarEquipos(){
        if (equiposOriginal.isEmpty()){
            return
        }
        val favoritos = _equiposFavoritos.value
        val equiposOrdenados = when(_tipoOrden.value){
            TipoOrden.FAVORITOS -> {
                equiposOriginal.sortedWith (
                    compareByDescending <Equipo> {favoritos.contains(it.nombre)  }
                        .thenByDescending { it.puntos }
                        .thenByDescending { it.golDiferencia }
                )
            }
            TipoOrden.NOMBREASCENDENTE -> {
                equiposOriginal.sortedBy { it.nombre}
            }
            TipoOrden.NOMBREDESCENDENTE -> {
                equiposOriginal.sortedBy { it.nombre }.reversed()
            }
            TipoOrden.PTSDESCENDENTE -> {
                equiposOriginal.sortedWith (
                    compareByDescending {it.puntos}
                )
            }
            TipoOrden.PTSASCENDENTE -> {
                equiposOriginal.sortedWith(
                    compareBy {it.puntos}
                )
            }
        }
        _uiState.value = EquiposUiState.Success(equiposOrdenados)
    }
}
