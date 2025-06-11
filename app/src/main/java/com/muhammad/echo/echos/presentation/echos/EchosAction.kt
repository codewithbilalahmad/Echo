package com.muhammad.echo.echos.presentation.echos

import android.content.Context
import com.muhammad.echo.echos.presentation.echos.models.EchoFilterChip
import com.muhammad.echo.echos.presentation.echos.models.TrackSizeInfo
import com.muhammad.echo.echos.presentation.models.MoodUi

sealed interface EchosAction{
    data object OnMoodChipClick : EchosAction
    data object OnDismissMoodDropDown : EchosAction
    data class OnFilterByMoodClick(val moodUi : MoodUi) : EchosAction
    data object OnTopicChipClick : EchosAction
    data object OnDismissTopicDropDown : EchosAction
    data class OnFilterByTopicClick(val topic : String) : EchosAction
    data object OnRecordFabClick : EchosAction
    data object OnRequestPermissionQuickRecording : EchosAction
    data object OnRecordButtonLongClick : EchosAction
    data object OnPauseRecordingClick : EchosAction
    data object OnResumeRecordingClick : EchosAction
    data object OnResumeAudioClick : EchosAction
    data object OnCompleteRecording : EchosAction
    data object OnPauseAudioClick : EchosAction
    data class OnTrackSizeAvailable(val trackSizeInfo : TrackSizeInfo) : EchosAction
    data class OnRemoveFilters(val filterType : EchoFilterChip) : EchosAction
    data class OnPlayEchoClick(val echoId : Int) : EchosAction
    data object OnAudioPermissionGranted : EchosAction
    data object OnCancelRecording : EchosAction
    data object OnMicPermissionPermanentlyDenied : EchosAction
    data object OnDismissMicAccessDialog : EchosAction
}