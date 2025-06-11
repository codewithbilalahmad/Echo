package com.muhammad.echo.echos.presentation.echos

import com.muhammad.echo.R
import com.muhammad.echo.core.presentation.designsystem.dropdown.Selectable
import com.muhammad.echo.core.presentation.util.UiText
import com.muhammad.echo.echos.presentation.echos.models.AudioCaptureMethod
import com.muhammad.echo.echos.presentation.echos.models.EchoDaySection
import com.muhammad.echo.echos.presentation.echos.models.EchoFilterChip
import com.muhammad.echo.echos.presentation.echos.models.MoodChipContent
import com.muhammad.echo.echos.presentation.echos.models.RecordingState
import com.muhammad.echo.echos.presentation.models.EchoUi
import com.muhammad.echo.echos.presentation.models.MoodUi
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.time.Duration

data class EchosState(
    val echos: Map<UiText, List<EchoUi>> = emptyMap(),
    val currentCaptureMethod: AudioCaptureMethod? = null,
    val recordingElapsedDuration: Duration = Duration.ZERO,
    val hasEchosRecorded: Boolean = false,
    val hasActiveTopicFilters: Boolean = false,
    val hasActiveMoodFilters: Boolean = false,
    val isLoadingData: Boolean = false,
    val recordingState: RecordingState = RecordingState.NOT_RECORDING,
    val moods: List<Selectable<MoodUi>> = emptyList(),
    val topics: List<Selectable<String>> = emptyList(),
    val moodChipContent: MoodChipContent = MoodChipContent(),
    val selectedEchoFilterChip: EchoFilterChip? = null,
    val topicChipTitle: UiText = UiText.StringResource(R.string.all_topics),
    val isMicPermanentlyDenied : Boolean = false,
    val showRecordingBottomSheet : Boolean = false,
    val showAllowMicAccessDialog : Boolean = false
) {
    val echoDaySections = echos.toList().map { (dateHeader, echos) ->
        EchoDaySection(dateHeader = dateHeader, echos = echos)
    }
    val formattedRecordDuration: String
        get() {
            val minutes = (recordingElapsedDuration.inWholeMinutes % 60).toInt()
            val seconds = (recordingElapsedDuration.inWholeSeconds % 60).toInt()
            val centiSeconds = ((recordingElapsedDuration.inWholeMilliseconds % 1000) / 10.0).roundToInt()
            return String.format(
                locale = Locale.US,
                format = "%02d:%02d:%02d",
                minutes,
                seconds,
                centiSeconds
            )
        }
}