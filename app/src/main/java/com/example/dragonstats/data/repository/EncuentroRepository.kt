package com.example.dragonstats.data.repository

import android.util.Log
import com.example.dragonstats.data.model.Encuentro
import com.example.dragonstats.data.model.PlayerEvent
import com.example.dragonstats.data.model.EventType
import com.example.dragonstats.data.model.Team
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EncuentroRepository {
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "EncuentroRepository"

    private fun getDocumentName(jornada: Int, totalJornadas: Int): String {
        return when {
            jornada <= totalJornadas -> "Jornada $jornada"
            jornada == totalJornadas + 1 -> "Cuartos de final"
            jornada == totalJornadas + 2 -> "Semifinales"
            jornada == totalJornadas + 3 -> "Final"
            else -> "Jornada $jornada" 
        }
    }

    suspend fun getBracketMatches(): Result<Map<String, List<Encuentro>>> {
        return try {
            val bracketPhases = listOf("Cuartos de final", "Semifinales", "Final")
            val bracketMap = mutableMapOf<String, List<Encuentro>>()

            for (phase in bracketPhases) {
                val result = getEncuentrosPorFase(phase)
                result.getOrNull()?.let {
                    bracketMap[phase] = it
                }
            }
            Result.success(bracketMap)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getEncuentrosPorFase(fase: String): Result<List<Encuentro>> {
        return try {
            val jornadaDoc = db.collection("tournaments")
                .document("2025")
                .collection("jornadas")
                .document(fase)
                .get()
                .await()

            if (!jornadaDoc.exists()) return Result.success(emptyList())

            val encuentrosArray = jornadaDoc.get("encuentros") as? List<Map<String, Any>> ?: emptyList()
            val jornadaNum = when(fase) {
                "Cuartos de final" -> 6
                "Semifinales" -> 7
                "Final" -> 8
                else -> 0
            }
            val encuentros = encuentrosArray.mapIndexedNotNull { index, encuentroMap ->
                try {
                    val goles1 = (encuentroMap["goles1"] as? Long)?.toInt()
                    val goles2 = (encuentroMap["goles2"] as? Long)?.toInt()
                    val penales1 = (encuentroMap["penalesEquipo1"] as? Long)?.toInt()
                    val penales2 = (encuentroMap["penalesEquipo2"] as? Long)?.toInt()
                    val equipo1 = encuentroMap["equipo1_id"] as? String ?: ""
                    val equipo2 = encuentroMap["equipo2_id"] as? String ?: ""
                    val eventos = parseEventos(encuentroMap["events"] ?: encuentroMap["eventos"], equipo1, equipo2)

                    Encuentro(
                        id = (jornadaNum * 100) + index,
                        equipo1 = equipo1,
                        equipo2 = equipo2,
                        fecha = encuentroMap["date"] as? String ?: "POR DEFINIR",
                        hora = encuentroMap["hora"] as? String,
                        resultado = if (goles1 != null && goles2 != null) "$goles1-$goles2" else null,
                        jornada = jornadaNum,
                        golesEquipo1 = goles1,
                        golesEquipo2 = goles2,
                        penalesEquipo1 = penales1,
                        penalesEquipo2 = penales2,
                        eventos = eventos,
                        grupo = encuentroMap["grupo"] as? String ?: ""
                    )
                } catch (e: Exception) {
                    null
                }
            }
            Result.success(encuentros)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEncuentrosPorJornada(jornada: Int): Result<List<Encuentro>> {
        return try {
            val totalJornadasResult = getTotalJornadas()
            val totalJornadas = totalJornadasResult.getOrNull() ?: 5

            val documentName = getDocumentName(jornada, totalJornadas)

            val jornadaDoc = db.collection("tournaments")
                .document("2025")
                .collection("jornadas")
                .document(documentName)
                .get()
                .await()

            if (!jornadaDoc.exists()) {
                return Result.success(emptyList())
            }

            val encuentrosArray = jornadaDoc.get("encuentros") as? List<Map<String, Any>>

            if (encuentrosArray == null) {
                return Result.success(emptyList())
            }

            val encuentros = encuentrosArray.mapIndexedNotNull { index, encuentroMap ->
                try {
                    val goles1 = (encuentroMap["goles1"] as? Long)?.toInt()
                    val goles2 = (encuentroMap["goles2"] as? Long)?.toInt()
                    val equipo1 = encuentroMap["equipo1_id"] as? String ?: ""
                    val equipo2 = encuentroMap["equipo2_id"] as? String ?: ""
                    
                    val eventos = parseEventos(encuentroMap["events"], equipo1, equipo2)

                    val uniqueId = (jornada * 100) + index

                    Encuentro(
                        id = uniqueId,
                        equipo1 = equipo1,
                        equipo2 = equipo2,
                        fecha = encuentroMap["date"] as? String ?: "POR DEFINIR",
                        hora = encuentroMap["hora"] as? String,
                        resultado = if (goles1 != null && goles2 != null) "$goles1-$goles2" else null,
                        jornada = jornada,
                        golesEquipo1 = goles1,
                        golesEquipo2 = goles2,
                        eventos = eventos,
                        grupo = encuentroMap["grupo"] as? String ?: ""
                    )
                } catch (e: Exception) {
                    null
                }
            }

            Result.success(encuentros)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEncuentroPorId(id: Int): Result<Encuentro?> {
        return try {
            val jornada = id / 100
            val index = id % 100

            if (jornada < 1 || jornada > 15) { 
                return Result.success(null)
            }

            val totalJornadasResult = getTotalJornadas()
            val totalJornadas = totalJornadasResult.getOrNull() ?: 5

            val documentName = getDocumentName(jornada, totalJornadas)

            val jornadaDoc = db.collection("tournaments")
                .document("2025")
                .collection("jornadas")
                .document(documentName)
                .get()
                .await()

            if (!jornadaDoc.exists()) {
                return Result.success(null)
            }

            val encuentrosArray = jornadaDoc.get("encuentros") as? List<Map<String, Any>>

            if (encuentrosArray == null) {
                return Result.success(null)
            }

            if (index < 0 || index >= encuentrosArray.size) {
                return Result.success(null)
            }

            val encuentroMap = encuentrosArray[index]

            val goles1 = (encuentroMap["goles1"] as? Long)?.toInt()
            val goles2 = (encuentroMap["goles2"] as? Long)?.toInt()
            val penales1 = (encuentroMap["penalesEquipo1"] as? Long)?.toInt()
            val penales2 = (encuentroMap["penalesEquipo2"] as? Long)?.toInt()

            val equipo1Nombre = encuentroMap["equipo1_id"] as? String ?: ""
            val equipo2Nombre = encuentroMap["equipo2_id"] as? String ?: ""

            val eventos = parseEventos(encuentroMap["events"], equipo1Nombre, equipo2Nombre)

            val encuentro = Encuentro(
                id = id,
                equipo1 = encuentroMap["equipo1_id"] as? String ?: "",
                equipo2 = encuentroMap["equipo2_id"] as? String ?: "",
                fecha = encuentroMap["date"] as? String ?: "POR DEFINIR",
                hora = encuentroMap["hora"] as? String,
                resultado = if (goles1 != null && goles2 != null) "$goles1-$goles2" else null,
                jornada = jornada,
                golesEquipo1 = goles1,
                golesEquipo2 = goles2,
                penalesEquipo1 = penales1,
                penalesEquipo2 = penales2,
                eventos = eventos,
                grupo = encuentroMap["grupo"] as? String ?: ""
            )

            Result.success(encuentro)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTotalJornadas(): Result<Int> {
        return try {
            val jornadasList = (1..10).toList()
            var totalJornadas = 0

            for (jornada in jornadasList) {
                val doc = db.collection("tournaments")
                    .document("2025")
                    .collection("jornadas")
                    .document("Jornada $jornada")
                    .get()
                    .await()

                if (doc.exists()) {
                    totalJornadas = jornada
                } else {
                    break
                }
            }

            Result.success(if (totalJornadas > 0) totalJornadas else 5)
        } catch (e: Exception) {
            Result.success(5)
        }
    }

    private fun parseEventos(eventosData: Any?, equipo1: String, equipo2: String): List<PlayerEvent> {
        return (eventosData as? List<Map<String, Any>>)?.mapNotNull { eventoMap ->
            try {
                val minute = (eventoMap["minute"] as? Long)?.toInt() ?: 0
                val playerName = eventoMap["player"] as? String ?: ""
                val eventTypeStr = eventoMap["eventType"] as? String ?: ""
                val teamName = eventoMap["team"] as? String ?: ""

                val team = when {
                    teamName.equals(equipo1, ignoreCase = true) -> Team.HOME
                    teamName.equals(equipo2, ignoreCase = true) -> Team.AWAY
                    else -> Team.HOME
                }

                val eventType = when (eventTypeStr.lowercase()) {
                    "goal" -> EventType.GOAL
                    "yellow" -> EventType.YELLOW_CARD
                    "red" -> EventType.RED_CARD
                    else -> EventType.GOAL
                }

                PlayerEvent(
                    minute = "${minute}'",
                    playerName = playerName,
                    eventType = eventType,
                    team = team
                )
            } catch (e: Exception) {
                null
            }
        } ?: emptyList()
    }
}