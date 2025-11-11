package com.example.dragonstats.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Equipo(
    val id: Int = 0,
    val nombre: String = "",
    val empatados: Int = 0,
    val ganados: Int = 0,
    val golesContra: Int = 0,
    val golesFavor: Int = 0,
    val perdidos: Int = 0,
    val puntos: Int = 0,
    val grupo: String = "",
    val jugadores: List<Jugador> = emptyList()
): Parcelable {

    val partidos: Int
        get() = ganados + empatados + perdidos

    val golDiferencia: Int
        get() = golesFavor - golesContra

    constructor() : this(
        id = 0,
        nombre = "",
        empatados = 0,
        ganados = 0,
        golesContra = 0,
        golesFavor = 0,
        perdidos = 0,
        puntos = 0,
        grupo = ""
    )
}