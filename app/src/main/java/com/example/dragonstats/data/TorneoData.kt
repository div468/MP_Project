package com.example.dragonstats.data

object TorneoData {
    fun obtenerGrupos(): List<Grupo> {
        return listOf(
            Grupo(
                "Grupo A",
                listOf(
                    Equipo(1, "Dragons FC", 3, 5, 9, "A",),
                    Equipo(2, "Phoenix United", 3, 2, 7, "A"),
                    Equipo(3, "Thunder Bolts", 3, -1, 4, "A"),
                    Equipo(4, "Storm Riders", 3, -3, 3, "A"),
                    Equipo(5, "Lightning Strike", 3, -3, 1, "A")
                )
            ),
            Grupo(
                "Grupo B",
                listOf(
                    Equipo(6, "Fire Hawks", 3, 4, 8, "B"),
                    Equipo(7, "Ice Wolves", 3, 3, 6, "B"),
                    Equipo(8, "Steel Eagles", 3, 1, 5, "B"),
                    Equipo(9, "Wind Warriors", 3, -2, 4, "B"),
                    Equipo(10, "Shadow Hunters", 3, -6, 0, "B")
                )
            ),
            Grupo(
                "Grupo C",
                listOf(
                    Equipo(11, "Flame Guardians", 3, 6, 9, "C"),
                    Equipo(12, "Frost Giants", 3, 2, 6, "C"),
                    Equipo(13, "Rock Titans", 3, 0, 4, "C"),
                    Equipo(14, "Ocean Breakers", 3, -4, 3, "C"),
                    Equipo(15, "Sky Runners", 3, -4, 1, "C")
                )
            )
        )
    }

    // Función para obtener equipos ordenados por puntos y diferencia de goles
    fun obtenerGruposOrdenados(): List<Grupo> {
        return obtenerGrupos().map { grupo ->
            grupo.copy(
                equipos = grupo.equipos.sortedWith(
                    compareByDescending<Equipo> { it.puntos }
                        .thenByDescending { it.golDiferencia }
                )
            )
        }
    }
}