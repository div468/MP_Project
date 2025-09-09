package com.example.dragonstats.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dragonstats.R
import com.example.dragonstats.data.Encuentro
import com.example.dragonstats.data.CalendarioData
import com.example.dragonstats.ui.navigation.Screen

@Composable
fun CalendarioScreen(navController: NavController) {
    val encuentros = CalendarioData.obtenerEncuentros()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 24.dp)
    ) {
        // Header
        Text(
            text = "Próximos encuentros",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        // Lista de encuentros
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(encuentros) { jornada ->
                JornadaCard(
                    jornada = jornada,
                    onMatchClick = { matchId ->
                        navController.navigate(Screen.MatchDetail.createRoute(matchId))
                    }
                )
            }
        }
    }
}

@Composable
private fun JornadaCard(
    jornada: CalendarioData.Jornada,
    onMatchClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header de la jornada
            Text(
                text = jornada.nombre,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista de encuentros
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                jornada.encuentros.forEach { encuentro ->
                    EncuentroItem(
                        encuentro = encuentro,
                        onClick = { onMatchClick(encuentro.id) }
                    )

                    // Divisor entre encuentros (excepto el último)
                    if (encuentro != jornada.encuentros.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 1.dp,
                            color = Color(0xFF333333)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EncuentroItem(
    encuentro: Encuentro,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
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
                tint = Color(0xFF4CAF50),
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
                // Mostrar hora si está programado, sino mostrar "--"
                val horaDisplay = encuentro.hora ?: encuentro.resultado ?: "--:--"
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
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}