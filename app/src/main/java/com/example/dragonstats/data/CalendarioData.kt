package com.example.dragonstats.data

object CalendarioData {

    data class Jornada(
        val numero: Int,
        val nombre: String,
        val encuentros: List<Encuentro>
    )

    fun obtenerEncuentros(): List<Jornada> {
        return listOf(
            Jornada(
                numero = 1,
                nombre = "Jornada 1 - Fase grupos",
                encuentros = listOf(
                    Encuentro(
                        id = 1,
                        equipo1 = "Dragons FC",
                        equipo2 = "Phoenix United",
                        fecha = "09 sep",
                        hora = "2:00 PM",
                        jornada = 1
                    ),
                    Encuentro(
                        id = 2,
                        equipo1 = "Fire Hawks",
                        equipo2 = "Ice Wolves",
                        fecha = "10 sep",
                        hora = "6:00 PM",
                        jornada = 1
                    ),
                    Encuentro(
                        id = 3,
                        equipo1 = "Flame Guardians",
                        equipo2 = "Frost Giants",
                        fecha = "11 sep",
                        hora = "4:00 PM",
                        jornada = 1
                    )
                )
            ),
            Jornada(
                numero = 2,
                nombre = "Jornada 2 - Fase grupos",
                encuentros = listOf(
                    Encuentro(
                        id = 4,
                        equipo1 = "Thunder Bolts",
                        equipo2 = "Storm Riders",
                        fecha = "16 sep",
                        hora = "4:00 PM",
                        jornada = 2
                    ),
                    Encuentro(
                        id = 5,
                        equipo1 = "Steel Eagles",
                        equipo2 = "Wind Warriors",
                        fecha = "17 sep",
                        resultado = "2:00 PM",
                        jornada = 2
                    ),
                    Encuentro(
                        id = 6,
                        equipo1 = "Rock Titans",
                        equipo2 = "Ocean Breakers",
                        fecha = "18 sep",
                        hora = "6:00 PM",
                        jornada = 2
                    )
                )
            )
        )
    }
}