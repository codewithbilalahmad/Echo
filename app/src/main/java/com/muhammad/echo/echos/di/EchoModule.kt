package com.muhammad.echo.echos.di

import com.muhammad.echo.echos.data.audio.AndroidAudioPlayer
import com.muhammad.echo.echos.data.echo.RoomEchoDataSource
import com.muhammad.echo.echos.data.recording.AndroidVoiceRecorder
import com.muhammad.echo.echos.data.recording.InternalRecordingStorage
import com.muhammad.echo.echos.data.settings.DataStoreSettings
import com.muhammad.echo.echos.domain.audio.AudioPlayer
import com.muhammad.echo.echos.domain.echo.EchoDataSource
import com.muhammad.echo.echos.domain.recording.RecordingStorage
import com.muhammad.echo.echos.domain.recording.VoiceRecorder
import com.muhammad.echo.echos.domain.settings.SettingsPreferences
import com.muhammad.echo.echos.presentation.create_echo.CreateEchoViewModel
import com.muhammad.echo.echos.presentation.echos.EchosViewModel
import com.muhammad.echo.echos.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val echoModule = module {
    singleOf(::AndroidVoiceRecorder) bind VoiceRecorder::class
    singleOf(::InternalRecordingStorage) bind RecordingStorage::class
    singleOf(::AndroidAudioPlayer) bind AudioPlayer::class
    singleOf(::RoomEchoDataSource) bind EchoDataSource::class
    singleOf(::DataStoreSettings) bind SettingsPreferences::class
    viewModelOf(::EchosViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::CreateEchoViewModel)
}