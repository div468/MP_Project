package com.example.dragonstats.data.model

data class Encuentro(
    val id: Int,
    val equipo1: String,
    val equipo2: String,
    val fecha: String,
    val hora: String?,
    var resultado: String?,
    val jornada: Int,
    val golesEquipo1: Int? = null,
    val golesEquipo2: Int? = null,
    val penalesEquipo1: Int? = null, // Penales equipo 1
    val penalesEquipo2: Int? = null, // Penales equipo 2
    val eventos: List<PlayerEvent> = emptyList(),
    val grupo: String? = null
) {
    val tieneResultado: Boolean
        get() = golesEquipo1 != null && golesEquipo2 != null
}