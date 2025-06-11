package com.muhammad.echo.echos.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.muhammad.echo.R
import com.muhammad.echo.core.presentation.designsystem.theme.Pause
import com.muhammad.echo.core.presentation.util.defaultShadow
import com.muhammad.echo.echos.presentation.echos.models.PlaybackState
import kotlin.time.Duration

@Composable
fun EchoPlaybackButton(
    modifier: Modifier = Modifier,
    playbackState: PlaybackState,
    onPlayClick: () -> Unit,currentPlaybackDuration : Duration,
    onPauseClick: () -> Unit,onResumeClick : () -> Unit,
    colors: IconButtonColors
) {
    FilledIconButton(onClick = {
        when{
           playbackState == PlaybackState.PLAYING -> onPauseClick()
            currentPlaybackDuration != Duration.ZERO-> onResumeClick()
            playbackState ==  PlaybackState.PAUSED || playbackState == PlaybackState.STOPPED -> {
                onPlayClick()
            }
        }
    }, colors = colors, modifier = modifier.defaultShadow()) {
        Icon(imageVector = when(playbackState){
            PlaybackState.PLAYING -> Icons.Filled.Pause
            PlaybackState.PAUSED, PlaybackState.STOPPED -> Icons.Filled.PlayArrow
        }, contentDescription = when(playbackState){
            PlaybackState.PLAYING -> stringResource(R.string.playing)
            PlaybackState.PAUSED -> stringResource(R.string.paused)
            PlaybackState.STOPPED -> stringResource(R.string.stopped)
        })
    }
}