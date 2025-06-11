package com.muhammad.echo.core.database

import androidx.room.*
import com.muhammad.echo.core.database.echo.EchoDao
import com.muhammad.echo.core.database.echo.EchoEntity
import com.muhammad.echo.core.database.echo.FloatListTypeConverter
import com.muhammad.echo.core.database.echo.MoodTypeConverter
import com.muhammad.echo.core.database.echo_topic_relation.EchoTopicCrossRef
import com.muhammad.echo.core.database.topic.TopicEntity

@Database(
    entities = [EchoEntity::class, TopicEntity::class, EchoTopicCrossRef::class], version = 2
)
@TypeConverters(MoodTypeConverter::class, FloatListTypeConverter::class)
abstract class EchoDatabase : RoomDatabase(){
    abstract val echoDao : EchoDao
}
