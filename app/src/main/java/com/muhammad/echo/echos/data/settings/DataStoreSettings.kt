package com.muhammad.echo.echos.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.muhammad.echo.echos.domain.echo.Mood
import com.muhammad.echo.echos.domain.settings.SettingsPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class DataStoreSettings(private val context: Context) : SettingsPreferences {
    companion object {
        private val Context.settingsPreferences by preferencesDataStore(name = "settings_preferences")
    }
    private val topicKey = stringSetPreferencesKey("topics")
    private val moodKey = stringPreferencesKey("mood")
    override suspend fun saveDefaultTopics(topics: List<String>) {
        context.settingsPreferences.edit {prefs ->
            prefs[topicKey] = topics.toSet()
        }
    }

    override fun observeDefaultTopics(): Flow<List<String>> {
        return context.settingsPreferences.data.map {prefs ->
            prefs[topicKey]?.toList() ?: emptyList()
        }.distinctUntilChanged()
    }

    override suspend fun saveDefaultMood(mood: Mood) {
        context.settingsPreferences.edit {prefs ->
            prefs[moodKey] = mood.name
        }
    }

    override fun observeDefaultMood(): Flow<Mood> {
        return context.settingsPreferences.data.map { prefs ->
            prefs[moodKey]?.let {mood ->
                Mood.valueOf(mood)
            } ?: Mood.NEUTRAL
        }.distinctUntilChanged()
    }

}