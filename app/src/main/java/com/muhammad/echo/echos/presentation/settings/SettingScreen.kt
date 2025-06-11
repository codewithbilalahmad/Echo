package com.muhammad.echo.echos.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muhammad.echo.R
import com.muhammad.echo.core.presentation.designsystem.theme.bgGradient
import com.muhammad.echo.core.presentation.util.defaultShadow
import com.muhammad.echo.echos.presentation.settings.components.DefaultTopicSelectorCard
import com.muhammad.echo.echos.presentation.settings.components.MoodCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(onGoBack: () -> Unit, viewModel: SettingsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center)
            )
        }, navigationIcon = {
            IconButton(onClick = {
                onGoBack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_back)
                )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.bgGradient)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MoodCard(selectedMood = state.selectedMood, onMoodClick = { mood ->
                viewModel.onAction(SettingAction.OnMoodClick(mood))
            }, modifier = Modifier.defaultShadow(shape = RoundedCornerShape(8.dp)))
            DefaultTopicSelectorCard(topics = state.topics, searchText = state.searchText, topicSuggestions = state.suggestedTopics, showCreateTopicOption = state.showCreateTopicOption, showSuggestionDropDown = state.isTopicSuggestionsVisible, canInputText = state.isTopicTextInputVisible, onSearchTextChange = {newValue ->
                viewModel.onAction(SettingAction.OnSearchTextChange(newValue))
            }, onToggleCanInputText = {
                viewModel.onAction(SettingAction.OnAddButtonClick)
            }, onAddTopicClick = {topic ->
                viewModel.onAction(SettingAction.OnSelectTopicClick(topic))
            }, onRemoveTopicClick = {topic ->
                viewModel.onAction(SettingAction.OnRemoveTopicClick(topic))
            }, onDismissSuggestionDropDown = {
                viewModel.onAction(SettingAction.OnDismissTopicDropDown)
            }, modifier = Modifier.defaultShadow(RoundedCornerShape(8.dp)))
        }
    }
}
