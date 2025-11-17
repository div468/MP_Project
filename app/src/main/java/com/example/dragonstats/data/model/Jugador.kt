package com.example.dragonstats.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Jugador(
    val id: Int = 0,
    val nombre: String = "",
    val goles: Int = 0,
    val asistencias: Int = 0,
    val posicion: String = "",
) : Parcelable
