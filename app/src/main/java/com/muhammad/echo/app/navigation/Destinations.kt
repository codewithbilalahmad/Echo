package com.muhammad.echo.app.navigation

import kotlinx.serialization.Serializable

sealed interface Destinations {
    @Serializable
    data class EchoScreen(val startRecording: Boolean) : Destinations

    @Serializable
    data class CreateEchoScreen(
        val recordingPath: String,
        val duration: Long,
        val amplitudes: String,
    ) : Destinations
    @Serializable
    data object SettingScreen
}