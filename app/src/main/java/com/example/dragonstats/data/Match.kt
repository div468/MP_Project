package com.example.dragonstats.data

import android.media.audiofx.DynamicsProcessing.Eq

data class Match(
    val teamA: Equipo,
    val teamB: Equipo,
    val score: String,
)