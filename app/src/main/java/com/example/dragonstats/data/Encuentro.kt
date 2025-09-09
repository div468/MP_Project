package com.example.dragonstats.data

data class Encuentro(
    val id: Int,
    val equipo1: String,
    val equipo2: String,
    val fecha: String,
    val hora: String? = null,
    val resultado: String? = null,
    val jornada: Int
)