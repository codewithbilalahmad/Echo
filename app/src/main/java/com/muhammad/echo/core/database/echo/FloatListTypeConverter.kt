package com.muhammad.echo.core.database.echo

import androidx.room.TypeConverter

class FloatListTypeConverter{
    @TypeConverter
    fun fromList(values : List<Float>) : String{
        return values.joinToString(",")
    }
    @TypeConverter
    fun toList(value : String) : List<Float>{
        return value.split(",").map { it.toFloat() }
    }
}