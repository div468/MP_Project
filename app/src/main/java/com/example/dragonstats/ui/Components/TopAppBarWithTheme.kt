package com.example.dragonstats.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.dragonstats.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithTheme(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = onToggleTheme) {
                Icon(
                    painter = painterResource(
                        id = if (isDarkMode) {
                            R.drawable.ic_light_mode
                        } else {
                            R.drawable.ic_dark_mode
                        }
                    ),
                    contentDescription = if (isDarkMode) {
                        "Cambiar a modo claro"
                    } else {
                        "Cambiar a modo oscuro"
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}