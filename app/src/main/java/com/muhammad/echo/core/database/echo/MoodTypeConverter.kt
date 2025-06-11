package com.muhammad.echo.core.database.echo

import androidx.room.*
import com.muhammad.echo.echos.domain.echo.Mood

class MoodTypeConverter{
    @TypeConverter
    fun fromMood(mood : Mood) : String{
        return mood.name
    }
    @TypeConverter
    fun toMood(name : String) : Mood{
        return Mood.valueOf(name)
    }
}