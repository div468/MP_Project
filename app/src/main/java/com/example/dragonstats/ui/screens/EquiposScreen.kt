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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonstats.R
import com.example.dragonstats.data.model.Equipo
import com.example.dragonstats.data.model.TorneoData
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource

@Composable
fun EquiposScreen(onEquipoClick: (Int) -> Unit) {
    val equipos = TorneoData.obtenerEquiposOrdenados()
    var equiposFavoritos by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ){
        Text(
            text = stringResource(id = R.string.list_title_equipos),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(equipos) { equipo ->
                EquipoCard(
                    equipo = equipo,
                    isFavorito = equiposFavoritos.contains(equipo.nombre),
                    onToggleFavorito = { esFavorito ->
                        equiposFavoritos = if (esFavorito){
                            equiposFavoritos + equipo.nombre
                        }else {
                            equiposFavoritos - equipo.nombre
                        }
                    },
                    onVerJugadores = {
                        onEquipoClick(equipo.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun EquipoCard(equipo: Equipo, isFavorito: Boolean, onToggleFavorito:(Boolean) -> Unit, onVerJugadores:()->Unit){
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Gray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = equipo.nombre.first().toString(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                //Header para nombre de equipo y ver jugadores
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = equipo.nombre,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = onVerJugadores,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.verJugadores),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) { //Informacion de los equipos
                    Text(
                        text = stringResource(R.string.puntos)+ " " + equipo.puntos,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(R.string.grupo)+ " " + equipo.grupo,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    //boton para equipos favoritos
                    IconButton(
                        onClick = {onToggleFavorito(!isFavorito)},
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = if(isFavorito)
                                painterResource(R.drawable.ic_favoritefilled)
                            else
                                painterResource(R.drawable.ic_favorite_screen),
                            contentDescription = null,
                            tint = if(isFavorito) Color(0xFF4CAF50) else Color(0xFF4CAF50),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

            }
        }
    }
}