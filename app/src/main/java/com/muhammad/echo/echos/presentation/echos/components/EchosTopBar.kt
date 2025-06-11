package com.muhammad.echo.echos.presentation.echos.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.muhammad.echo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EchosTopBar(modifier: Modifier = Modifier, onSettingClick: () -> Unit) {
    TopAppBar(modifier = modifier, title = {
        Text(
            text = stringResource(R.string.title_echos),
            style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface)
        )
    }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent), actions = {
        IconButton(onClick = onSettingClick) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    })
}