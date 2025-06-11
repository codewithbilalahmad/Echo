package com.muhammad.echo.core.database.topic

import androidx.room.*

@Entity
data class TopicEntity(
    @PrimaryKey(autoGenerate = false)
    val topic : String
)
