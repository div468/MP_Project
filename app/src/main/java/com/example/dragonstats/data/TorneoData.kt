package com.example.dragonstats.data

object TorneoData {
    fun obtenerGrupos(): List<Grupo> {
        return listOf(
            Grupo(
                "Grupo A",
                listOf(
                    Equipo(1, "Equipo 1", 1, 1, 1, "A"),
                    Equipo(2, "Equipo 2", 2, 2, 2, "A"),
                    Equipo(3, "Equipo 3", 3, 3, 3, "A"),
                    Equipo(4, "Equipo 4", 4, 4, 4, "A"),
                    Equipo(5, "Equipo 5", 5, 5, 5, "A")
                )
            ),
            Grupo(
                "Grupo B",
                listOf(
                    Equipo(6, "Equipo 1", 1, 1, 1, "B"),
                    Equipo(7, "Equipo 2", 2, 2, 2, "B"),
                    Equipo(8, "Equipo 3", 3, 3, 3, "B"),
                    Equipo(9, "Equipo 4", 4, 4, 4, "B"),
                    Equipo(10, "Equipo 5", 5, 5, 5, "B")
                )
            ),
            Grupo(
                "Grupo C",
                listOf(
                    Equipo(11, "Equipo 1", 1, 1, 1, "C"),
                    Equipo(12, "Equipo 2", 2, 2, 2, "C")
                )
            )
        )
    }
}
