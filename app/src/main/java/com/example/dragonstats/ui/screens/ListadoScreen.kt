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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dragonstats.R
import com.example.dragonstats.data.model.Equipo
import com.example.dragonstats.data.model.Jugador
import com.example.dragonstats.data.model.TorneoData

@Composable
fun ListadoScreen (e: Int, navController: NavController){
    val j = TorneoData.obtenerJugadores()
    val eq = obtenerEquipoPorId(e)
    Box( //Contenedor de la vista
        modifier = Modifier.fillMaxSize()
            .background(Color.Black)
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row( //Contenedor del botón de retroceso y título
                modifier = Modifier.fillMaxWidth().height(25.dp)
                    .background(Color.Transparent),

                ){
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.height(18.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_icon_description)
                    )
                }
                Text(
                    text = stringResource(R.string.list_title),
                    fontSize = 18.sp
                )
            }

            DatosEquipo(eq)

            Box(//Contenedor con la tabla de los jugadores
                modifier = Modifier.padding(horizontal = 5.dp)
                    .padding(vertical = 30.dp, horizontal = 15.dp)
                    .fillMaxWidth().weight(1f)
                    .height(150.dp).background(Color(0xFF1A1A1A),RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
            ){
                TablaJugadores(j)
            }
        }
    }
}

@Composable
fun DatosEquipo(equipo: Equipo?){
    var isLiked by remember { mutableStateOf(false) }
    Box( //Contenedor con los datos del equipo
        modifier = Modifier.padding(horizontal = 15.dp)
            .padding(top = 40.dp)
            .fillMaxWidth()
            .height(150.dp).background(Color(0xFF1A1A1A),RoundedCornerShape(12.dp))
    ){
        Row(
            modifier = Modifier.fillMaxSize().padding(top = 20.dp)
        ){
            Image( //Logo/Escudo del equipo
                painter = painterResource(id = R.drawable.ic_equipo_default),
                contentDescription = stringResource(R.string.logo_equipo),
                modifier = Modifier.size(100.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ){ //Datos generales del equipo
                Text(
                    text = equipo!!.nombre,
                    fontSize = 25.sp
                )
                Text(
                    text = stringResource(R.string.ganados)+ " " + 5 +
                            " " + stringResource(R.string.empatados) + " " + 0 +
                            " " + stringResource(R.string.perdidos) + " " + 2
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.ptsTotal) + " " + equipo.puntos
                    )
                    IconButton(
                        onClick = { isLiked = !isLiked }
                    ) {
                        Icon(
                            painter = if(isLiked)
                                painterResource(R.drawable.ic_favoritefilled)
                            else
                                painterResource(R.drawable.ic_favorite_screen),
                            contentDescription = null,
                            tint = if(isLiked) Color(0xFF4CAF50) else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
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
            text = stringResource(R.string.jugador),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Start
        )
        Text(
            text = stringResource(R.string.goles), //Goles
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.asistencias), //Asistencias
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.posicion),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FilaJugador(j:Jugador){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Columna Jugador
        Text(
            text = j.nombre,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Start
        )

        // Columna Goles
        Text(
            text = j.goles.toString(),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        // Columna Asistencias
        Text(
            text = j.asistencias.toString(),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        // Columna Posición
        Text(
            text = j.posicion,
            modifier = Modifier.weight(1.5f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun TablaJugadores(jugadores: List<Jugador>){
    Column(modifier = Modifier.fillMaxWidth()){
        Encabezados()
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(jugadores){ index, jugador ->
                FilaJugador(jugador)
                if (index != jugadores.size - 1) {
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

fun obtenerEquipoPorId(id: Int): Equipo? {
    return TorneoData.obtenerGrupos()
        .flatMap { it.equipos } // Aplanamos todas las listas de equipos
        .firstOrNull { it.id == id } // Buscamos por ID
}