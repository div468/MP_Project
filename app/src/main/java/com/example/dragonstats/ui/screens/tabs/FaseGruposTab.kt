package com.example.dragonstats.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.dragonstats.data.model.Equipo
import com.example.dragonstats.data.model.Grupo
import com.example.dragonstats.ui.viewmodel.GruposUiState
import com.example.dragonstats.ui.viewmodel.GruposViewModel

@Composable
fun FaseGruposTab(
    viewModel: GruposViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (val state = uiState) {
            is GruposUiState.Loading -> {
                LoadingState()
            }
            is GruposUiState.Success -> {
                GruposContent(grupos = state.grupos)
            }
            is GruposUiState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = { viewModel.retry() }
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
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
                text = "Cargando grupos...",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ErrorState(
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
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = null,
                tint = Color(0xFFF44336),
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Error al cargar los datos",
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
private fun GruposContent(grupos: List<Grupo>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(grupos) { grupo ->
            GrupoCard(grupo = grupo)
        }
    }
}

@Composable
private fun GrupoCard(grupo: Grupo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header del grupo con columnas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = grupo.nombre,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                // Headers de columnas
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.header_partidos),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.header_gol_diferencia),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.header_puntos),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

            // Lista de equipos
            Column {
                grupo.equipos.forEachIndexed { index, equipo ->
                    EquipoItem(
                        equipo = equipo,
                        posicion = index + 1
                    )
                }
            }
        }
    }
}

@Composable
private fun EquipoItem(
    equipo: Equipo,
    posicion: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Posición, icono y nombre
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Posición
            Text(
                text = posicion.toString(),
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 8.dp)
            )

            // Icono del equipo con color según posición
            val iconColor = when (posicion) {
                1, 2 -> Color(0xFF4CAF50) // Clasifican (verde)
                3, 4 -> Color(0xFFFF9800) // En peligro (naranja)
                else -> Color(0xFFF44336) // Eliminados (rojo)
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_equipo_default),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 12.dp)
            )

            // Nombre del equipo
            Text(
                text = equipo.nombre,
                color = if (posicion == 1) Color(0xFF4CAF50) else Color.White,
                fontSize = 14.sp,
                fontWeight = if (posicion == 1) FontWeight.Bold else FontWeight.Normal
            )
        }

        // Stats
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = equipo.partidos.toString(),
                color = Color.White,
                fontSize = 14.sp
            )

            Text(
                text = if (equipo.golDiferencia > 0) "+${equipo.golDiferencia}" else equipo.golDiferencia.toString(),
                color = Color.White,
                fontSize = 14.sp
            )

            Text(
                text = equipo.puntos.toString(),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}