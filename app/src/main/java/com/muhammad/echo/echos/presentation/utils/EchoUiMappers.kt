package com.muhammad.echo.echos.presentation.utils

import com.muhammad.echo.echos.domain.echo.Echo
import com.muhammad.echo.echos.presentation.echos.models.PlaybackState
import com.muhammad.echo.echos.presentation.models.EchoUi
import com.muhammad.echo.echos.presentation.models.MoodUi
import kotlin.time.Duration

fun Echo.toEchoUi(
    currentPlaybackDuration: Duration = Duration.ZERO,
    playbackState: PlaybackState = PlaybackState.STOPPED,
): EchoUi {
    return EchoUi(
        id = id!!,
        title = title,
        mood = MoodUi.valueOf(mood.name),
        recordedAt = recordedAt,
        note = note,
        topics = topics,
        amplitudes = audioAmplitudes,
        audioFilePath = audioFilePath,
        playbackTotalDuration = audioPlaybackLength,
        playbackCurrentDuration = currentPlaybackDuration,
        playbackState = playbackState
    )
}