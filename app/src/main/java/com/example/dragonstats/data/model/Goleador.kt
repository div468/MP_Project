package com.example.dragonstats.data.model

data class Goleador(
    val nombreJugador: String,
    val nombreEquipo: String,
    val goles: Int,
    val posicion: String = ""
)

data class EstadisticasData(
    val goleadoresPorEquipo: Map<String, Goleador>,
    val top10FaseGrupos: List<Goleador>,
    val top10FaseFinales: List<Goleador>
)