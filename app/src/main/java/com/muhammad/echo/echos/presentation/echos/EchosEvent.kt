package com.muhammad.echo.echos.presentation.echos

import com.muhammad.echo.echos.domain.recording.RecordingDetails

interface EchosEvent{
    data object RequestAudioPermission : EchosEvent
    data class OnDoneRecording(val details : RecordingDetails) : EchosEvent
}