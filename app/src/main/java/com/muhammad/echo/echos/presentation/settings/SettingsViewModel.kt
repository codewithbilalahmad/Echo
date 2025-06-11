package com.muhammad.echo.echos.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.echo.echos.domain.echo.EchoDataSource
import com.muhammad.echo.echos.domain.echo.Mood
import com.muhammad.echo.echos.domain.settings.SettingsPreferences
import com.muhammad.echo.echos.presentation.models.MoodUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val echoDataSource: EchoDataSource,
) : ViewModel() {
    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(SettingState())
    val state = _state.onStart {
        if (!hasLoadedInitialData) {
            observeSettings()
            observeTopicSearchResults()
            hasLoadedInitialData = true
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SettingState())

    fun onAction(action: SettingAction) {
        when (action) {
            SettingAction.OnAddButtonClick -> onAddButtonClick()
            SettingAction.OnDismissTopicDropDown -> onDismissTopicDropDown()
            is SettingAction.OnMoodClick -> onMoodClick(action.mood)
            is SettingAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            is SettingAction.OnSearchTextChange -> onSearchTextChange(action.text)
            is SettingAction.OnSelectTopicClick -> onSelectTopic(action.topic)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeTopicSearchResults() {
        state.distinctUntilChangedBy { it.searchText }.map { it.searchText }.debounce(300)
            .flatMapLatest { query ->
                if (query.isNotBlank()) {
                    echoDataSource.searchTopics(query)
                } else emptyFlow()
            }.onEach { filteredResults ->
                _state.update {
                    val filteredNoDefaultResults = filteredResults - it.topics
                    val searchText = it.searchText.trim()
                    val isNewTopic =
                        searchText !in filteredNoDefaultResults && searchText !in it.topics && searchText.isNotBlank()
                    it.copy(
                        suggestedTopics = filteredNoDefaultResults,
                        isTopicSuggestionsVisible = filteredResults.isEmpty() || isNewTopic,
                        showCreateTopicOption = isNewTopic
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun onSearchTextChange(text: String) {
        _state.update { it.copy(searchText = text) }
    }

    private fun onDismissTopicDropDown() {
        _state.update { it.copy(isTopicSuggestionsVisible = false) }
    }

    private fun onAddButtonClick() {
        _state.update { it.copy(isTopicTextInputVisible = true) }
    }

    private fun onMoodClick(mood: MoodUi) {
        viewModelScope.launch {
            settingsPreferences.saveDefaultMood(Mood.valueOf(mood.name))
        }
    }

    private fun onSelectTopic(topic: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isTopicSuggestionsVisible = false,
                    isTopicTextInputVisible = false,
                    searchText = ""
                )
            }
            val newDefaultTopics = (state.value.topics + topic).distinct()
            settingsPreferences.saveDefaultTopics(newDefaultTopics)
        }
    }

    private fun onRemoveTopicClick(topic: String) {
        viewModelScope.launch {
            val newDefaultTopics = (state.value.topics - topic).distinct()
            settingsPreferences.saveDefaultTopics(newDefaultTopics)
        }
    }

    private fun observeSettings() {
        combine(
            settingsPreferences.observeDefaultTopics(),
            settingsPreferences.observeDefaultMood()
        ) { topics, mood ->
            _state.update { it.copy(topics = topics, selectedMood = MoodUi.valueOf(mood.name)) }
        }.launchIn(viewModelScope)
    }
}