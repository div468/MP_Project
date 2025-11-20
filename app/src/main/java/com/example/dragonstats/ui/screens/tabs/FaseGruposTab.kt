package com.example.dragonstats.ui.screens.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.dragonstats.utils.EquipoLogoHelper

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
    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.grupo) + " " + grupo.nombre,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Equipos",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(180.dp)
                )

                // PJ
                Text(
                    text = "J",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(40.dp)
                )

                // PG
                Text(
                    text = "G",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(40.dp)
                )

                // PE
                Text(
                    text = "E",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(40.dp)
                )

                // PP
                Text(
                    text = "P",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(40.dp)
                )

                // GF
                Text(
                    text = "GF",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(40.dp)
                )

                // GC
                Text(
                    text = "GC",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(40.dp)
                )

                // DG
                Text(
                    text = "DG",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(40.dp)
                )

                // PTS
                Text(
                    text = "PTS",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(50.dp)
                )
            }

            // Lista de equipos con el mismo scroll state
            Column {
                grupo.equipos.forEachIndexed { index, equipo ->
                    EquipoItem(
                        equipo = equipo,
                        posicion = index + 1,
                        scrollState = scrollState
                    )
                }
            }
        }
    }
}

@Composable
private fun EquipoItem(
    equipo: Equipo,
    posicion: Int,
    scrollState: ScrollState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Posición, icono de posición, logo del equipo y nombre (fijo)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(180.dp)
        ) {
            // Posición
            Text(
                text = posicion.toString(),
                color = Color.White,
                fontSize = 13.sp,
                modifier = Modifier.width(20.dp),
                textAlign = TextAlign.Center
            )

            // Logo del equipo
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF333333)),
                contentAlignment = Alignment.Center
            ) {
                val logoRes = EquipoLogoHelper.getLogoResource(equipo.nombre)
                if (logoRes != 0 && logoRes != R.drawable.ic_equipo_default) {
                    Image(
                        painter = painterResource(id = logoRes),
                        contentDescription = "Logo ${equipo.nombre}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Nombre del equipo
            Text(
                text = equipo.nombre,
                color = if (posicion == 1 || posicion == 2) Color(0xFF4CAF50) else Color.White,
                fontSize = 13.sp,
                fontWeight = if (posicion == 1) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

        // PJ - Partidos Jugados
        Text(
            text = equipo.partidos.toString(),
            color = Color.White,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(40.dp)
        )

        // PG - Partidos Ganados
        Text(
            text = equipo.ganados.toString(),
            color = Color(0xFF4CAF50),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(40.dp)
        )

        // PE - Partidos Empatados
        Text(
            text = equipo.empatados.toString(),
            color = Color(0xFFFFEB3B),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(40.dp)
        )

        // PP - Partidos Perdidos
        Text(
            text = equipo.perdidos.toString(),
            color = Color(0xFFF44336),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(40.dp)
        )

        // GF - Goles a Favor
        Text(
            text = equipo.golesFavor.toString(),
            color = Color.White,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(40.dp)
        )

        // GC - Goles en Contra
        Text(
            text = equipo.golesContra.toString(),
            color = Color.White,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(40.dp)
        )

        // DG - Diferencia de Goles
        val dgColor = when {
            equipo.golDiferencia > 0 -> Color(0xFF4CAF50)
            equipo.golDiferencia < 0 -> Color(0xFFF44336)
            else -> Color.White
        }
        Text(
            text = if (equipo.golDiferencia > 0) "+${equipo.golDiferencia}" else equipo.golDiferencia.toString(),
            color = dgColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(40.dp)
        )

        // PTS - Puntos
        Text(
            text = equipo.puntos.toString(),
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(50.dp)
        )
    }
}