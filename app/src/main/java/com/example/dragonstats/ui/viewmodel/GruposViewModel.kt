package com.example.dragonstats.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonstats.data.model.Grupo
import com.example.dragonstats.data.repository.EquipoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class GruposUiState {
    object Loading : GruposUiState()
    data class Success(val grupos: List<Grupo>) : GruposUiState()
    data class Error(val message: String) : GruposUiState()
}

class GruposViewModel(
    private val repository: EquipoRepository = EquipoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<GruposUiState>(GruposUiState.Loading)
    val uiState: StateFlow<GruposUiState> = _uiState.asStateFlow()

    init {
        loadGrupos()
    }

    fun loadGrupos() {
        viewModelScope.launch {
            _uiState.value = GruposUiState.Loading

            repository.getGrupos()
                .onSuccess { grupos ->
                    _uiState.value = GruposUiState.Success(grupos)
                }
                .onFailure { exception ->
                    _uiState.value = GruposUiState.Error(
                        exception.message ?: "Error desconocido al cargar los grupos"
                    )
                }
        }
    }

    fun retry() {
        loadGrupos()
    }
}