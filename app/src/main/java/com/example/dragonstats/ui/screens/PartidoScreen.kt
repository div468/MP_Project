package com.example.dragonstats.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dragonstats.R
import com.example.dragonstats.data.model.Encuentro
import com.example.dragonstats.data.model.EventType
import com.example.dragonstats.data.model.PlayerEvent
import com.example.dragonstats.data.model.Team
import com.example.dragonstats.ui.viewmodel.PartidoUiState
import com.example.dragonstats.ui.viewmodel.PartidoViewModel
import com.example.dragonstats.utils.EquipoLogoHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidoDetailsScreen(
    onBackClick: (Int) -> Unit = {},
    matchId: Int = 1,
    totalJornadas: Int = 5,
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
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
        is PartidoUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.onBackground,
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header personalizado sin TopAppBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBackClick(matchId) }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = getFaseNombre(encuentro.jornada, totalJornadas),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                MatchHeader(
                    encuentro = encuentro,
                    modifier = Modifier.padding(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
            } else if (!encuentro.tieneResultado) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (encuentro.hora != null) "Próximo partido" else "Partido pendiente",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        if (encuentro.hora != null) {
                            Text(
                                text = "${encuentro.fecha} - ${encuentro.hora}",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getNombreCorto(nombreCompleto: String): String {
    val partes = nombreCompleto.trim().split(" ")
    return when {
        partes.size >= 2 -> "${partes[0]} ${partes[1]}"
        partes.size == 1 -> partes[0]
        else -> nombreCompleto
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
        if (encuentro.tieneResultado) {
            Text(
                text = "${encuentro.golesEquipo1} - ${encuentro.golesEquipo2}",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )

            if (encuentro.penalesEquipo1 != null && encuentro.penalesEquipo2 != null) {
                Text(
                    text = "(${encuentro.penalesEquipo1}-${encuentro.penalesEquipo2})",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        } else {
            val horaDisplay = encuentro.hora ?: "--:--"
            Text(
                text = horaDisplay,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = encuentro.fecha,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    val logoRes = EquipoLogoHelper.getLogoResource(encuentro.equipo1)
                    if (logoRes != 0 && logoRes != R.drawable.ic_equipo_default) {
                        Image(
                            painter = painterResource(id = logoRes),
                            contentDescription = "Logo ${encuentro.equipo1}",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(70.dp)
                        )
                    } else {
                        Text(
                            text = encuentro.equipo1.first().toString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = encuentro.equipo1.ifEmpty { "Equipo 1" },
                    color = MaterialTheme.colorScheme.onSurface,
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
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    val logoRes = EquipoLogoHelper.getLogoResource(encuentro.equipo2)
                    if (logoRes != 0 && logoRes != R.drawable.ic_equipo_default) {
                        Image(
                            painter = painterResource(id = logoRes),
                            contentDescription = "Logo ${encuentro.equipo2}",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(70.dp)
                        )
                    } else {
                        Text(
                            text = encuentro.equipo2.first().toString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = encuentro.equipo2.ifEmpty { "Equipo 2" },
                    color = MaterialTheme.colorScheme.onSurface,
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
    val nombreCorto = getNombreCorto(event.playerName)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (event.team == Team.HOME)
            Arrangement.Start else Arrangement.End
    ) {
        if (event.team == Team.HOME) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${event.minute} $nombreCorto",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                EventIcon(eventType = event.eventType)
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                EventIcon(eventType = event.eventType)

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$nombreCorto ${event.minute}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
private fun EventIcon(eventType: EventType) {
    when (eventType) {
        EventType.GOAL -> {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color(0xFF4CAF50), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚽",
                    fontSize = 16.sp
                )
            }
        }
        EventType.YELLOW_CARD -> {
            Box(
                modifier = Modifier
                    .size(18.dp, 24.dp)
                    .background(Color(0xFFFFEB3B), RoundedCornerShape(3.dp))
            )
        }
        EventType.RED_CARD -> {
            Box(
                modifier = Modifier
                    .size(18.dp, 24.dp)
                    .background(Color(0xFFF44336), RoundedCornerShape(3.dp))
            )
        }
    }
}