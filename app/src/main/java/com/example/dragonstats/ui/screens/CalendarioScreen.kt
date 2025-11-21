package com.example.dragonstats.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dragonstats.R
import com.example.dragonstats.data.model.Encuentro
import com.example.dragonstats.ui.viewmodel.CalendarioViewModel
import com.example.dragonstats.ui.viewmodel.CalendarioUiState
import com.example.dragonstats.utils.EquipoLogoHelper

@Composable
fun CalendarioScreen(
    onPartidoClick: (Int) -> Unit,
    initialJornada: Int = 1,
    viewModel: CalendarioViewModel = viewModel()
) {
    var selectedJornada by rememberSaveable { mutableIntStateOf(initialJornada) }
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedJornada) {
        viewModel.loadEncuentros(selectedJornada)
    }

    fun scrollToJornada(jornada: Int, totalJornadas: Int) {
        coroutineScope.launch {
            val index = if (jornada <= totalJornadas) {
                jornada - 1
            } else {
                totalJornadas + (jornada - totalJornadas - 1)
            }
            listState.animateScrollToItem(index)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.calendario_screen_title),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        when (val state = uiState) {
            is CalendarioUiState.Loading -> {
                LoadingStateCalendario()
            }
            is CalendarioUiState.Success -> {
                LazyRow(
                    state = listState,
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
                                scrollToJornada(jornadaNum, state.totalJornadas)
                            }
                        )
                    }

                    item {
                        FaseTab(
                            fase = "Cuartos",
                            jornadaValue = state.totalJornadas + 1,
                            isSelected = selectedJornada == state.totalJornadas + 1,
                            onClick = {
                                selectedJornada = state.totalJornadas + 1
                                scrollToJornada(state.totalJornadas + 1, state.totalJornadas)
                            }
                        )
                    }

                    item {
                        FaseTab(
                            fase = "Semifinal",
                            jornadaValue = state.totalJornadas + 2,
                            isSelected = selectedJornada == state.totalJornadas + 2,
                            onClick = {
                                selectedJornada = state.totalJornadas + 2
                                scrollToJornada(state.totalJornadas + 2, state.totalJornadas)
                            }
                        )
                    }

                    item {
                        FaseTab(
                            fase = "Final",
                            jornadaValue = state.totalJornadas + 3,
                            isSelected = selectedJornada == state.totalJornadas + 3,
                            onClick = {
                                selectedJornada = state.totalJornadas + 3
                                scrollToJornada(state.totalJornadas + 3, state.totalJornadas)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Cargando encuentros...",
                color = MaterialTheme.colorScheme.onBackground,
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
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
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
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Reintentar",
                    color = MaterialTheme.colorScheme.onPrimary
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
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

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
private fun FaseTab(
    fase: String,
    jornadaValue: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFFFD700) else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant

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
            text = fase,
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    val logoRes = EquipoLogoHelper.getLogoResource(encuentro.equipo1)
                    if (logoRes != 0 && logoRes != R.drawable.ic_equipo_default) {
                        Image(
                            painter = painterResource(id = logoRes),
                            contentDescription = "Logo ${encuentro.equipo1}",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_equipo_default),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = encuentro.equipo1,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                if (encuentro.tieneResultado) {
                    Text(
                        text = "${encuentro.golesEquipo1} - ${encuentro.golesEquipo2}",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Final",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    val horaDisplay = encuentro.hora ?: "--:--"
                    Text(
                        text = horaDisplay,
                        color = if (horaDisplay != "--:--") MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = encuentro.fecha,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = encuentro.equipo2,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    val logoRes = EquipoLogoHelper.getLogoResource(encuentro.equipo2)
                    if (logoRes != 0 && logoRes != R.drawable.ic_equipo_default) {
                        Image(
                            painter = painterResource(id = logoRes),
                            contentDescription = "Logo ${encuentro.equipo2}",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_equipo_default),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}