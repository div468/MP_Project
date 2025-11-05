package com.example.dragonstats.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonstats.data.model.Encuentro
import com.example.dragonstats.data.repository.EncuentroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PartidoUiState {
    object Loading : PartidoUiState()
    data class Success(val encuentro: Encuentro) : PartidoUiState()
    data class Error(val message: String) : PartidoUiState()
}

class PartidoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<PartidoUiState>(PartidoUiState.Loading)
    val uiState: StateFlow<PartidoUiState> = _uiState.asStateFlow()

    private val repository = EncuentroRepository()
    private var currentMatchId = 0

    init {
        // No cargamos nada en init, esperamos que se llame loadPartido
    }

    fun loadPartido(matchId: Int) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            currentMatchId = matchId

            repository.getEncuentroPorId(matchId).onSuccess { encuentro ->
                if (encuentro != null) {
                    _uiState.value = PartidoUiState.Success(encuentro)
                } else {
                    _uiState.value = PartidoUiState.Error("Partido no encontrado")
                }
            }.onFailure { exception ->
                _uiState.value = PartidoUiState.Error(
                    exception.message ?: "Error desconocido"
                )
            }
        }
    }

    fun retry() {
        loadPartido(currentMatchId)
    }
}