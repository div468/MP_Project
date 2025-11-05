package com.example.dragonstats.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dragonstats.R
import com.example.dragonstats.data.model.Encuentro
import com.example.dragonstats.ui.viewmodel.CalendarioViewModel
import com.example.dragonstats.ui.viewmodel.CalendarioUiState

@Composable
fun CalendarioScreen(
    onPartidoClick: (Int) -> Unit,
    initialJornada: Int = 1,
    viewModel: CalendarioViewModel = viewModel()
) {
    var selectedJornada by rememberSaveable { mutableIntStateOf(initialJornada) }
    val uiState by viewModel.uiState.collectAsState()

    // Cargar la jornada inicial cuando la pantalla se monta
    LaunchedEffect(Unit) {
        viewModel.loadEncuentros(initialJornada)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 24.dp)
    ) {
        // Header
        Text(
            text = stringResource(id = R.string.calendario_screen_title),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        when (val state = uiState) {
            is CalendarioUiState.Loading -> {
                LoadingStateCalendario()
            }
            is CalendarioUiState.Success -> {
                // Navbar horizontal
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(state.totalJornadas) { index ->
                        val jornadaNum = index + 1
                        JornadaTab(
                            jornadaNumero = jornadaNum,
                            isSelected = selectedJornada == jornadaNum,
                            onClick = {
                                selectedJornada = jornadaNum
                                viewModel.loadEncuentros(jornadaNum)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de encuentros
                if (state.encuentros.isEmpty()) {
                    EmptyStateCalendario()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(state.encuentros) { encuentro ->
                            EncuentroCard(
                                encuentro = encuentro,
                                onPartidoClick = { onPartidoClick(encuentro.id) }
                            )
                        }
                    }
                }
            }
            is CalendarioUiState.Error -> {
                ErrorStateCalendario(
                    message = state.message,
                    onRetry = { viewModel.retry() }
                )
            }
        }
    }
}

@Composable
private fun LoadingStateCalendario() {
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
                text = "Cargando encuentros...",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun EmptyStateCalendario() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay encuentros programados",
            color = Color.Gray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorStateCalendario(
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
                text = "Error al cargar encuentros",
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
private fun JornadaTab(
    jornadaNumero: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF4CAF50) else Color(0xFF1A1A1A)
    val textColor = if (isSelected) Color.White else Color.Gray

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Jornada $jornadaNumero",
            color = textColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun EncuentroCard(encuentro: Encuentro, onPartidoClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPartidoClick() },
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.dark_gray)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Equipo 1
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_equipo_default),
                    contentDescription = null,
                    tint = colorResource(id = R.color.green_calendar),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = encuentro.equipo1,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Información central
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                if (encuentro.tieneResultado) {
                    Text(
                        text = "${encuentro.golesEquipo1} - ${encuentro.golesEquipo2}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Final",
                        color = Color(0xFF4CAF50),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    val horaDisplay = encuentro.hora ?: "--:--"
                    Text(
                        text = horaDisplay,
                        color = if (horaDisplay != "--:--") Color.White else Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = encuentro.fecha,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            // Equipo 2
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = encuentro.equipo2,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_equipo_default),
                    contentDescription = null,
                    tint = colorResource(id = R.color.green_calendar),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}