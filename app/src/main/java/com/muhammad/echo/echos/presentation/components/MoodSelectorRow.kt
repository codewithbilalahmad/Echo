package com.muhammad.echo.echos.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.muhammad.echo.echos.presentation.models.MoodUi

@Composable
fun MoodSelectorRow(
    modifier: Modifier = Modifier,
    selectedMood: MoodUi?,
    onMoodClick: (MoodUi) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MoodUi.entries.forEach {mood ->
            MoodItem(selected = selectedMood == mood, mood = mood, onClick = {
                onMoodClick(mood)
            })
        }
    }
}