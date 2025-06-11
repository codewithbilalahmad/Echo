package com.muhammad.echo.echos.presentation.echos.models

import com.muhammad.echo.R
import com.muhammad.echo.core.presentation.util.UiText

data class MoodChipContent(
    val iconRes : List<Int> = emptyList(),
    val title : UiText = UiText.StringResource(R.string.all_moods)
)