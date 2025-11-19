package com.example.dragonstats.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dragonstats.R
import com.example.dragonstats.data.model.Goleador
import com.example.dragonstats.ui.viewmodel.EstadisticasUiState
import com.example.dragonstats.ui.viewmodel.EstadisticasViewModel

@Composable
fun EstadisticasScreen(
    viewModel: EstadisticasViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 24.dp)
    ) {
        // Header
        Text(
            text = stringResource(id = R.string.estadisticas_title),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        when (val state = uiState) {
            is EstadisticasUiState.Loading -> {
                LoadingStateEstadisticas()
            }
            is EstadisticasUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    // Sección: Máximo goleador por equipo
                    item {
                        SectionHeader(title = stringResource(id = R.string.max_goleador_equipo))
                    }

                    item {
                        MaxGoleadoresPorEquipoCard(
                            goleadores = state.estadisticas.goleadoresPorEquipo
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Sección: Top 10 Fase de Grupos
                    item {
                        SectionHeader(title = stringResource(id = R.string.top10_fase_grupos))
                    }

                    itemsIndexed(state.estadisticas.top10FaseGrupos) { index, goleador ->
                        GoleadorCard(
                            posicion = index + 1,
                            goleador = goleador
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Sección: Top 10 Fase Finales
                    item {
                        SectionHeader(title = stringResource(id = R.string.top10_fase_finales))
                    }

                    itemsIndexed(state.estadisticas.top10FaseFinales) { index, goleador ->
                        GoleadorCard(
                            posicion = index + 1,
                            goleador = goleador,
                            isPlayoffs = true
                        )
                    }
                }
            }
            is EstadisticasUiState.Error -> {
                ErrorStateEstadisticas(
                    message = state.message,
                    onRetry = { viewModel.retry() }
                )
            }
        }
    }
}

@Composable
private fun LoadingStateEstadisticas() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = Color(0xFF4CAF50),
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Cargando estadísticas...",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ErrorStateEstadisticas(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_stats),
                contentDescription = null,
                tint = Color(0xFFF44336),
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Error al cargar estadísticas",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message,
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text(
                    text = "Reintentar",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color(0xFF4CAF50),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun MaxGoleadoresPorEquipoCard(
    goleadores: Map<String, Goleador>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            goleadores.entries.sortedBy { it.key }.forEach { (equipo, goleador) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = equipo,
                            color = Color(0xFF4CAF50),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = goleador.nombreJugador,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        if (goleador.posicion.isNotEmpty()) {
                            Text(
                                text = goleador.posicion,
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "⚽",
                            fontSize = 16.sp
                        )
                        Text(
                            text = goleador.goles.toString(),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoleadorCard(
    posicion: Int,
    goleador: Goleador,
    isPlayoffs: Boolean = false
) {
    val backgroundColor = when (posicion) {
        1 -> Color(0xFF2D2D2D)
        2 -> Color(0xFF252525)
        3 -> Color(0xFF1F1F1F)
        else -> Color(0xFF1A1A1A)
    }

    val medalColor = when (posicion) {
        1 -> Color(0xFFFFD700) // Oro
        2 -> Color(0xFFC0C0C0) // Plata
        3 -> Color(0xFFCD7F32) // Bronce
        else -> Color.Transparent
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (posicion <= 3) 6.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Posición con medalla
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (posicion <= 3) medalColor else Color(0xFF333333),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = posicion.toString(),
                    color = if (posicion <= 3) Color.Black else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información del jugador
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = goleador.nombreJugador,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = if (posicion <= 3) FontWeight.Bold else FontWeight.Medium
                )
                Text(
                    text = goleador.nombreEquipo,
                    color = if (isPlayoffs) Color(0xFFFFD700) else Color(0xFF4CAF50),
                    fontSize = 13.sp
                )
                if (goleador.posicion.isNotEmpty()) {
                    Text(
                        text = goleador.posicion,
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }

            // Goles
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "⚽",
                    fontSize = 20.sp
                )
                Text(
                    text = goleador.goles.toString(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}