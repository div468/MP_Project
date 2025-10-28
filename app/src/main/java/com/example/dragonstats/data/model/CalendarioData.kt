package com.example.dragonstats.data.model

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
            // JORNADA 1
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
                        resultado = null,
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
                        fecha = "02 sep",
                        hora = null,
                        resultado = null,
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
                        fecha = "02 sep",
                        hora = null,
                        resultado = null,
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

            // JORNADA 2
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
                        fecha = "09 sep",
                        hora = "6:00 PM",
                        resultado = null,
                        jornada = 2
                    ),
                    Encuentro(
                        id = 6,
                        equipo1 = "Rock Titans",
                        equipo2 = "Ocean Breakers",
                        fecha = "09 sep",
                        hora = "8:00 PM",
                        resultado = null,
                        jornada = 2
                    )
                )
            ),

            // JORNADA 3
            Jornada(
                numero = 3,
                nombre = "Jornada 3 - Fase grupos",
                encuentros = listOf(
                    Encuentro(
                        id = 7,
                        equipo1 = "Dragons FC",
                        equipo2 = "Lightning Strike",
                        fecha = "16 sep",
                        hora = "4:00 PM",
                        resultado = null,
                        jornada = 3
                    ),
                    Encuentro(
                        id = 8,
                        equipo1 = "Phoenix United",
                        equipo2 = "Thunder Bolts",
                        fecha = "16 sep",
                        hora = "6:00 PM",
                        resultado = null,
                        jornada = 3
                    ),
                    Encuentro(
                        id = 9,
                        equipo1 = "Shadow Hunters",
                        equipo2 = "Fire Hawks",
                        fecha = "16 sep",
                        hora = "8:00 PM",
                        resultado = null,
                        jornada = 3
                    )
                )
            ),

            // JORNADA 4
            Jornada(
                numero = 4,
                nombre = "Jornada 4 - Fase grupos",
                encuentros = listOf(
                    Encuentro(
                        id = 10,
                        equipo1 = "Ice Wolves",
                        equipo2 = "Steel Eagles",
                        fecha = "23 sep",
                        hora = "4:00 PM",
                        resultado = null,
                        jornada = 4
                    ),
                    Encuentro(
                        id = 11,
                        equipo1 = "Flame Guardians",
                        equipo2 = "Sky Runners",
                        fecha = "23 sep",
                        hora = "6:00 PM",
                        resultado = null,
                        jornada = 4
                    ),
                    Encuentro(
                        id = 12,
                        equipo1 = "Frost Giants",
                        equipo2 = "Rock Titans",
                        fecha = "23 sep",
                        hora = "8:00 PM",
                        resultado = null,
                        jornada = 4
                    )
                )
            ),

            // JORNADA 5
            Jornada(
                numero = 5,
                nombre = "Jornada 5 - Fase grupos",
                encuentros = listOf(
                    Encuentro(
                        id = 13,
                        equipo1 = "Storm Riders",
                        equipo2 = "Lightning Strike",
                        fecha = "30 sep",
                        hora = "4:00 PM",
                        resultado = null,
                        jornada = 5
                    ),
                    Encuentro(
                        id = 14,
                        equipo1 = "Wind Warriors",
                        equipo2 = "Ocean Breakers",
                        fecha = "30 sep",
                        hora = "6:00 PM",
                        resultado = null,
                        jornada = 5
                    ),
                    Encuentro(
                        id = 15,
                        equipo1 = "Sky Runners",
                        equipo2 = "Shadow Hunters",
                        fecha = "30 sep",
                        hora = "8:00 PM",
                        resultado = null,
                        jornada = 5
                    )
                )
            )
        )
    }

    fun obtenerEncuentroPorId(id: Int): Encuentro? {
        return obtenerEncuentros().flatMap { it.encuentros }.find { it.id == id }
    }
}