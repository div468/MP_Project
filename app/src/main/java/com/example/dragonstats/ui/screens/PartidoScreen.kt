package com.example.dragonstats.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonstats.data.model.PlayerEvent
import com.example.dragonstats.data.model.EventType
import com.example.dragonstats.data.model.Team
import com.example.dragonstats.data.model.Encuentro
import com.example.dragonstats.ui.viewmodel.PartidoUiState
import com.example.dragonstats.ui.viewmodel.PartidoViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidoDetailsScreen(
    onBackClick: (Int) -> Unit = {},
    matchId: Int = 1,
    totalJornadas: Int = 5, // Pasar desde la navegación
    viewModel: PartidoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(matchId) {
        viewModel.loadPartido(matchId)
    }

    when (val state = uiState) {
        is PartidoUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF4CAF50))
            }
        }
        is PartidoUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.message,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.retry() }) {
                        Text("Reintentar")
                    }
                }
            }
        }
        is PartidoUiState.Success -> {
            PartidoDetailsContent(
                encuentro = state.encuentro,
                onBackClick = onBackClick,
                matchId = matchId,
                totalJornadas = totalJornadas
            )
        }
    }
}

// Función helper para obtener el nombre de la fase
private fun getFaseNombre(jornada: Int, totalJornadas: Int): String {
    return when (jornada) {
        totalJornadas + 1 -> "Cuartos de Final"
        totalJornadas + 2 -> "Semifinal"
        totalJornadas + 3 -> "Final"
        else -> "Jornada $jornada"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PartidoDetailsContent(
    encuentro: Encuentro,
    onBackClick: (Int) -> Unit,
    matchId: Int,
    totalJornadas: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Barra de navegación superior con label de jornada o fase
        TopAppBar(
            title = {
                Text(
                    text = getFaseNombre(encuentro.jornada, totalJornadas),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { onBackClick(matchId) } ,
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Match result section in card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x66424242)),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                MatchHeader(
                    encuentro = encuentro,
                    modifier = Modifier.padding(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Solo mostrar eventos si el partido tiene resultado
            if (encuentro.tieneResultado && encuentro.eventos.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(encuentro.eventos) { event ->
                        EventItem(
                            event = event,
                            homeTeam = encuentro.equipo1,
                            awayTeam = encuentro.equipo2
                        )
                    }
                }
            } else {
                // Si no hay resultado, mostrar mensaje
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (encuentro.hora != null) "Próximo partido" else "Partido pendiente",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        if (encuentro.hora != null) {
                            Text(
                                text = "${encuentro.fecha} - ${encuentro.hora}",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchHeader(
    encuentro: Encuentro,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Resultado principal o información del partido
        if (encuentro.tieneResultado) {
            Text(
                text = "${encuentro.golesEquipo1} - ${encuentro.golesEquipo2}",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            val horaDisplay = encuentro.hora ?: "--:--"
            Text(
                text = horaDisplay,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = encuentro.fecha,
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Equipos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = encuentro.equipo1.first().toString(),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = encuentro.equipo1,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = encuentro.equipo2.first().toString(),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = encuentro.equipo2,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun EventItem(
    event: PlayerEvent,
    homeTeam: String,
    awayTeam: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF1A1A1A),
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (event.team == Team.HOME)
            Arrangement.Start else Arrangement.End
    ) {
        if (event.team == Team.HOME) {
            // Equipo local - alineado a la izquierda
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar del jugador
                PlayerAvatar(playerName = event.playerName)

                Spacer(modifier = Modifier.width(8.dp))

                // Minuto y nombre
                Text(
                    text = "${event.minute} ${event.playerName}",
                    color = Color.White,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Icono del evento
                EventIcon(eventType = event.eventType)
            }
        } else {
            // Equipo visitante - alineado a la derecha
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono del evento
                EventIcon(eventType = event.eventType)

                Spacer(modifier = Modifier.width(8.dp))

                // Minuto y nombre
                Text(
                    text = "${event.minute} ${event.playerName}",
                    color = Color.White,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Avatar del jugador
                PlayerAvatar(playerName = event.playerName)
            }
        }
    }
}

@Composable
private fun PlayerAvatar(playerName: String) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Color(0xFF4CAF50)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = playerName.first().toString(),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EventIcon(eventType: EventType) {
    when (eventType) {
        EventType.GOAL -> {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFF4CAF50), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚽",
                    fontSize = 12.sp
                )
            }
        }
        EventType.YELLOW_CARD -> {
            Box(
                modifier = Modifier
                    .size(16.dp, 20.dp)
                    .background(Color(0xFFFFEB3B), RoundedCornerShape(2.dp))
            )
        }
        EventType.RED_CARD -> {
            Box(
                modifier = Modifier
                    .size(16.dp, 20.dp)
                    .background(Color(0xFFF44336), RoundedCornerShape(2.dp))
            )
        }
    }
}