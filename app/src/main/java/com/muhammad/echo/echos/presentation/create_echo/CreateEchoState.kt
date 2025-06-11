package com.muhammad.echo.echos.presentation.create_echo

import com.muhammad.echo.core.presentation.designsystem.dropdown.Selectable
import com.muhammad.echo.echos.presentation.echos.models.PlaybackState
import com.muhammad.echo.echos.presentation.models.MoodUi
import kotlin.time.Duration

data class CreateEchoState(
    val titleText : String = "",
    val addTopicText : String = "",
    val topics : List<String> = emptyList(),
    val noteText : String = "",
    val showMoodSelector : Boolean= false,
    val selectedMood : MoodUi = MoodUi.NEUTRAL,
    val showTopicSuggestion : Boolean = false,
    val mood : MoodUi?=null,
    val searchResults : List<Selectable<String>> = emptyList(),
    val showCreateTopicOption : Boolean = true,
    val canSaveEcho  : Boolean = false,
    val playbackAmplitudes : List<Float> = emptyList(),
    val playbackTotalDuration : Duration = Duration.ZERO,
    val playbackState : PlaybackState = PlaybackState.STOPPED,
    val durationPlayed : Duration = Duration.ZERO,
    val showConfirmLeaveDialog : Boolean = false
){
    val durationPlayedRatio  = (durationPlayed / playbackTotalDuration).toFloat()
}