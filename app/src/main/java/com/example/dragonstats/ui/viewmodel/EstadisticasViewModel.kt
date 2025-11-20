package com.example.dragonstats.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonstats.data.model.EstadisticasData
import com.example.dragonstats.data.repository.EstadisticasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class EstadisticasUiState {
    object Loading : EstadisticasUiState()
    data class Success(val estadisticas: EstadisticasData) : EstadisticasUiState()
    data class Error(val message: String) : EstadisticasUiState()
}

class EstadisticasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<EstadisticasUiState>(EstadisticasUiState.Loading)
    val uiState: StateFlow<EstadisticasUiState> = _uiState.asStateFlow()

    private val repository = EstadisticasRepository()

    init {
        loadEstadisticas()
    }

    private fun loadEstadisticas() {
        viewModelScope.launch {
            _uiState.value = EstadisticasUiState.Loading

            repository.getEstadisticas().onSuccess { estadisticas ->
                _uiState.value = EstadisticasUiState.Success(estadisticas)
            }.onFailure { exception ->
                _uiState.value = EstadisticasUiState.Error(
                    exception.message ?: "Error desconocido"
                )
            }
        }
    }

    fun retry() {
        loadEstadisticas()
    }
}