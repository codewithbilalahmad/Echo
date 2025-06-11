package com.muhammad.echo.echos.presentation.echos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.echo.R

@Composable
fun EchosEmptyBackground(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(imageVector = ImageVector.vectorResource(R.drawable.moods), contentDescription = null)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.title_echos_empty),
            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface)
        )
        Text(text = stringResource(R.string.desc_echos_empty), style= MaterialTheme.typography.bodyMedium)
    }
}