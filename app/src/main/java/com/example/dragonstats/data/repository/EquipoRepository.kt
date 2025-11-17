package com.example.dragonstats.data.repository

import com.example.dragonstats.data.model.Equipo
import com.example.dragonstats.data.model.Grupo
import com.example.dragonstats.data.model.Jugador
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EquipoRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getGrupos(): Result<List<Grupo>> {
        return try {
            val grupos = mutableListOf<Grupo>()
            val gruposList = listOf("A", "B", "C", "D")

            for (grupoNombre in gruposList) {
                val teamsSnapshot = db.collection("tournaments")
                    .document("2025")
                    .collection("groups")
                    .document(grupoNombre)
                    .collection("teams")
                    .get()
                    .await()

                val equipos = teamsSnapshot.documents.mapNotNull { doc ->
                    try {
                        //Se extrae el listado de jugadores mediante una lista con map y los datos son convertidos a un objeto jugador
                        val jugadoresArray = doc.get("jugadores") as? List<Map<String, Any>> ?: emptyList()
                        val jugadores = jugadoresArray.mapNotNull { jugadorMap ->
                            try {
                                Jugador(
                                    id = (jugadorMap["id"] as? Long)?.toInt() ?: 0,
                                    nombre = jugadorMap["nombre"] as? String ?: "",
                                    goles = (jugadorMap["goles"] as? Long)?.toInt() ?: 0,
                                    asistencias = (jugadorMap["asistencias"] as? Long)?.toInt() ?: 0,
                                    posicion = jugadorMap["posicion"] as? String ?: ""
                                )
                            } catch (e: Exception) {
                                null
                            }
                        }
                        Equipo(
                            id = doc.id.hashCode(),
                            nombre = doc.getString("nombre") ?: "",
                            empatados = doc.getLong("empatados")?.toInt() ?: 0,
                            ganados = doc.getLong("ganados")?.toInt() ?: 0,
                            golesContra = doc.getLong("golesContra")?.toInt() ?: 0,
                            golesFavor = doc.getLong("golesFavor")?.toInt() ?: 0,
                            perdidos = doc.getLong("perdidos")?.toInt() ?: 0,
                            puntos = doc.getLong("puntos")?.toInt() ?: 0,
                            grupo = grupoNombre,
                            jugadores = jugadores
                        )
                    } catch (e: Exception) {
                        null
                    }
                }

                if (equipos.isNotEmpty()) {
                    grupos.add(
                        Grupo(
                            nombre = grupoNombre,
                            equipos = equipos.sortedWith(
                                compareByDescending<Equipo> { it.puntos }
                                    .thenByDescending { it.golDiferencia }
                            )
                        )
                    )
                }
            }
            Result.success(grupos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getEquiposOrdenados(): Result<List<Equipo>> {
        return try {
            val grupos = getGrupos().getOrNull() ?: emptyList()
            val todosLosEquipos = grupos.flatMap { it.equipos }
                .sortedWith(
                    compareByDescending<Equipo> { it.puntos }
                        .thenByDescending { it.golDiferencia }
                )
            Result.success(todosLosEquipos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}