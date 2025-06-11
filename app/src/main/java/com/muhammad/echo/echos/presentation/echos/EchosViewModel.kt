package com.muhammad.echo.echos.presentation.echos

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.echo.R
import com.muhammad.echo.core.presentation.designsystem.dropdown.Selectable
import com.muhammad.echo.core.presentation.util.UiText
import com.muhammad.echo.echos.domain.audio.AudioPlayer
import com.muhammad.echo.echos.domain.echo.Echo
import com.muhammad.echo.echos.domain.echo.EchoDataSource
import com.muhammad.echo.echos.domain.recording.VoiceRecorder
import com.muhammad.echo.echos.presentation.echos.models.AudioCaptureMethod
import com.muhammad.echo.echos.presentation.echos.models.EchoFilterChip
import com.muhammad.echo.echos.presentation.echos.models.MoodChipContent
import com.muhammad.echo.echos.presentation.echos.models.PlaybackState
import com.muhammad.echo.echos.presentation.echos.models.RecordingState
import com.muhammad.echo.echos.presentation.echos.models.TrackSizeInfo
import com.muhammad.echo.echos.presentation.models.EchoUi
import com.muhammad.echo.echos.presentation.models.MoodUi
import com.muhammad.echo.echos.presentation.utils.AmplitudeNormalizer
import com.muhammad.echo.echos.presentation.utils.toEchoUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

class EchosViewModel(
    private val voiceRecorder: VoiceRecorder,
    private val audioPlayer: AudioPlayer,
    private val echoDataSource: EchoDataSource,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var hasLoadedInitialData = false
    private val playingEchoId = MutableStateFlow<Int?>(null)
    private val selectedMoodFilters = MutableStateFlow<List<MoodUi>>(emptyList())
    private val selectedTopicFilters = MutableStateFlow<List<String>>(emptyList())
    private val audioTrackSizeInfo = MutableStateFlow<TrackSizeInfo?>(null)
    private val eventChannel = Channel<EchosEvent>()
    val events = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(EchosState())
    val state = _state.onStart {
        if (!hasLoadedInitialData) {
            observeFilters()
            observeEchos()
            fetchNavigationArgs()
            hasLoadedInitialData = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = EchosState()
    )
    private val filteredEchos =
        echoDataSource.observeEchos().filterByMoodAndTopics().onEach { echos ->
            _state.update { it.copy(hasEchosRecorded = echos.isNotEmpty(), isLoadingData = false) }
        }.combine(audioTrackSizeInfo) { echos, trackSizeInfo ->
            if (trackSizeInfo != null) {
                echos.map { echo ->
                    echo.copy(
                        audioAmplitudes = AmplitudeNormalizer.normalize(
                            sourceAmplitudes = echo.audioAmplitudes,
                            trackWidth = trackSizeInfo.trackWidth,
                            barWidth = trackSizeInfo.barWidth, spacing = trackSizeInfo.spacing
                        )
                    )
                }
            } else echos
        }.flowOn(Dispatchers.Default)

    fun onAction(action: EchosAction) {
        when (action) {
            EchosAction.OnCancelRecording -> cancelRecording()
            EchosAction.OnCompleteRecording -> stopRecording()
            EchosAction.OnDismissMoodDropDown, EchosAction.OnDismissTopicDropDown -> {
                _state.update { it.copy(selectedEchoFilterChip = null) }
            }

            is EchosAction.OnFilterByMoodClick -> {
                toggleMoodFilter(action.moodUi)
            }

            is EchosAction.OnFilterByTopicClick -> {
                toggleTopicFilter(action.topic)
            }

            EchosAction.OnMoodChipClick -> {
                _state.update { it.copy(selectedEchoFilterChip = EchoFilterChip.MOODS) }
            }

            EchosAction.OnPauseAudioClick -> audioPlayer.pause()
            EchosAction.OnPauseRecordingClick -> pauseRecording()
            is EchosAction.OnPlayEchoClick -> onPlayEchoClick(action.echoId)
            EchosAction.OnRecordButtonLongClick -> {
                _state.update {
                    it.copy(
                        currentCaptureMethod = AudioCaptureMethod.QUICK,
                        recordingState = RecordingState.QUICK_CAPTURE
                    )
                }
            }

            EchosAction.OnRecordFabClick -> {
                requestAudioPermission()
                _state.update {
                    it.copy(
                        currentCaptureMethod = AudioCaptureMethod.STANDARD,
                        recordingState = RecordingState.NORMAL_CAPTURE
                    )
                }
            }

            is EchosAction.OnRemoveFilters -> {
                when (action.filterType) {
                    EchoFilterChip.MOODS -> selectedMoodFilters.update { emptyList() }
                    EchoFilterChip.TOPICS -> selectedTopicFilters.update { emptyList() }
                }
            }

            EchosAction.OnRequestPermissionQuickRecording -> {
                requestAudioPermission()
                _state.update {
                    it.copy(
                        currentCaptureMethod = AudioCaptureMethod.QUICK,
                        recordingState = RecordingState.QUICK_CAPTURE
                    )
                }
            }

            EchosAction.OnResumeRecordingClick -> resumeRecording()
            EchosAction.OnTopicChipClick -> {
                _state.update { it.copy(selectedEchoFilterChip = EchoFilterChip.TOPICS) }
            }

            is EchosAction.OnTrackSizeAvailable -> {
                audioTrackSizeInfo.update { action.trackSizeInfo }
            }

            EchosAction.OnAudioPermissionGranted -> {
                _state.update {
                    it.copy(
                        isMicPermanentlyDenied = false,
                        showAllowMicAccessDialog = false, showRecordingBottomSheet = true
                    )
                }
                startRecording(captureMethod = AudioCaptureMethod.STANDARD)
            }

            EchosAction.OnMicPermissionPermanentlyDenied -> {
                _state.update {
                    it.copy(
                        isMicPermanentlyDenied = true,
                        showAllowMicAccessDialog = true
                    )
                }
            }

            EchosAction.OnDismissMicAccessDialog -> {
                _state.update { it.copy(showAllowMicAccessDialog = false) }
            }

            EchosAction.OnResumeAudioClick -> {
                audioPlayer.resume()
            }
        }
    }

    private fun fetchNavigationArgs() {
        val startRecording = savedStateHandle["startRecording"] ?: false
        if (startRecording) {
            _state.update { it.copy(currentCaptureMethod = AudioCaptureMethod.STANDARD) }
            requestAudioPermission()
        }
    }

    private fun observeEchos() {
        combine(
            filteredEchos,
            playingEchoId,
            audioPlayer.activeTrack
        ) { echos, playingEchoId, activeTrack ->
            if (playingEchoId == null || activeTrack == null) {
                return@combine echos.map { it.toEchoUi() }
            }
            echos.map { echo ->
                if (echo.id == playingEchoId) {
                    echo.toEchoUi(
                        currentPlaybackDuration = activeTrack.durationPlayed,
                        playbackState = if (activeTrack.isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED
                    )
                } else echo.toEchoUi()
            }
        }.groupByRelativeDate().onEach { groupEchos ->
            _state.update { it.copy(echos = groupEchos) }
            println("All Echos List : ${groupEchos.values.toList()}")
        }.flowOn(Dispatchers.Default).launchIn(viewModelScope)
    }

    private fun onPlayEchoClick(echoId: Int) {
        val selectedEcho = state.value.echos.values.flatten().first { it.id == echoId }
        val activeTrack = audioPlayer.activeTrack.value
        val isNewEcho = playingEchoId.value != echoId
        val isSameEchoIsPlayingFromBeginning =
            echoId == playingEchoId.value && activeTrack != null && activeTrack.durationPlayed == Duration.ZERO
        when {
            isNewEcho || isSameEchoIsPlayingFromBeginning -> {
                playingEchoId.update { echoId }
                audioPlayer.stop()
                audioPlayer.play(
                    filePath = selectedEcho.audioFilePath,
                    onComplete = ::completePlayback
                )
            }

            else -> audioPlayer.resume()
        }
    }

    private fun completePlayback() {
        _state.update {
            it.copy(echos = it.echos.mapValues { (_, echos) ->
                echos.map { echo ->
                    echo.copy(playbackTotalDuration = Duration.ZERO)
                }
            })
        }
        playingEchoId.update { null }
    }

    private fun requestAudioPermission() = viewModelScope.launch {
        eventChannel.send(EchosEvent.RequestAudioPermission)
    }

    private fun pauseRecording() {
        voiceRecorder.pause()
        _state.update { it.copy(recordingState = RecordingState.PAUSED) }
    }

    private fun resumeRecording() {
        voiceRecorder.resume()
        _state.update { it.copy(recordingState = RecordingState.NORMAL_CAPTURE) }
    }

    private fun cancelRecording() {
        _state.update {
            it.copy(
                recordingState = RecordingState.NOT_RECORDING,
                currentCaptureMethod = null,
                showRecordingBottomSheet = false
            )
        }
        voiceRecorder.cancel()
    }

    private fun stopRecording() {
        voiceRecorder.stop()
        _state.update {
            it.copy(
                recordingState = RecordingState.NOT_RECORDING,
                showRecordingBottomSheet = false
            )
        }
        val recordingDetails = voiceRecorder.recordingDetails.value
        viewModelScope.launch {
            eventChannel.send(
                EchosEvent.OnDoneRecording(
                    details = recordingDetails.copy(
                        amplitudes = AmplitudeNormalizer.normalize(
                            sourceAmplitudes = recordingDetails.amplitudes,
                            trackWidth = 10_000f,
                            barWidth = 20f,
                            spacing = 15f
                        )
                    )
                )
            )
        }
    }

    private fun startRecording(captureMethod: AudioCaptureMethod) {
        _state.update {
            it.copy(
                recordingState = when (captureMethod) {
                    AudioCaptureMethod.STANDARD -> RecordingState.NORMAL_CAPTURE
                    AudioCaptureMethod.QUICK -> RecordingState.QUICK_CAPTURE
                }
            )
        }
        voiceRecorder.start()
        if (captureMethod == AudioCaptureMethod.STANDARD) {
            voiceRecorder.recordingDetails.distinctUntilChangedBy { it.duration }
                .map { it.duration }.onEach { duration ->
                    _state.update { it.copy(recordingElapsedDuration = duration) }
                }.launchIn(viewModelScope)
        }
    }

    private fun toggleMoodFilter(mood: MoodUi) {
        selectedMoodFilters.update { selectedMoods ->
            if (mood in selectedMoods) {
                selectedMoods - mood
            } else {
                selectedMoods + mood
            }
        }
    }

    private fun toggleTopicFilter(topic: String) {
        selectedTopicFilters.update { selectedTopics ->
            if (topic in selectedTopics) {
                selectedTopics - topic
            } else {
                selectedTopics + topic
            }
        }
    }

    private fun observeFilters() {
        combine(
            echoDataSource.observeTopics(),
            selectedMoodFilters,
            selectedTopicFilters
        ) { allTopics, selectedMoods, selectedTopics ->
            _state.update {
                it.copy(
                    topics = allTopics.map { topic ->
                        Selectable(item = topic, selected = selectedTopics.contains(topic))
                    },
                    moods = MoodUi.entries.map { mood ->
                        Selectable(item = mood, selected = selectedMoods.contains(mood))
                    },
                    hasActiveMoodFilters = selectedMoods.isNotEmpty(),
                    hasActiveTopicFilters = selectedTopics.isNotEmpty(),
                    topicChipTitle = selectedTopics.deriveTopicsToText(),
                    moodChipContent = selectedMoods.asMoodChipContent()
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun List<String>.deriveTopicsToText(): UiText {
        return when (size) {
            0 -> UiText.StringResource(R.string.all_topics)
            1 -> UiText.Dynamic(this.first())
            2 -> UiText.Dynamic("${this.first()},${this.last()}")
            else -> {
                val extraElementCount = size - 2
                UiText.Dynamic("${this.first()},${this[1]} + $extraElementCount")
            }
        }
    }

    private fun List<MoodUi>.asMoodChipContent(): MoodChipContent {
        if (this.isEmpty()) {
            return MoodChipContent()
        }
        val icons = this.map { it.iconSet.fill }
        val moodNames = this.map { it.title }
        return when (size) {
            1 -> MoodChipContent(iconRes = icons, title = moodNames.first())
            2 -> MoodChipContent(
                iconRes = icons,
                title = UiText.Combined(format = "%s, %s", uiTexts = moodNames.toTypedArray())
            )

            else -> {
                val extraElementCount = size - 2
                MoodChipContent(
                    iconRes = icons,
                    title = UiText.Combined(
                        format = "%s, %s +$extraElementCount",
                        uiTexts = moodNames.take(2).toTypedArray()
                    )
                )
            }
        }
    }

    private fun Flow<List<Echo>>.filterByMoodAndTopics(): Flow<List<Echo>> {
        return combine(
            this,
            selectedTopicFilters,
            selectedMoodFilters
        ) { echos, topicFilters, moodFilters ->
            echos.filter { echo ->
                val matchesMoodFilter =
                    moodFilters.takeIf { it.isNotEmpty() }?.any { it.name == echo.mood.name }
                        ?: true
                val matchesTopicFilter =
                    topicFilters.takeIf { it.isNotEmpty() }?.any { it in echo.topics } ?: true
                matchesTopicFilter && matchesMoodFilter
            }
        }
    }

    @SuppressLint("NewApi")
    private fun Flow<List<EchoUi>>.groupByRelativeDate(): Flow<Map<UiText, List<EchoUi>>> {
        val formatter = DateTimeFormatter.ofPattern("dd MMM")
        val today = LocalDate.now()
        return map { echos ->
            echos.groupBy { echo ->
                LocalDate.ofInstant(echo.recordedAt, ZoneId.systemDefault())
            }.mapValues { (_, echos) ->
                echos.sortedByDescending { it.recordedAt }
            }.toSortedMap(compareByDescending { it }).mapKeys { (date, _) ->
                when (date) {
                    today -> UiText.StringResource(R.string.today)
                    today.minusDays(1) -> UiText.StringResource(R.string.yesterday)
                    else -> UiText.Dynamic(date.format(formatter))
                }
            }
        }
    }
}