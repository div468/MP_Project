package com.example.dragonstats.data

data class PlayerEvent(
    val minute: String,
    val playerName: String,
    val eventType: EventType,
    val team: Team
)

enum class EventType {
    GOAL, YELLOW_CARD, RED_CARD
}

enum class Team {
    HOME, AWAY
}

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
                        fecha = "02 sep",
                        hora = null,
                        resultado = "4:00 PM",
                        jornada = 1,
                        golesEquipo1 = 4,
                        golesEquipo2 = 0,
                        eventos = listOf(
                            PlayerEvent("3'", "Miguel M.", EventType.GOAL, Team.HOME),
                            PlayerEvent("15'", "Carlos R.", EventType.GOAL, Team.HOME),
                            PlayerEvent("22'", "Andrés P.", EventType.YELLOW_CARD, Team.AWAY),
                            PlayerEvent("35'", "Miguel M.", EventType.GOAL, Team.HOME),
                            PlayerEvent("67'", "Santiago L.", EventType.GOAL, Team.HOME),
                            PlayerEvent("78'", "Pedro V.", EventType.RED_CARD, Team.AWAY)
                        )
                    ),
                    Encuentro(
                        id = 2,
                        equipo1 = "Fire Hawks",
                        equipo2 = "Ice Wolves",
                        fecha = "03 sep",
                        hora = null,
                        resultado = "6:00 PM",
                        jornada = 1,
                        golesEquipo1 = 1,
                        golesEquipo2 = 2,
                        eventos = listOf(
                            PlayerEvent("12'", "Edinson C.", EventType.GOAL, Team.HOME),
                            PlayerEvent("25'", "Roberto K.", EventType.YELLOW_CARD, Team.HOME),
                            PlayerEvent("34'", "Lucas N.", EventType.GOAL, Team.AWAY),
                            PlayerEvent("56'", "Adrián M.", EventType.GOAL, Team.AWAY),
                            PlayerEvent("73'", "Marcos T.", EventType.YELLOW_CARD, Team.AWAY)
                        )
                    ),
                    Encuentro(
                        id = 3,
                        equipo1 = "Flame Guardians",
                        equipo2 = "Frost Giants",
                        fecha = "04 ago",
                        hora = null,
                        resultado = "4:00 PM",
                        jornada = 1,
                        golesEquipo1 = 2,
                        golesEquipo2 = 3,
                        eventos = listOf(
                            PlayerEvent("8'", "Rodrigo B.", EventType.GOAL, Team.HOME),
                            PlayerEvent("14'", "Leandro P.", EventType.RED_CARD, Team.HOME),
                            PlayerEvent("23'", "Santiago S.", EventType.GOAL, Team.AWAY),
                            PlayerEvent("37'", "Agustín A.", EventType.GOAL, Team.AWAY),
                            PlayerEvent("49'", "Carlos P.", EventType.GOAL, Team.HOME),
                            PlayerEvent("67'", "Manuel G.", EventType.GOAL, Team.AWAY),
                            PlayerEvent("82'", "Diego R.", EventType.YELLOW_CARD, Team.AWAY)
                        )
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
                        fecha = "09 sep",
                        hora = "4:00 PM",
                        resultado = null,
                        jornada = 2
                    ),
                    Encuentro(
                        id = 5,
                        equipo1 = "Steel Eagles",
                        equipo2 = "Wind Warriors",
                        fecha = "10 sep",
                        hora = null,
                        resultado = "6:00 PM",
                        jornada = 2
                    ),
                    Encuentro(
                        id = 6,
                        equipo1 = "Rock Titans",
                        equipo2 = "Ocean Breakers",
                        fecha = "11 sep",
                        hora = "2:00 PM",
                        resultado = null,
                        jornada = 2
                    )
                )
            )
        )
    }

    fun obtenerEncuentroPorId(id: Int): Encuentro? {
        return obtenerEncuentros().flatMap { it.encuentros }.find { it.id == id }
    }
}