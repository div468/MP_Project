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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonstats.R
import com.example.dragonstats.data.model.Encuentro
import com.example.dragonstats.data.model.CalendarioData

@Composable
fun CalendarioScreen(
    onPartidoClick: (Int) -> Unit,
    initialJornada: Int = 1
) {
    val jornadas = CalendarioData.obtenerEncuentros()
    var selectedJornada by remember { mutableIntStateOf(initialJornada) }

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

        // Navbar horizontal tipo FotMob
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(5) { index ->
                val jornadaNum = index + 1
                JornadaTab(
                    jornadaNumero = jornadaNum,
                    isSelected = selectedJornada == jornadaNum,
                    onClick = { selectedJornada = jornadaNum }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de encuentros de la jornada seleccionada
        val jornadaActual = jornadas.find { it.numero == selectedJornada }

        if (jornadaActual != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(jornadaActual.encuentros) { encuentro ->
                    EncuentroCard(
                        encuentro = encuentro,
                        onPartidoClick = { onPartidoClick(encuentro.id) }
                    )
                }
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

            // Información central (resultado si está jugado, sino hora/fecha)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                // Mostrar resultado si el partido ya se jugó
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
                    // Mostrar hora si está programado
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