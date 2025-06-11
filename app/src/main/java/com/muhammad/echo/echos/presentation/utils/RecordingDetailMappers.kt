package com.muhammad.echo.echos.presentation.utils

import com.muhammad.echo.app.navigation.Destinations
import com.muhammad.echo.echos.domain.recording.RecordingDetails
import kotlin.time.Duration.Companion.milliseconds

fun RecordingDetails.toCreateEchoDestination(): Destinations.CreateEchoScreen {
    return Destinations.CreateEchoScreen(
        recordingPath = this.filePath
            ?: throw IllegalArgumentException("Recording path cant be null"),
        duration = this.duration.inWholeMilliseconds, amplitudes = this.amplitudes.joinToString(";")
    )
}

fun Destinations.CreateEchoScreen.toRecordingDetails(): RecordingDetails {
    return RecordingDetails(
        duration = this.duration.milliseconds,
        amplitudes = this.amplitudes.split(";").map { it.toFloat() },
        filePath = recordingPath
    )
}