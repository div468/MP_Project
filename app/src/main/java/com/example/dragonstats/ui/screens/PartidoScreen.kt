package com.example.dragonstats.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import com.example.dragonstats.R

data class PlayerEvent(
    val time: String,
    val playerName: String,
    val eventType: EventType
)

enum class EventType {
    GOL, TARJETA_AMARILLA, TARJETA_ROJA, CAMBIO
}

data class MatchData(
    val teamA: String,
    val teamB: String,
    val teamALogo: Int,
    val teamBLogo: Int,
    val scoreA: String,
    val scoreB: String,
    val date: String,
    val leftTeamEvents: List<PlayerEvent>,
    val rightTeamEvents: List<PlayerEvent>
)

object PartidoData {
    fun getMatchData(): MatchData {
        return MatchData(
            teamA = "Real Madrid",
            teamB = "FC Barcelona",
            teamALogo = R.drawable.ic_equipo_default,
            teamBLogo = R.drawable.ic_equipo_default,
            scoreA = "2",
            scoreB = "3",
            date = "15/03/2024",
            leftTeamEvents = listOf(
                PlayerEvent("3'", "Miguel M.", EventType.GOL),
                PlayerEvent("18'", "Edinson C.", EventType.TARJETA_AMARILLA),
                PlayerEvent("23'", "Rodrigo B.", EventType.TARJETA_ROJA),
                PlayerEvent("35'", "Leandro P.", EventType.CAMBIO),
                PlayerEvent("67'", "Miguel M.", EventType.GOL),
                PlayerEvent("82'", "Carlos P.", EventType.TARJETA_AMARILLA)
            ),
            rightTeamEvents = listOf(
                PlayerEvent("12'", "Lucas N.", EventType.GOL),
                PlayerEvent("29'", "Adrián M.", EventType.CAMBIO),
                PlayerEvent("41'", "Santiago S.", EventType.TARJETA_AMARILLA),
                PlayerEvent("54'", "Lucas N.", EventType.GOL),
                PlayerEvent("73'", "Agustin A.", EventType.TARJETA_ROJA),
                PlayerEvent("89'", "Adrián M.", EventType.GOL)
            )
        )
    }
}

@Composable
fun PartidoDetailsScreen(onBackClick: () -> Unit = {}) {
    val matchData = PartidoData.getMatchData()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
    ) {
        // Back button
        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF008934)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "CALENDARIO",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Title
        Text(
            text = "Descripción del partido",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Match result section in card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0x66424242)),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            MatchResultSection(
                matchData = matchData,
                modifier = Modifier.padding(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Events section
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val maxEvents = maxOf(matchData.leftTeamEvents.size, matchData.rightTeamEvents.size)

            items(maxEvents) { index ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left team event
                    if (index < matchData.leftTeamEvents.size) {
                        EventCard(
                            event = matchData.leftTeamEvents[index],
                            isLeftTeam = true,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Right team event
                    if (index < matchData.rightTeamEvents.size) {
                        EventCard(
                            event = matchData.rightTeamEvents[index],
                            isLeftTeam = false,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchResultSection(
    matchData: MatchData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Team A
        TeamInfo(
            teamName = matchData.teamA,
            score = matchData.scoreA,
            teamLogo = matchData.teamALogo
        )

        // Center info
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "-",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = matchData.date,
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Team B
        TeamInfo(
            teamName = matchData.teamB,
            score = matchData.scoreB,
            teamLogo = matchData.teamBLogo
        )
    }
}

@Composable
private fun TeamInfo(teamName: String, score: String, teamLogo: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = teamLogo),
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = score,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = teamName,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EventCard(
    event: PlayerEvent,
    isLeftTeam: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF424242)),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = if (isLeftTeam) Arrangement.Start else Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLeftTeam) {
                // Left team: Icon - Time - Name
                Icon(
                    painter = painterResource(id = getEventIcon(event.eventType)),
                    contentDescription = null,
                    tint = getEventColor(event.eventType),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = event.time,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = event.playerName,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            } else {
                // Right team: Name - Time - Icon
                Text(
                    text = event.playerName,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = event.time,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(id = getEventIcon(event.eventType)),
                    contentDescription = null,
                    tint = getEventColor(event.eventType),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

private fun getEventIcon(eventType: EventType): Int {
    return when (eventType) {
        EventType.GOL -> R.drawable.ic_equipo_default
        EventType.TARJETA_AMARILLA -> R.drawable.ic_equipo_default
        EventType.TARJETA_ROJA -> R.drawable.ic_equipo_default
        EventType.CAMBIO -> R.drawable.ic_equipo_default
    }
}

private fun getEventColor(eventType: EventType): Color {
    return when (eventType) {
        EventType.GOL -> Color(0xFF4CAF50)
        EventType.TARJETA_AMARILLA -> Color(0xFFFFEB3B)
        EventType.TARJETA_ROJA -> Color(0xFFF44336)
        EventType.CAMBIO -> Color(0xFF2196F3)
    }
}