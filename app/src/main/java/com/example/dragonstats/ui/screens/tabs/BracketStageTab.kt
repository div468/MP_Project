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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.dragonstats.data.model.TorneoData

@Composable
fun TeamBox(team: Equipo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(40.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)

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
            text = team.nombre.substring(0,3).uppercase(),
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
fun MatchCardsLazyRow(matches: List<Match>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(matches) { match ->
            MatchCard(
                match = match,
                modifier = Modifier,
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
                MatchCard(match = match,
                    modifier = Modifier)
            }
        }
    }
}

@Composable
fun BracketStageTab() {
    val octavos = remember { createOctavos() }
    val quarterFinals = remember { createQuarterFinals() }
    val semiFinals = remember { createSemiFinals() }
    val finalMatch = remember { createFinal() }
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
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                CenteredMatchRow(round.matches)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.header_phase4),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black
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
                    fontWeight = FontWeight.Bold
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

    MatchCardsLazyRow(
        matches = matchList
    )
}

private fun createOctavos(): List<Match> {
    val teams = getTopTeamsFromGroups(16)

    return if (teams.size >= 16) {
        listOf(
            Match(teams[0], teams[15], "0 - 0"),
            Match(teams[1], teams[14], "0 - 0"),
            Match(teams[2], teams[13], "0 - 0"),
            Match(teams[3], teams[12], "0 - 0"),
            Match(teams[4], teams[11], "0 - 0"),
            Match(teams[5], teams[10], "0 - 0"),
            Match(teams[6], teams[9], "0 - 0"),
            Match(teams[7], teams[8], "0 - 0")
        )
    } else {
        createDummyMatches(8)
    }
}

private fun createQuarterFinals(): List<Match> {
    return listOf(
        Match(createDummyTeam("Ganador O1"), createDummyTeam("Ganador O2"), "0 - 0"),
        Match(createDummyTeam("Ganador O3"), createDummyTeam("Ganador O4"), "0 - 0"),
        Match(createDummyTeam("Ganador O5"), createDummyTeam("Ganador O6"), "0 - 0"),
        Match(createDummyTeam("Ganador O7"), createDummyTeam("Ganador O8"), "0 - 0")
    )
}

private fun createSemiFinals(): List<Match> {
    return listOf(
        Match(createDummyTeam("Ganador QF1"), createDummyTeam("Ganador QF2"), "0 - 0"),
        Match(createDummyTeam("Ganador QF3"), createDummyTeam("Ganador QF4"), "0 - 0")
    )
}

private fun createFinal(): Match {
    return Match(
        createDummyTeam("Ganador SF1"),
        createDummyTeam("Ganador SF2"),
        "0 - 0"
    )
}

private fun getTopTeamsFromGroups(count: Int): List<Equipo> {
    return try {
        TorneoData.obtenerGruposOrdenados()
            .flatMap { it.equipos.take(2) }
            .take(count)
    } catch (e: Exception) {
        emptyList()
    }
}

private fun createDummyMatches(count: Int): List<Match> {
    return List(count) { index ->
        Match(
            createDummyTeam("Equipo ${index * 2 + 1}"),
            createDummyTeam("Equipo ${index * 2 + 2}"),
            "0 - 0"
        )
    }
}

private fun createDummyTeam(name: String): Equipo {
    return Equipo(
        id = 0,
        nombre = "",
        empatados = 0,
        ganados = 0,
        golesContra= 0,
        golesFavor = 0,
        perdidos = 0,
        puntos = 0,
        grupo = ""
    )
}