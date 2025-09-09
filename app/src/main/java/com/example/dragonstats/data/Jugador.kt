package com.example.dragonstats.data

data class Jugador(
    val id: Int,
    val nombre: String,
    val goles: Int,
    val asistencias: Int,
    val posicion: String,
    val equipoId: Int,
)
