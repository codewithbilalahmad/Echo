package com.muhammad.echo.echos.presentation.settings

import com.muhammad.echo.echos.presentation.models.MoodUi

sealed interface SettingAction{
    data class OnSearchTextChange(val text : String) : SettingAction
    data class OnSelectTopicClick(val topic : String) : SettingAction
    data class OnRemoveTopicClick(val topic : String) : SettingAction
    data object OnDismissTopicDropDown : SettingAction
    data object OnAddButtonClick : SettingAction
    data class OnMoodClick(val mood : MoodUi) : SettingAction
}