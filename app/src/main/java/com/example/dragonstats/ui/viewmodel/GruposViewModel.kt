package com.example.dragonstats.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonstats.data.model.Equipo
import com.example.dragonstats.data.model.Grupo
import com.example.dragonstats.data.model.Match
import com.example.dragonstats.data.repository.EquipoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class GruposUiState {
    object Loading : GruposUiState()
    data class Success(val grupos: List<Grupo>, val topTeams: List<Equipo>) : GruposUiState()
    data class Error(val message: String) : GruposUiState()
}

class GruposViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<GruposUiState>(GruposUiState.Loading)
    val uiState: StateFlow<GruposUiState> = _uiState.asStateFlow()

    private val repository = EquipoRepository()

    init {
        fetchGrupos()
    }

    private fun fetchGrupos() {
        viewModelScope.launch {
            _uiState.value = GruposUiState.Loading
            repository.getGrupos().onSuccess { grupos ->
                val topTeams = grupos.flatMap { it.equipos.take(2) }
                _uiState.value = GruposUiState.Success(grupos, topTeams)
            }.onFailure { exception ->
                _uiState.value = GruposUiState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun retry() {
        fetchGrupos()
    }

    fun createQuarterFinals(topTeams: List<Equipo>): List<Match> {
        val teamsByGroup = topTeams.groupBy { it.grupo }

        val teamA1 = teamsByGroup["A"]?.getOrNull(0) ?: Equipo(nombre = "A1")
        val teamA2 = teamsByGroup["A"]?.getOrNull(1) ?: Equipo(nombre = "A2")
        val teamB1 = teamsByGroup["B"]?.getOrNull(0) ?: Equipo(nombre = "B1")
        val teamB2 = teamsByGroup["B"]?.getOrNull(1) ?: Equipo(nombre = "B2")
        val teamC1 = teamsByGroup["C"]?.getOrNull(0) ?: Equipo(nombre = "C1")
        val teamC2 = teamsByGroup["C"]?.getOrNull(1) ?: Equipo(nombre = "C2")
        val teamD1 = teamsByGroup["D"]?.getOrNull(0) ?: Equipo(nombre = "D1")
        val teamD2 = teamsByGroup["D"]?.getOrNull(1) ?: Equipo(nombre = "D2")

        return listOf(
            Match(teamA1, teamD2, "0-0"),
            Match(teamC1, teamB2, "0-0"),
            Match(teamB1, teamC2, "0-0"),
            Match(teamD1, teamA2, "0-0"),
        )
    }

    fun createSemiFinals(): List<Match> {
        val placeholder = Equipo(nombre = "TBD")
        return listOf(
            Match(placeholder, placeholder, "0-0"),
            Match(placeholder, placeholder, "0-0")
        )
    }

    fun createFinal(): Match {
        val placeholder = Equipo(nombre = "TBD")
        return Match(placeholder, placeholder, "0-0")
    }
}