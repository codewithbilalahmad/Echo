@file:OptIn(ExperimentalMaterial3Api::class)

package com.muhammad.echo.echos.presentation.create_echo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.muhammad.echo.R
import com.muhammad.echo.core.presentation.designsystem.buttons.PrimaryButton
import com.muhammad.echo.core.presentation.designsystem.buttons.SecondaryButton
import com.muhammad.echo.echos.presentation.components.MoodSelectorRow
import com.muhammad.echo.echos.presentation.models.MoodUi

@Composable
fun SelectMoodSheet(
    modifier: Modifier = Modifier,
    selectedMood: MoodUi,
    onMoodClick: (MoodUi) -> Unit,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.how_are_you_doing),
                style = MaterialTheme.typography.titleMedium
            )
            MoodSelectorRow(
                selectedMood = selectedMood,
                onMoodClick = onMoodClick,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecondaryButton(text = stringResource(R.string.cancel), onClick = onDismiss)
                PrimaryButton(
                    text = stringResource(R.string.confirm),
                    onClick = onConfirmClick,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}