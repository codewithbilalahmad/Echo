package com.muhammad.echo.echos.presentation.echos.models

import com.muhammad.echo.core.presentation.util.UiText
import com.muhammad.echo.echos.presentation.models.EchoUi

data class EchoDaySection(
    val dateHeader : UiText,
    val echos : List<EchoUi>
)