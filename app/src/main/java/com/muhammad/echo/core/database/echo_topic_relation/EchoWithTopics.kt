package com.muhammad.echo.core.database.echo_topic_relation

import androidx.room.*
import com.muhammad.echo.core.database.echo.EchoEntity
import com.muhammad.echo.core.database.topic.TopicEntity


@Entity(primaryKeys = ["echoId","topic"])
data class EchoTopicCrossRef(
    val echoId : Int, val topic : String
)
data class EchoWithTopics(
    @Embedded val echo : EchoEntity,
    @Relation(
        parentColumn = "echoId",
        entityColumn = "topic",
        associateBy = Junction(value = EchoTopicCrossRef::class)
    )
    val topics : List<TopicEntity>
)