package com.example.dragonstats.ui.screens.tabs

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonstats.R
import com.example.dragonstats.data.model.Equipo
import com.example.dragonstats.data.model.Match
import com.example.dragonstats.data.model.Round
import com.example.dragonstats.ui.viewmodel.GruposUiState
import com.example.dragonstats.ui.viewmodel.GruposViewModel

@Composable
fun TeamBox(team: Equipo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(40.dp)
    ) {
        Box(
            modifier = Modifier.size(30.dp)
        ){
            Image(
                painter = painterResource(id = team.shield),
                contentDescription = "Team Shield",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = if (team.nombre.length >= 3) team.nombre.substring(0,3).uppercase() else team.nombre.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            maxLines = 1
        )
    }
}

@Composable
fun MatchCard(match: Match, modifier: Modifier) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardWidth = (screenWidth - 24.dp - 64.dp) / 4
    Card(
        modifier = Modifier.width(cardWidth).height(80.dp),
        elevation = CardDefaults.cardElevation(30.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.dark_gray)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                TeamBox(team = match.teamA)
                TeamBox(team = match.teamB)
            }
            Text(
                text = match.score,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CenteredMatchRow(matches: List<Match>) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(matches) { match ->
                MatchCard(match = match, modifier = Modifier)
            }
        }
    }
}

@Composable
fun BracketStageTab(viewModel: GruposViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (val state = uiState) {
            is GruposUiState.Loading -> {
                LoadingStateBracket()
            }
            is GruposUiState.Success -> {
                BracketContent(
                    viewModel = viewModel,
                    topTeams = state.topTeams
                )
            }
            is GruposUiState.Error -> {
                ErrorStateBracket(
                    message = state.message,
                    onRetry = { viewModel.retry() }
                )
            }
        }
    }
}

@Composable
private fun LoadingStateBracket() {
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
                text = "Cargando bracket...",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ErrorStateBracket(
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
                text = "Error al cargar el bracket",
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
private fun BracketContent(
    viewModel: GruposViewModel,
    topTeams: List<Equipo>
) {
    val octavos = viewModel.createOctavos(topTeams)
    val quarterFinals = viewModel.createQuarterFinals()
    val semiFinals = viewModel.createSemiFinals()
    val finalMatch = viewModel.createFinal()

    val topRounds = listOf(
        Round(
            phase = stringResource(id = R.string.header_phase1),
            matches = octavos.take(4),
        ),
        Round(
            phase = stringResource(id = R.string.header_phase2),
            matches = quarterFinals.take(2),
        ),
        Round(
            phase = stringResource(id = R.string.header_phase3),
            matches = semiFinals.take(1),
        )
    )

    val bottomRounds = listOf(
        Round(
            phase = stringResource(id = R.string.header_phase3),
            matches = semiFinals.drop(1),
        ),
        Round(
            phase = stringResource(id = R.string.header_phase2),
            matches = quarterFinals.drop(2),
        ),
        Round(
            phase = stringResource(id = R.string.header_phase1),
            matches = octavos.drop(4),
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        topRounds.forEach { round ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = round.phase,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                CenteredMatchRow(round.matches)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.header_phase4),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        MatchCard(
            match = finalMatch,
            modifier = Modifier
                .width(180.dp)
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        bottomRounds.forEach { round ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = round.phase,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                CenteredMatchRow(round.matches)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMatchCard() {
    val match = Equipo(1, "Dragons FC", 3, 5, 9, 7,1,1,"C")
    val match2 = Equipo(2, "Phoenix United", 3, 2, 7, 7,1,1,"C")

    val match6 = Match(
        teamA = match,
        teamB = match2,
        score = "0 - 0"
    )
    val match7 = Match(
        teamA = match,
        teamB = match2,
        score = "1 - 1"
    )
    val match8 = Match(
        teamA = match,
        teamB = match2,
        score = "2 - 0"
    )
    val match9 = Match(
        teamA = match,
        teamB = match2,
        score = "0 - 2"
    )

    val matchList: List<Match> = listOf(match6, match7, match8, match9)

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(matchList) { match ->
            MatchCard(
                match = match,
                modifier = Modifier,
            )
        }
    }
}