package com.example.dragonstats.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonstats.R
import com.example.dragonstats.ui.screens.tabs.BracketStageTab
import com.example.dragonstats.ui.screens.tabs.FaseGruposTab

enum class GruposTab {
    FASE_GRUPOS, BRACKET_STAGE
}

@Composable
fun GruposScreen() {
    var selectedTab by remember { mutableStateOf(GruposTab.FASE_GRUPOS) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 24.dp)
    ) {
        // Custom Tab Row
        CustomTabRow(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        // Tab Content
        when (selectedTab) {
            GruposTab.FASE_GRUPOS -> FaseGruposTab()
            GruposTab.BRACKET_STAGE -> BracketStageTab()
        }
    }
}

@Composable
private fun CustomTabRow(
    selectedTab: GruposTab,
    onTabSelected: (GruposTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomTab(
            text = stringResource(id = R.string.tab_fase_grupos),
            selected = selectedTab == GruposTab.FASE_GRUPOS,
            onClick = { onTabSelected(GruposTab.FASE_GRUPOS) },
            modifier = Modifier.weight(1f)
        )

        CustomTab(
            text = stringResource(id = R.string.tab_bracket_stage),
            selected = selectedTab == GruposTab.BRACKET_STAGE,
            onClick = { onTabSelected(GruposTab.BRACKET_STAGE) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CustomTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) Color(0xFF4CAF50) else Color(0xFF1A1A1A)

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}