package com.example.dragonstats.data.repository

import android.util.Log
import com.example.dragonstats.data.model.EstadisticasData
import com.example.dragonstats.data.model.Goleador
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EstadisticasRepository {
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "EstadisticasRepository"

    suspend fun getEstadisticas(): Result<EstadisticasData> {
        return try {
            Log.d(TAG, "=== Obteniendo estadísticas ===")

            // Obtener todos los grupos
            val gruposList = listOf("A", "B", "C", "D")
            val goleadoresPorEquipo = mutableMapOf<String, Goleador>()
            val todosLosGoleadores = mutableListOf<Goleador>()

            for (grupoNombre in gruposList) {
                val teamsSnapshot = db.collection("tournaments")
                    .document("2025")
                    .collection("groups")
                    .document(grupoNombre)
                    .collection("teams")
                    .get()
                    .await()

                teamsSnapshot.documents.forEach { doc ->
                    val nombreEquipo = doc.getString("nombre") ?: ""
                    val jugadoresArray = doc.get("jugadores") as? List<Map<String, Any>> ?: emptyList()

                    // Encontrar el máximo goleador del equipo
                    var maxGoleador: Goleador? = null
                    var maxGoles = -1

                    jugadoresArray.forEach { jugadorMap ->
                        val nombre = jugadorMap["nombre"] as? String ?: ""
                        val goles = (jugadorMap["goles"] as? Long)?.toInt() ?: 0
                        val posicion = jugadorMap["posicion"] as? String ?: ""

                        // Agregar a la lista general
                        todosLosGoleadores.add(
                            Goleador(
                                nombreJugador = nombre,
                                nombreEquipo = nombreEquipo,
                                goles = goles,
                                posicion = posicion
                            )
                        )

                        // Verificar si es el máximo goleador del equipo
                        if (goles > maxGoles) {
                            maxGoles = goles
                            maxGoleador = Goleador(
                                nombreJugador = nombre,
                                nombreEquipo = nombreEquipo,
                                goles = goles,
                                posicion = posicion
                            )
                        }
                    }

                    // Guardar el máximo goleador del equipo
                    maxGoleador?.let {
                        goleadoresPorEquipo[nombreEquipo] = it
                    }
                }
            }

            // Obtener top 10 de fase de grupos (ordenar por goles)
            val top10FaseGrupos = todosLosGoleadores
                .sortedByDescending { it.goles }
                .take(10)

            // Obtener estadísticas de fase finales
            val top10FaseFinales = getGoleadoresFaseFinales()

            Log.d(TAG, "✅ Estadísticas cargadas correctamente")
            Log.d(TAG, "Goleadores por equipo: ${goleadoresPorEquipo.size}")
            Log.d(TAG, "Top 10 fase grupos: ${top10FaseGrupos.size}")
            Log.d(TAG, "Top 10 fase finales: ${top10FaseFinales.size}")

            Result.success(
                EstadisticasData(
                    goleadoresPorEquipo = goleadoresPorEquipo,
                    top10FaseGrupos = top10FaseGrupos,
                    top10FaseFinales = top10FaseFinales
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo estadísticas: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private suspend fun getGoleadoresFaseFinales(): List<Goleador> {
        return try {
            val goleadoresFaseFinales = mutableMapOf<String, Int>()
            val fases = listOf("Cuartos de final", "Semifinales", "Final")

            for (fase in fases) {
                val jornadaDoc = db.collection("tournaments")
                    .document("2025")
                    .collection("jornadas")
                    .document(fase)
                    .get()
                    .await()

                if (!jornadaDoc.exists()) continue

                val encuentrosArray = jornadaDoc.get("encuentros") as? List<Map<String, Any>> ?: emptyList()

                encuentrosArray.forEach { encuentroMap ->
                    val eventosArray = encuentroMap["events"] as? List<Map<String, Any>> ?: emptyList()

                    eventosArray.forEach { evento ->
                        val eventType = evento["eventType"] as? String ?: ""
                        if (eventType.lowercase() == "goal") {
                            val playerName = evento["player"] as? String ?: ""
                            val teamName = evento["team"] as? String ?: ""

                            if (playerName.isNotEmpty()) {
                                val key = "$playerName|$teamName"
                                goleadoresFaseFinales[key] = (goleadoresFaseFinales[key] ?: 0) + 1
                            }
                        }
                    }
                }
            }

            // Convertir a lista de Goleador y ordenar
            goleadoresFaseFinales.entries
                .map { (key, goles) ->
                    val parts = key.split("|")
                    Goleador(
                        nombreJugador = parts.getOrNull(0) ?: "",
                        nombreEquipo = parts.getOrNull(1) ?: "",
                        goles = goles
                    )
                }
                .sortedByDescending { it.goles }
                .take(10)
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo goleadores fase finales: ${e.message}")
            emptyList()
        }
    }
}