package com.example.dragonstats.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonstats.data.model.Encuentro
import com.example.dragonstats.data.model.Equipo
import com.example.dragonstats.data.model.Grupo
import com.example.dragonstats.data.repository.EncuentroRepository
import com.example.dragonstats.data.repository.EquipoRepository
import com.example.dragonstats.ui.screens.GruposTab
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class GruposUiState {
    object Loading : GruposUiState()
    data class Success(
        val grupos: List<Grupo>,
        val topTeams: List<Equipo>,
        val bracketMatches: Map<String, List<Encuentro>> = emptyMap()
    ) : GruposUiState()
    data class Error(val message: String) : GruposUiState()
}

class GruposViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<GruposUiState>(GruposUiState.Loading)
    val uiState: StateFlow<GruposUiState> = _uiState.asStateFlow()

    private val _selectedTab = MutableStateFlow(GruposTab.FASE_GRUPOS)
    val selectedTab: StateFlow<GruposTab> = _selectedTab.asStateFlow()

    private val equipoRepository = EquipoRepository()
    private val encuentroRepository = EncuentroRepository()

    init {
        fetchData()
    }

    fun onTabSelected(tab: GruposTab) {
        _selectedTab.value = tab
    }

    private fun fetchData() {
        viewModelScope.launch {
            _uiState.value = GruposUiState.Loading

            val gruposDeferred = async { equipoRepository.getGrupos() }
            val bracketDeferred = async { encuentroRepository.getBracketMatches() }

            val gruposResult = gruposDeferred.await()
            val bracketResult = bracketDeferred.await()

            if (gruposResult.isSuccess && bracketResult.isSuccess) {
                val grupos = gruposResult.getOrThrow()
                val bracketMatches = bracketResult.getOrThrow()
                val topTeams = grupos.flatMap { it.equipos.take(2) }
                _uiState.value = GruposUiState.Success(grupos, topTeams, bracketMatches)
            } else {
                val errorMessage = (gruposResult.exceptionOrNull() ?: bracketResult.exceptionOrNull())?.message ?: "Error desconocido"
                _uiState.value = GruposUiState.Error(errorMessage)
            }
        }
    }

    fun retry() {
        fetchData()
    }
}