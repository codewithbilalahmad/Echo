package com.muhammad.echo.echos.presentation.settings

import com.muhammad.echo.echos.presentation.models.MoodUi

data class SettingState(
    val topics : List<String> = emptyList(),
    val selectedMood : MoodUi?=null,
    val searchText : String = "",
    val suggestedTopics : List<String> = emptyList(),
    val isTopicSuggestionsVisible : Boolean = false,
    val showCreateTopicOption : Boolean = false,
    val isTopicTextInputVisible : Boolean = false
)