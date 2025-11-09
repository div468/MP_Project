package com.example.dragonstats.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonstats.data.repository.EquipoRepository
import com.example.dragonstats.data.model.Equipo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class EquiposUiState {
    object Loading : EquiposUiState()
    data class Success(val equipo: List<Equipo>) : EquiposUiState()
    data class Error(val message: String) : EquiposUiState()
}

class EquiposListadoViewModel(
    private val repository: EquipoRepository = EquipoRepository()
) : ViewModel(){

    private val _uiState = MutableStateFlow<EquiposUiState>(EquiposUiState.Loading)

    val uiState: StateFlow<EquiposUiState> = _uiState.asStateFlow()

    private val _equiposFavoritos = MutableStateFlow<Set<String>>(emptySet())
    val equiposFavoritos: StateFlow<Set<String>> = _equiposFavoritos.asStateFlow()

    init {
        cargarEquipos()
    }

    fun cargarEquipos(){
        viewModelScope.launch {
            _uiState.value = EquiposUiState.Loading

            val result = repository.getEquiposOrdenados()
            _uiState.value = if (result.isSuccess){
                EquiposUiState.Success(result.getOrNull() ?: emptyList())
            }else {
                EquiposUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    fun toggleFavorito(nombreEquipo: String){
        val favoritos = _equiposFavoritos.value.toMutableSet()
        if (favoritos.contains(nombreEquipo)){
            favoritos.remove(nombreEquipo)
        }else{
            favoritos.add(nombreEquipo)
        }
        _equiposFavoritos.value = favoritos
    }
}
