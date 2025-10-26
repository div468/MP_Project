package com.example.dragonstats.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonstats.data.model.Equipo
import com.example.dragonstats.data.model.Grupo
import com.example.dragonstats.data.model.Match
import com.example.dragonstats.data.repository.EquipoRepository
import com.example.dragonstats.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class GruposUiState {
    object Loading : GruposUiState()
    data class Success(
        val grupos: List<Grupo>,
        val topTeams: List<Equipo> = emptyList()
    ) : GruposUiState()
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
                    // Obtener los top 16 equipos para el bracket
                    val topTeams = grupos.flatMap { it.equipos.take(2) }.take(16)
                    _uiState.value = GruposUiState.Success(
                        grupos = grupos,
                        topTeams = topTeams
                    )
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

    // Funciones para crear los matches del bracket
    fun createOctavos(topTeams: List<Equipo>): List<Match> {
        return if (topTeams.size >= 16) {
            listOf(
                Match(topTeams[0], topTeams[15], "0 - 0"),
                Match(topTeams[1], topTeams[14], "0 - 0"),
                Match(topTeams[2], topTeams[13], "0 - 0"),
                Match(topTeams[3], topTeams[12], "0 - 0"),
                Match(topTeams[4], topTeams[11], "0 - 0"),
                Match(topTeams[5], topTeams[10], "0 - 0"),
                Match(topTeams[6], topTeams[9], "0 - 0"),
                Match(topTeams[7], topTeams[8], "0 - 0")
            )
        } else {
            createDummyMatches(8)
        }
    }

    fun createQuarterFinals(): List<Match> {
        return listOf(
            Match(createDummyTeam("Ganador O1"), createDummyTeam("Ganador O2"), "0 - 0"),
            Match(createDummyTeam("Ganador O3"), createDummyTeam("Ganador O4"), "0 - 0"),
            Match(createDummyTeam("Ganador O5"), createDummyTeam("Ganador O6"), "0 - 0"),
            Match(createDummyTeam("Ganador O7"), createDummyTeam("Ganador O8"), "0 - 0")
        )
    }

    fun createSemiFinals(): List<Match> {
        return listOf(
            Match(createDummyTeam("Ganador QF1"), createDummyTeam("Ganador QF2"), "0 - 0"),
            Match(createDummyTeam("Ganador QF3"), createDummyTeam("Ganador QF4"), "0 - 0")
        )
    }

    fun createFinal(): Match {
        return Match(
            createDummyTeam("Ganador SF1"),
            createDummyTeam("Ganador SF2"),
            "0 - 0"
        )
    }

    private fun createDummyMatches(count: Int): List<Match> {
        return List(count) { index ->
            Match(
                createDummyTeam("Equipo ${index * 2 + 1}"),
                createDummyTeam("Equipo ${index * 2 + 2}"),
                "0 - 0"
            )
        }
    }

    private fun createDummyTeam(name: String): Equipo {
        return Equipo(
            id = 0,
            nombre = name,
            empatados = 0,
            ganados = 0,
            golesContra = 0,
            golesFavor = 0,
            perdidos = 0,
            puntos = 0,
            grupo = "",
            shield = R.drawable.default_shield
        )
    }
}