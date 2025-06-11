@file:OptIn(ExperimentalTime::class)

package com.muhammad.echo.echos.domain.echo

import java.time.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

data class Echo(
    val mood: Mood,
    val title: String,
    val note: String?,
    val topics: List<String>,
    val audioFilePath: String,
    val audioPlaybackLength: Duration,
    val audioAmplitudes: List<Float>,
    val recordedAt: Instant,
    val id: Int? = null
)