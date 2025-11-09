package com.example.dragonstats.data.model

data class Encuentro(
    val id: Int,
    val equipo1: String,
    val equipo2: String,
    val fecha: String,
    val hora: String? = null,
    val resultado: String? = null,
    val jornada: Int,
    val golesEquipo1: Int? = null,
    val golesEquipo2: Int? = null,
    val eventos: List<PlayerEvent> = emptyList(),
    val grupo: String = ""
) {
    val tieneResultado: Boolean
        get() = golesEquipo1 != null && golesEquipo2 != null
}