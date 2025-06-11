package com.muhammad.echo.echos.domain.audio

import kotlin.time.Duration

data class AudioTrack(
    val totalDuration : Duration = Duration.ZERO,
    val durationPlayed : Duration = Duration.ZERO,
    val isPlaying : Boolean = false
)