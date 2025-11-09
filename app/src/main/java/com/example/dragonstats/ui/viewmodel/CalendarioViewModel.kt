package com.example.dragonstats.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonstats.data.model.Encuentro
import com.example.dragonstats.data.repository.EncuentroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CalendarioUiState {
    object Loading : CalendarioUiState()
    data class Success(
        val encuentros: List<Encuentro>,
        val totalJornadas: Int
    ) : CalendarioUiState()
    data class Error(val message: String) : CalendarioUiState()
}

class CalendarioViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CalendarioUiState>(CalendarioUiState.Loading)
    val uiState: StateFlow<CalendarioUiState> = _uiState.asStateFlow()

    private val repository = EncuentroRepository()
    private var currentJornada = 1
    private var totalJornadas = 5

    init {
        viewModelScope.launch {
            // Primero cargar el total de jornadas
            repository.getTotalJornadas().onSuccess { total ->
                totalJornadas = total
            }
            // Luego cargar los encuentros de la jornada 1
            loadEncuentros(1)
        }
    }

    fun loadEncuentros(jornada: Int) {
        viewModelScope.launch {
            _uiState.value = CalendarioUiState.Loading
            currentJornada = jornada

            repository.getEncuentrosPorJornada(jornada).onSuccess { encuentros ->
                _uiState.value = CalendarioUiState.Success(
                    encuentros = encuentros,
                    totalJornadas = totalJornadas
                )
            }.onFailure { exception ->
                _uiState.value = CalendarioUiState.Error(
                    exception.message ?: "Error desconocido"
                )
            }
        }
    }

    fun retry() {
        loadEncuentros(currentJornada)
    }
}