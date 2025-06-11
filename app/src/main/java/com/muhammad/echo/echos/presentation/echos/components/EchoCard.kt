package com.muhammad.echo.echos.presentation.echos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.echo.core.presentation.designsystem.chips.HashTagChip
import com.muhammad.echo.core.presentation.util.defaultShadow
import com.muhammad.echo.echos.presentation.components.EchoMoodPlayer
import com.muhammad.echo.echos.presentation.echos.models.TrackSizeInfo
import com.muhammad.echo.echos.presentation.models.EchoUi

@Composable
fun EchoCard(
    modifier: Modifier = Modifier,
    echoUi: EchoUi,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,onResumeClick : () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.defaultShadow(shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = echoUi.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = echoUi.formattedRecordedAt,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
            EchoMoodPlayer(
                moodUi = echoUi.mood,
                playbackState = echoUi.playbackState,
                playerProgress = { echoUi.playBackRatio },
                durationPlayed = echoUi.playbackCurrentDuration,
                totalPlaybackDuration = echoUi.playbackTotalDuration,
                onPlayClick = onPlayClick,
                onPauseClick = onPauseClick,
                powerRatios = echoUi.amplitudes,
                onTrackSizeAvailable = onTrackSizeAvailable, onResumeClick = {
                    onResumeClick()
                }
            )
            if(!echoUi.note.isNullOrBlank()){
                EchoExpandableText(text = echoUi.note)
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)){
                echoUi.topics.forEach {topic ->
                    HashTagChip(text = topic)
                }
            }
        }
    }
}