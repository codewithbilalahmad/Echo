package com.muhammad.echo.echos.presentation.echos

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muhammad.echo.core.presentation.designsystem.theme.bgGradient
import com.muhammad.echo.core.presentation.util.ObserveAsEvents
import com.muhammad.echo.core.presentation.util.isAppInForeground
import com.muhammad.echo.echos.domain.recording.RecordingDetails
import com.muhammad.echo.echos.presentation.echos.components.EchoFilterRow
import com.muhammad.echo.echos.presentation.echos.components.EchoList
import com.muhammad.echo.echos.presentation.echos.components.EchoQuickRecordFloatingActionButton
import com.muhammad.echo.echos.presentation.echos.components.EchoRecordingSheet
import com.muhammad.echo.echos.presentation.echos.components.EchosEmptyBackground
import com.muhammad.echo.echos.presentation.echos.components.EchosTopBar
import com.muhammad.echo.echos.presentation.echos.models.AudioCaptureMethod
import com.muhammad.echo.echos.presentation.echos.models.RecordingState
import org.koin.androidx.compose.koinViewModel

@Composable
fun EchoScreen(
    onNavigateToCreateEcho: (RecordingDetails) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: EchosViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val activity = (context as Activity)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when {
                isGranted && state.currentCaptureMethod == AudioCaptureMethod.STANDARD -> {
                    viewModel.onAction(EchosAction.OnAudioPermissionGranted)
                }

                !isGranted && !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.RECORD_AUDIO
                ) -> {
                    viewModel.onAction(EchosAction.OnMicPermissionPermanentlyDenied)
                }
            }
        }
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is EchosEvent.OnDoneRecording -> {
                onNavigateToCreateEcho(event.details)
            }

            is EchosEvent.RequestAudioPermission -> {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }
    val isAppInForeground by isAppInForeground()
    LaunchedEffect(isAppInForeground, state.recordingState) {
        if (state.recordingState == RecordingState.NORMAL_CAPTURE && !isAppInForeground) {
            viewModel.onAction(EchosAction.OnPauseRecordingClick)
        }
    }
    Scaffold(floatingActionButton = {
        EchoQuickRecordFloatingActionButton(
            onClick = {
                viewModel.onAction(EchosAction.OnRecordFabClick)
            },
            isQuickRecording = state.recordingState == RecordingState.QUICK_CAPTURE,
            onLongPressEnd = { cancelledRecording ->
                if (cancelledRecording) {
                    viewModel.onAction(EchosAction.OnCancelRecording)
                } else {
                    viewModel.onAction(EchosAction.OnCompleteRecording)
                }
            },
            onLongPressStart = {
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
                if (hasPermission) {
                    viewModel.onAction(EchosAction.OnRecordButtonLongClick)
                } else {
                    viewModel.onAction(EchosAction.OnRequestPermissionQuickRecording)
                }
            })
    }, topBar = {
        EchosTopBar(onSettingClick = {
            onNavigateToSettings()
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = MaterialTheme.colorScheme.bgGradient)
                .padding(paddingValues)
        ) {
            EchoFilterRow(
                moods = state.moods,
                moodChipContent = state.moodChipContent,
                topicChipTitle = state.topicChipTitle,
                onAction = viewModel::onAction,
                hasActiveMoodFilters = state.hasActiveMoodFilters,
                hasActiveTopicFilters = state.hasActiveTopicFilters,
                selectedEchoFilterChip = state.selectedEchoFilterChip,
                topics = state.topics
            )
            when {
                state.isLoadingData -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .wrapContentSize(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                !state.hasEchosRecorded -> {
                    EchosEmptyBackground(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }

                else -> {
                    EchoList(sections = state.echoDaySections, onPlayClick = { id ->
                        viewModel.onAction(EchosAction.OnPlayEchoClick(id))
                    }, onPauseClick = {
                        viewModel.onAction(EchosAction.OnPauseAudioClick)
                    }, onTrackSizeAvailable = { trackSize ->
                        viewModel.onAction(EchosAction.OnTrackSizeAvailable(trackSize))
                    }, onResumeClick = {
                        viewModel.onAction(EchosAction.OnResumeAudioClick)
                    })
                }
            }
        }
        if (state.showRecordingBottomSheet) {
            EchoRecordingSheet(
                formattedRecordDuration = state.formattedRecordDuration,
                isRecording = state.recordingState == RecordingState.NORMAL_CAPTURE,
                onDismiss = {
                    viewModel.onAction(EchosAction.OnCancelRecording)
                },
                onPauseClick = {
                    viewModel.onAction(EchosAction.OnPauseRecordingClick)
                },
                onResumeClick = {
                    viewModel.onAction(EchosAction.OnResumeRecordingClick)
                },
                onCompleteRecording = {
                    viewModel.onAction(EchosAction.OnCompleteRecording)
                })
        }
        if (state.showAllowMicAccessDialog) {
            AlertDialog(onDismissRequest = {
                viewModel.onAction(EchosAction.OnDismissMicAccessDialog)
            }, title = {
                Text(text = "Allow Microphone")
            }, text = {
                Text(text = "Allow Microphone access to Echo to record your Echo Memories, tap Confirm to open Settings then Permissions -> Microphone -> Allow Permission, and you are good to go😁!")
            }, dismissButton = {
                TextButton(onClick = {
                    viewModel.onAction(EchosAction.OnDismissMicAccessDialog)
                }) {
                    Text(text = "Denied")
                }
            }, confirmButton = {
                TextButton(onClick = {
                    viewModel.onAction(EchosAction.OnDismissMicAccessDialog)
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }) {
                    Text(text = "Confirm")
                }
            })
        }
    }
}
