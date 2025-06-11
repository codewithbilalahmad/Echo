package com.muhammad.echo.echos.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.muhammad.echo.R
import com.muhammad.echo.echos.presentation.components.MoodSelectorRow
import com.muhammad.echo.echos.presentation.models.MoodUi

@Composable
fun MoodCard(
    modifier: Modifier = Modifier,
    selectedMood: MoodUi?,
    onMoodClick: (MoodUi) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(14.dp)
    ) {
        Text(
            text = stringResource(R.string.my_mood),
            style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
        )
        Text(
            text = stringResource(R.string.select_default_mood_to_apply_to_all_new_entries),
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Spacer(Modifier.height(16.dp))
        MoodSelectorRow(
            selectedMood = selectedMood,
            onMoodClick = onMoodClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}