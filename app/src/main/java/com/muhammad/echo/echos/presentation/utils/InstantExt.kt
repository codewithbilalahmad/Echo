package com.muhammad.echo.echos.presentation.utils

import android.annotation.SuppressLint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
fun Instant.toReadableTime() : String{
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.atZone(ZoneId.systemDefault()).format(formatter)
}