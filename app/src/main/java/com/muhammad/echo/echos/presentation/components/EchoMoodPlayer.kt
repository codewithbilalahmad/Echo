package com.muhammad.echo.echos.presentation.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.echo.core.presentation.designsystem.theme.MoodPrimary25
import com.muhammad.echo.core.presentation.designsystem.theme.MoodPrimary35
import com.muhammad.echo.core.presentation.designsystem.theme.MoodPrimary80
import com.muhammad.echo.core.presentation.util.formatMMSS
import com.muhammad.echo.echos.presentation.echos.models.PlaybackState
import com.muhammad.echo.echos.presentation.echos.models.TrackSizeInfo
import com.muhammad.echo.echos.presentation.models.MoodUi
import kotlin.time.Duration

@Composable
fun EchoMoodPlayer(
    modifier: Modifier = Modifier,
    moodUi: MoodUi?,
    playbackState: PlaybackState,
    playerProgress: () -> Float,
    durationPlayed: Duration,
    totalPlaybackDuration: Duration,
    powerRatios: List<Float>,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,onResumeClick : () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit,
    amplitudeBarWidth: Dp = 3.dp,
    amplitudeBarSpacing: Dp = 1.5.dp,
) {
    val iconTint = when (moodUi) {
        null -> MoodPrimary80
        else -> moodUi.colorSet.vivid
    }
    val trackFillColor = when (moodUi) {
        null -> MoodPrimary80
        else -> moodUi.colorSet.vivid
    }
    val backgroundColor = when (moodUi) {
        null -> MoodPrimary25
        else -> moodUi.colorSet.faded
    }
    val trackColor = when (moodUi) {
        null -> MoodPrimary35
        else -> moodUi.colorSet.desaturated
    }
    val formattedDuration = remember(durationPlayed, totalPlaybackDuration) {
        "${durationPlayed.formatMMSS()}/${totalPlaybackDuration.formatMMSS()}"
    }
    val density = LocalDensity.current
    Surface(modifier = modifier, color = backgroundColor, shape = CircleShape) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EchoPlaybackButton(
                playbackState = playbackState,
                onPlayClick = onPlayClick,
                onPauseClick = onPauseClick, onResumeClick = onResumeClick, currentPlaybackDuration = durationPlayed,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = iconTint
                )
            )
            EchoPlayBar(
                amplitudeBarWidth = amplitudeBarWidth,
                amplitudeBarSpacing = amplitudeBarSpacing,
                powerRatios = powerRatios,
                trackColor = trackColor,
                trackFillColor = trackFillColor,
                playerProgress = playerProgress,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp, horizontal = 8.dp)
                    .fillMaxHeight()
                    .onSizeChanged {
                        if (it.width > 0) {
                            onTrackSizeAvailable(
                                TrackSizeInfo(
                                    trackWidth = it.width.toFloat(),
                                    barWidth = with(density) { amplitudeBarWidth.toPx() },
                                    spacing = with(density) { amplitudeBarSpacing.toPx() }
                                )
                            )
                        }
                    })
            Text(
                text = formattedDuration,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFeatureSettings = "tnum"
                ),
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}
