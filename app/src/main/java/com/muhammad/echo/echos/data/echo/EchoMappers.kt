package com.muhammad.echo.echos.data.echo

import com.muhammad.echo.core.database.echo.EchoEntity
import com.muhammad.echo.core.database.echo_topic_relation.EchoWithTopics
import com.muhammad.echo.core.database.topic.TopicEntity
import com.muhammad.echo.echos.domain.echo.Echo
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds

fun EchoWithTopics.toEcho(): Echo {
    return Echo(
        mood = echo.mood,
        title = echo.title,
        topics = topics.map { it.topic },
        note = echo.note,
        audioFilePath = echo.audioFilePath,
        audioAmplitudes = echo.audioAmplitudes,
        recordedAt = Instant.ofEpochMilli(echo.recordedAt),
        audioPlaybackLength = echo.audioPlaybackLength.milliseconds, id = echo.echoId
    )
}

fun Echo.toEchoWithTopics(): EchoWithTopics {
    return EchoWithTopics(
        echo = EchoEntity(
            echoId = id ?: 0,
            title = title,
            mood = mood,
            recordedAt = recordedAt.toEpochMilli(),
            note = note,
            audioAmplitudes = audioAmplitudes,
            audioFilePath = audioFilePath,
            audioPlaybackLength = audioPlaybackLength.inWholeMilliseconds
        ), topics = topics.map {topic -> TopicEntity(topic) }
    )
}