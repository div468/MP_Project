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
    //Retorna un listado de jugadores predefinidos
    fun obtenerJugadores(): List<Jugador>{
        val jugadores = listOf(
            Jugador(id = 1, nombre = "Lionel Messi", goles = 25, asistencias = 18, posicion = "Delantero", equipoId = 1),
            Jugador(id = 2, nombre = "Cristiano Ronaldo", goles = 22, asistencias = 8, posicion = "Delantero", equipoId = 1),
            Jugador(id = 3, nombre = "Kevin De Bruyne", goles = 8, asistencias = 20, posicion = "Mediocampista", equipoId = 1),
            Jugador(id = 4, nombre = "Virgil van Dijk", goles = 3, asistencias = 2, posicion = "Defensa", equipoId = 1),
            Jugador(id = 5, nombre = "Robert Lewandowski", goles = 28, asistencias = 6, posicion = "Delantero", equipoId = 1),
            Jugador(id = 6, nombre = "Kylian Mbappé", goles = 24, asistencias = 10, posicion = "Delantero", equipoId = 1),
            Jugador(id = 7, nombre = "Mohamed Salah", goles = 20, asistencias = 12, posicion = "Delantero", equipoId = 1),
            Jugador(id = 8, nombre = "Karim Benzema", goles = 19, asistencias = 9, posicion = "Delantero", equipoId = 1),
            Jugador(id = 9, nombre = "Erling Haaland", goles = 30, asistencias = 5, posicion = "Delantero", equipoId = 1),
            Jugador(id = 10, nombre = "Luka Modrić", goles = 5, asistencias = 15, posicion = "Mediocampista", equipoId = 1),
            Jugador(id = 11, nombre = "Sergio Ramos", goles = 4, asistencias = 3, posicion = "Defensa", equipoId = 1),
            Jugador(id = 12, nombre = "Manuel Neuer", goles = 0, asistencias = 1, posicion = "Portero", equipoId = 1)
        )
        return jugadores
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

    //Funcion para obtener lista de equipos ordenados por puntos y diferencia de goles
    fun obtenerEquiposOrdenados(): List<Equipo>{
        return obtenerGrupos().flatMap {it.equipos}
            .sortedWith(
            compareByDescending<Equipo>{it.puntos}
                .thenByDescending {it.golDiferencia}
        )
    }

}