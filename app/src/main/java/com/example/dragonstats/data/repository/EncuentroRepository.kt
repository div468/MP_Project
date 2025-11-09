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

    suspend fun getEncuentrosPorJornada(jornada: Int): Result<List<Encuentro>> {
        return try {
            Log.d(TAG, "=== INICIO: Buscando encuentros para Jornada $jornada ===")

            val jornadaDoc = db.collection("tournaments")
                .document("2025")
                .collection("jornadas")
                .document("Jornada $jornada")
                .get()
                .await()

            if (!jornadaDoc.exists()) {
                Log.w(TAG, "Documento de Jornada $jornada no existe")
                return Result.success(emptyList())
            }

            Log.d(TAG, "Documento de Jornada encontrado: ${jornadaDoc.id}")

            val encuentrosArray = jornadaDoc.get("encuentros") as? List<Map<String, Any>>

            if (encuentrosArray == null) {
                Log.w(TAG, "No se encontró el array de encuentros")
                return Result.success(emptyList())
            }

            Log.d(TAG, "Encuentros encontrados en array: ${encuentrosArray.size}")

            // Generar ID único: jornada * 100 + índice
            // Ej: Jornada 1, índice 0 → ID 100
            // Ej: Jornada 2, índice 0 → ID 200
            val encuentros = encuentrosArray.mapIndexedNotNull { index, encuentroMap ->
                try {
                    Log.d(TAG, "--- Procesando encuentro $index ---")

                    val goles1 = (encuentroMap["goles1"] as? Long)?.toInt()
                    val goles2 = (encuentroMap["goles2"] as? Long)?.toInt()
                    val equipo1 = encuentroMap["equipo1_id"] as? String ?: ""
                    val equipo2 = encuentroMap["equipo2_id"] as? String ?: ""
                    val fecha = encuentroMap["date"] as? String ?: "POR DEFINIR"
                    val grupo = encuentroMap["grupo"] as? String ?: ""

                    Log.d(TAG, "equipo1: $equipo1 vs equipo2: $equipo2 ($goles1-$goles2)")

                    val eventos = parseEventos(encuentroMap["eventos"])

                    // ID único: jornada * 100 + índice
                    val uniqueId = (jornada * 100) + index

                    Encuentro(
                        id = uniqueId,
                        equipo1 = equipo1,
                        equipo2 = equipo2,
                        fecha = fecha,
                        hora = encuentroMap["hora"] as? String,
                        resultado = if (goles1 != null && goles2 != null) "$goles1-$goles2" else null,
                        jornada = jornada,
                        golesEquipo1 = goles1,
                        golesEquipo2 = goles2,
                        eventos = eventos,
                        grupo = grupo
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Error procesando encuentro $index: ${e.message}")
                    e.printStackTrace()
                    null
                }
            }

            Log.d(TAG, "=== FIN: Total encuentros procesados: ${encuentros.size} ===")
            Result.success(encuentros)
        } catch (e: Exception) {
            Log.e(TAG, "❌❌❌ ERROR CRÍTICO obteniendo encuentros: ${e.message}")
            Log.e(TAG, "Tipo de error: ${e.javaClass.simpleName}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getEncuentroPorId(id: Int): Result<Encuentro?> {
        return try {
            Log.d(TAG, "=== Buscando encuentro con ID: $id ===")

            // Extraer jornada e índice del ID único
            // ID 205 → jornada 2, índice 5
            val jornada = id / 100
            val index = id % 100

            Log.d(TAG, "ID $id decodificado → Jornada: $jornada, Índice: $index")

            if (jornada < 1 || jornada > 10) {
                Log.w(TAG, "⚠️ Jornada inválida: $jornada")
                return Result.success(null)
            }

            val jornadaDoc = db.collection("tournaments")
                .document("2025")
                .collection("jornadas")
                .document("Jornada $jornada")
                .get()
                .await()

            if (!jornadaDoc.exists()) {
                Log.w(TAG, "⚠️ Jornada $jornada no existe")
                return Result.success(null)
            }

            val encuentrosArray = jornadaDoc.get("encuentros") as? List<Map<String, Any>>

            if (encuentrosArray == null) {
                Log.w(TAG, "⚠️ No hay encuentros en Jornada $jornada")
                return Result.success(null)
            }

            // Buscar el encuentro por índice
            if (index < 0 || index >= encuentrosArray.size) {
                Log.w(TAG, "⚠️ Índice $index fuera de rango (tamaño: ${encuentrosArray.size})")
                return Result.success(null)
            }

            val encuentroMap = encuentrosArray[index]

            Log.d(TAG, "✅ Encuentro encontrado en Jornada $jornada, índice $index")

            val goles1 = (encuentroMap["goles1"] as? Long)?.toInt()
            val goles2 = (encuentroMap["goles2"] as? Long)?.toInt()

            val eventos = parseEventos(encuentroMap["eventos"])

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
                eventos = eventos,
                grupo = encuentroMap["grupo"] as? String ?: ""
            )

            Result.success(encuentro)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error crítico buscando encuentro: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getTotalJornadas(): Result<Int> {
        return try {
            Log.d(TAG, "=== Calculando total de jornadas ===")

            val jornadasList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            var totalJornadas = 0

            for (jornada in jornadasList) {
                val doc = db.collection("tournaments")
                    .document("2025")
                    .collection("jornadas")
                    .document("Jornada $jornada")
                    .get()
                    .await()

                if (doc.exists()) {
                    Log.d(TAG, "Jornada $jornada existe")
                    totalJornadas = jornada
                } else {
                    Log.d(TAG, "Jornada $jornada no existe")
                    break
                }
            }

            Log.d(TAG, "✅ Total de jornadas encontradas: $totalJornadas")
            Result.success(if (totalJornadas > 0) totalJornadas else 5)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error obteniendo total de jornadas: ${e.message}")
            Result.success(5)
        }
    }

    private fun parseEventos(eventosData: Any?): List<PlayerEvent> {
        return (eventosData as? List<Map<String, Any>>)?.mapNotNull { eventoMap ->
            try {
                PlayerEvent(
                    minute = eventoMap["minute"] as? String ?: "",
                    playerName = eventoMap["playerName"] as? String ?: "",
                    eventType = when (eventoMap["eventType"] as? String) {
                        "GOAL" -> EventType.GOAL
                        "YELLOW_CARD" -> EventType.YELLOW_CARD
                        "RED_CARD" -> EventType.RED_CARD
                        else -> EventType.GOAL
                    },
                    team = when (eventoMap["team"] as? String) {
                        "HOME" -> Team.HOME
                        "AWAY" -> Team.AWAY
                        else -> Team.HOME
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error parseando evento: ${e.message}")
                null
            }
        } ?: emptyList()
    }
}