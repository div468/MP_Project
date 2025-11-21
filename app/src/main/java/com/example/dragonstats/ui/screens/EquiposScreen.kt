package com.example.dragonstats.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonstats.R
import com.example.dragonstats.data.model.Equipo
import com.example.dragonstats.ui.viewmodel.EquiposUiState
import com.example.dragonstats.ui.viewmodel.EquiposListadoViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dragonstats.ui.viewmodel.TipoOrden
import com.example.dragonstats.utils.EquipoLogoHelper

@Composable
fun EquiposScreen(
    onEquipoClick: (Equipo) -> Unit,
    viewModel: EquiposListadoViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsState()
    val equiposFavoritos by viewModel.equiposFavoritos.collectAsState()
    val tipoOrdenActual by viewModel.tipoOrden.collectAsState()

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
            textAlign = TextAlign.Left
        )

        TipoOrdenMenu(
            tipoActual = tipoOrdenActual,
            onTipoSeleccionado = { nuevoTipo ->
                viewModel.cambiardeOrden(nuevoTipo)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )


        when (uiState){
            is EquiposUiState.Loading -> {
                LoadingStateListE()
            }
            is EquiposUiState.Success -> {
                val equipos = (uiState as EquiposUiState.Success).equipo
                EquiposListContent(
                    equipos = equipos,
                    equiposFavoritos = equiposFavoritos,
                    onToggleFavorito = { nombreEquipo ->
                        viewModel.toggleFavorito(nombreEquipo)
                    },
                    onEquipoClick = onEquipoClick
                )
            }
            is EquiposUiState.Error -> {
                val errorMessage = (uiState as EquiposUiState.Error).message
                ErrorStateListE(
                    message = errorMessage,
                    onRetry = {viewModel.cargarEquipos()}
                )
            }
        }
    }
}

@Composable
private fun TipoOrdenMenu(
    tipoActual : TipoOrden,
    onTipoSeleccionado : (TipoOrden) -> Unit,
    modifier: Modifier = Modifier
){
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentWidth(Alignment.End)){
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF333333),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                text = tipoActual.toText(),
                fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
            )
                Icon(
                    painter = painterResource(android.R.drawable.arrow_down_float),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false },
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .background(Color(0xFF1A1A1A))
        ) {
            TipoOrden.entries.forEach {tipo ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = tipo.toText(),
                            color = if(tipo == tipoActual) Color(0xFF4CAF50) else Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        onTipoSeleccionado(tipo)
                        expanded = false
                    }
                )
            }
        }
    }
}
@Composable
private fun LoadingStateListE() {
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
                text = "Cargando equipos...",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ErrorStateListE(
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
private fun EquiposListContent(
    equipos: List<Equipo>,
    equiposFavoritos: Set<String>,
    onToggleFavorito: (String) -> Unit,
    onEquipoClick: (Equipo) -> Unit
){
    LazyColumn (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(equipos) { equipo ->
            EquipoCard(
                equipo = equipo,
                isFavorito = equiposFavoritos.contains(equipo.nombre),
                onToggleFavorito = { isFavorito ->
                    onToggleFavorito(equipo.nombre)
                },
                onVerJugadores = {
                    onEquipoClick(equipo)
                }
            )
        }
    }
}

@Composable
private fun EquipoCard(
    equipo: Equipo,
    isFavorito: Boolean,
    onToggleFavorito:(Boolean) -> Unit,
    onVerJugadores:()->Unit
){
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
            // Logo del equipo
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF333333)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = EquipoLogoHelper.getLogoResource(equipo.nombre)),
                    contentDescription = "Logo de ${equipo.nombre}",
                    modifier = Modifier.size(56.dp),
                    contentScale = ContentScale.Fit
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
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = equipo.nombre,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
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