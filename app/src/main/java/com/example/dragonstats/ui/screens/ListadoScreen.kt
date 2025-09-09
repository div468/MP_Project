package com.example.dragonstats.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.dragonstats.data.Equipo
import com.example.dragonstats.data.Jugador

@Composable
fun ListadoScreen (e: Equipo){
    Box( //Contenedor de la vista
        modifier = Modifier.fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row( //Contenedor del botón de retroceso y título
                modifier = Modifier.fillMaxWidth().height(20.dp)
                    .background(Color.Transparent),

            ){
                IconButton(
                    onClick = {},
                    modifier = Modifier.height(18.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                    Text(
                        text = "Listado de Jugadores",
                        fontSize = 18.sp
                    )
                }
            }

            DatosEquipo(e)

            Box(//Contenedor con la tabla de los jugadores
                modifier = Modifier.padding(horizontal = 5.dp)
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(150.dp).background(Color.Gray),
            ){

            }
        }
    }
}

@Composable
fun DatosEquipo(equipo: Equipo){
    var isLiked by remember { mutableStateOf(false) }
    Box( //Contenedor con los datos del equipo
        modifier = Modifier.padding(horizontal = 5.dp)
            .padding(top = 10.dp)
            .fillMaxWidth()
            .height(150.dp).background(Color.Gray),
    ){
        Row(
            modifier = Modifier.fillMaxSize()
        ){
            Image( //Logo/Escudo del equipo
                painter = painterResource(id = R.drawable.ic_equipo_default),
                contentDescription = "Logo del equipo",
                modifier = Modifier.size(100.dp)
            )
            Column{ //Datos generales del equipo
                Text(
                    text = equipo.nombre,
                    fontSize = 25.sp
                )
                Text(
                    text = "Ganados: " + 5 +
                            "Empatados: " + 0 +
                            "Perdidos: " + 2
                )
                Text(
                    text = "Pts. Totales: " + equipo.puntos
                )
                IconButton(
                    onClick = { isLiked = !isLiked }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                        contentDescription = if (isLiked) "Quitar like" else "Dar like",
                        tint = if (isLiked) Color(0xFF4CAF50) else Color.Gray
                    )
                }
            }

        }
    }
}

@Composable
fun Encabezados(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = "Jugador",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Start
        )
        Text(
            text = "G", //Goles
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "A", //Asistencias
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Posición",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TablaJugadores(){

}