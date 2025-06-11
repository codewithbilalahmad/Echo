package com.muhammad.echo.echos.presentation.create_echo

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.muhammad.echo.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muhammad.echo.core.presentation.designsystem.buttons.PrimaryButton
import com.muhammad.echo.core.presentation.designsystem.buttons.SecondaryButton
import com.muhammad.echo.core.presentation.designsystem.textFields.TransparentHintTextField
import com.muhammad.echo.core.presentation.designsystem.theme.secondary70
import com.muhammad.echo.core.presentation.designsystem.theme.secondary95
import com.muhammad.echo.core.presentation.util.ObserveAsEvents
import com.muhammad.echo.echos.presentation.components.EchoMoodPlayer
import com.muhammad.echo.echos.presentation.create_echo.components.EchoTopicsRow
import com.muhammad.echo.echos.presentation.create_echo.components.SelectMoodSheet
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEchoScreen(onConfirmLeave: () -> Unit, viewModel: CreateEchoViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val mood = state.mood
    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CreateEchoEvent.EchoSuccessfullySaved -> {
                onConfirmLeave()
            }

            CreateEchoEvent.FailedToSaveFile -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_couldnt_save_file),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    BackHandler(enabled = !state.showConfirmLeaveDialog) {
        onConfirmLeave()
    }
    Scaffold(containerColor = MaterialTheme.colorScheme.surface, topBar = {
        CenterAlignedTopAppBar(title = {
            Text(text = stringResource(R.string.new_entry), textAlign = TextAlign.Center)
        }, navigationIcon = {
            IconButton(onClick = {
                onConfirmLeave()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
            }
        })
    }) { paddingValues ->
        val descriptionFocusRequester = remember {
            FocusRequester()
        }
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (mood != null) {
                    Image(
                        imageVector = ImageVector.vectorResource(mood.iconSet.fill),
                        contentDescription = null, modifier = Modifier
                            .height(32.dp)
                            .clickable {
                                viewModel.onAction(CreateEchoAction.OnSelectMoodClick)
                            }, contentScale = ContentScale.FillHeight
                    )
                } else {
                    FilledIconButton(
                        onClick = {
                            viewModel.onAction(CreateEchoAction.OnSelectMoodClick)
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondary95,
                            contentColor = MaterialTheme.colorScheme.secondary70
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add_mood)
                        )
                    }
                }
                TransparentHintTextField(
                    text = state.titleText,
                    onValueChange = { newValue ->
                        viewModel.onAction(CreateEchoAction.OnTitleTextChange(newValue))
                    },
                    modifier = Modifier.weight(1f),
                    hintText = stringResource(R.string.add_title),
                    textStyle = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onDone = {
                        descriptionFocusRequester.requestFocus()
                    })
                )
            }
            EchoMoodPlayer(
                moodUi = state.mood,
                playbackState = state.playbackState,
                playerProgress = { state.durationPlayedRatio },
                durationPlayed = state.durationPlayed,
                powerRatios = state.playbackAmplitudes,
                onPlayClick = {
                    viewModel.onAction(CreateEchoAction.OnPlayAudioClick)
                },
                onPauseClick = {
                    viewModel.onAction(CreateEchoAction.OnPauseAudioClick)
                },
                onTrackSizeAvailable = { trackSize ->
                    viewModel.onAction(CreateEchoAction.OnTrackSizeAvailable(trackSize))
                },
                totalPlaybackDuration = state.playbackTotalDuration, onResumeClick = {
                    viewModel.onAction(CreateEchoAction.OnResumeAudioClick)
                }
            )
            EchoTopicsRow(
                topics = state.topics,
                addTopicText = state.addTopicText,
                showTopicSuggestion = state.showTopicSuggestion,
                showCreateTopicOption = state.showCreateTopicOption,
                onTopicClick = { topic ->
                    viewModel.onAction(CreateEchoAction.OnTopicClick(topic))
                },
                onDismissTopicSuggestion = {
                    viewModel.onAction((CreateEchoAction.OnDismissTopicSuggestions))
                },
                onRemoveTopicClick = { topic ->
                    viewModel.onAction(CreateEchoAction.OnRemoveTopicClick(topic))
                },
                searchResults = state.searchResults,
                onAddTopicTextChange = { topic ->
                    viewModel.onAction(CreateEchoAction.OnAddTopicTextChange(topic))
                })
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = stringResource(R.string.add_description),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                TransparentHintTextField(
                    text = state.noteText,
                    onValueChange = { newValue ->
                        viewModel.onAction(CreateEchoAction.OnNoteTextChange(newValue))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(descriptionFocusRequester),
                    hintText = stringResource(R.string.add_description),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecondaryButton(text = stringResource(R.string.cancel), onClick = {
                    viewModel.onAction(CreateEchoAction.OnCancelClick)
                }, modifier = Modifier.fillMaxHeight())
                PrimaryButton(text = stringResource(R.string.save), onClick = {
                    viewModel.onAction(CreateEchoAction.OnSaveClick)
                }, enabled = state.canSaveEcho, leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                })
            }
        }
        if (state.showMoodSelector) {
            SelectMoodSheet(selectedMood = state.selectedMood, onMoodClick = { mood ->
                viewModel.onAction(CreateEchoAction.OnMoodClick(mood))
            }, onDismiss = {
                viewModel.onAction(CreateEchoAction.OnDismissMoodSelector)
            }, onConfirmClick = {
                viewModel.onAction(CreateEchoAction.OnConfirmMood)
            })
        }
        if (state.showConfirmLeaveDialog) {
            AlertDialog(onDismissRequest = {
                viewModel.onAction(CreateEchoAction.OnDismissConfirmLeaveDialog)
            }, confirmButton = {
                TextButton(onClick = onConfirmLeave) {
                    Text(
                        text = stringResource(R.string.discard),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }, dismissButton = {
                TextButton(onClick = {
                    viewModel.onAction(CreateEchoAction.OnDismissConfirmLeaveDialog)
                }) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
            }, title = {
                Text(text = stringResource(R.string.discard_recording))
            }, text = {
                Text(text = stringResource(R.string.this_cannot_be_undone))
            })
        }
    }
}