package com.example.dragonstats.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavController
import com.example.dragonstats.R
import com.example.dragonstats.data.model.Equipo
import com.example.dragonstats.data.model.Jugador
import com.example.dragonstats.ui.viewmodel.ListadoUiState
import com.example.dragonstats.ui.viewmodel.ListadoViewModel
import com.example.dragonstats.utils.EquipoLogoHelper

@Composable
fun ListadoScreen (viewModel: ListadoViewModel, navController: NavController){
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 3.dp)
            .background(MaterialTheme.colorScheme.background)
    ){
        when(uiState){
            is ListadoUiState.Loading ->{
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ListadoUiState.Error ->{
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (uiState as ListadoUiState.Error).message,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Volver")
                    }
                }
            }
            is ListadoUiState.Success ->{
                val equipo = (uiState as ListadoUiState.Success).equipo
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top=15.dp)
                            .height(25.dp)
                            .background(Color.Transparent),
                    ){
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.height(25.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_icon_description),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Text(
                            text = stringResource(R.string.list_title),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 18.sp
                        )
                    }

                    DatosEquipo(equipo = equipo, viewModel = viewModel)

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .padding(vertical = 30.dp, horizontal = 15.dp)
                            .fillMaxWidth()
                            .weight(1f)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                    ){
                        TablaJugadores(equipo.jugadores)
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
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = stringResource(R.string.jugador),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(2.25f).padding(start = 5.dp),
            textAlign = TextAlign.Start
        )
        Text(
            text = stringResource(R.string.goles),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.5f),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.asistencias),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.25f),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.posicion),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.85f),
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
        Text(
            text = j.nombre,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(2.25f),
            textAlign = TextAlign.Start
        )

        Text(
            text = j.goles.toString(),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1.5f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = j.asistencias.toString(),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1.25f),
            textAlign = TextAlign.Center
        )

        Text(
            text = j.posicion,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1.85f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun DatosEquipo(equipo: Equipo, viewModel: ListadoViewModel){
    val isFavorite by viewModel.isFavorite.collectAsState()

    Box(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .padding(top = 30.dp)
            .fillMaxWidth()
            .height(150.dp)
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(12.dp)
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp, start = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = EquipoLogoHelper.getLogoResource(equipo.nombre)),
                    contentDescription = "Logo de ${equipo.nombre}",
                    modifier = Modifier.size(56.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical=20.dp, horizontal = 10.dp),
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = equipo.nombre,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 25.sp
                )
                Text(
                    text = stringResource(R.string.ganados)+ " " + equipo.ganados +
                            " " + stringResource(R.string.empatados) + " " + equipo.empatados +
                            " " + stringResource(R.string.perdidos) + " " + equipo.perdidos,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.ptsTotal) + " " + equipo.puntos,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(
                        onClick = { viewModel.toogleFavorite() }
                    ) {
                        Icon(
                            painter = if(isFavorite)
                                painterResource(R.drawable.ic_favoritefilled)
                            else
                                painterResource(R.drawable.ic_favorite_screen),
                            contentDescription = null,
                            tint = if(isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
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
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}