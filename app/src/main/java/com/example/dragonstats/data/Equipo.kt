package com.example.dragonstats.data

import com.example.dragonstats.R

data class Equipo(
    val id: Int,
    val nombre: String,
    val partidos: Int,
    val golDiferencia: Int,
    val puntos: Int,
    val grupo: String,
    val shield: Int = R.drawable.default_shield
)