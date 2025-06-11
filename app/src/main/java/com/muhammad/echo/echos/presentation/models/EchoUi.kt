package com.muhammad.echo.echos.presentation.models

import com.muhammad.echo.echos.presentation.echos.models.PlaybackState
import com.muhammad.echo.echos.presentation.utils.toReadableTime
import kotlin.time.Duration
import java.time.Instant as JavaInstant

data class EchoUi(
    val id: Int, val title: String, val mood: MoodUi,
    val recordedAt: JavaInstant,
    val note: String?,
    val topics: List<String>,
    val amplitudes: List<Float>, val playbackTotalDuration: Duration,
    val audioFilePath: String,
    val playbackCurrentDuration: Duration = Duration.ZERO,
    val playbackState: PlaybackState = PlaybackState.STOPPED
){
    val formattedRecordedAt = recordedAt.toReadableTime()
    val playBackRatio = (playbackCurrentDuration / playbackTotalDuration).toFloat()
}