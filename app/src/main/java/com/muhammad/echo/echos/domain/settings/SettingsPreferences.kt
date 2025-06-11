package com.muhammad.echo.echos.domain.settings

import com.muhammad.echo.echos.domain.echo.Mood
import kotlinx.coroutines.flow.Flow

interface SettingsPreferences{
    suspend fun saveDefaultTopics(topics : List<String>)
    fun observeDefaultTopics() : Flow<List<String>>
    suspend fun saveDefaultMood(mood : Mood)
    fun observeDefaultMood() : Flow<Mood>
}