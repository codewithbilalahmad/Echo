package com.muhammad.echo.echos.presentation.echos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.muhammad.echo.R
import com.muhammad.echo.core.presentation.designsystem.theme.Microphone
import com.muhammad.echo.core.presentation.designsystem.theme.buttonGradient
import com.muhammad.echo.core.presentation.designsystem.theme.buttonGradientPressed
import com.muhammad.echo.core.presentation.designsystem.theme.primary90
import com.muhammad.echo.core.presentation.designsystem.theme.primary95
import com.muhammad.echo.echos.presentation.echos.models.rememberBubbleFloatingActionButtonColors
import kotlin.math.roundToInt

@Composable
fun EchoQuickRecordFloatingActionButton(
    modifier: Modifier = Modifier,
    isQuickRecording: Boolean,
    onClick: () -> Unit,
    onLongPressStart: () -> Unit,
    onLongPressEnd: (isCancelled: Boolean) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val density = LocalDensity.current
    val cancelButtonOffset = (-100).dp
    val cancelButtonOffsetPx = with(density) {
        cancelButtonOffset.toPx()
    }
    var dragOffsetX by remember {
        mutableFloatStateOf(0f)
    }
    var needToHandleLongClickEnd by remember {
        mutableStateOf(false)
    }
    val isWithInCancelThreshold by remember(cancelButtonOffsetPx) {
        derivedStateOf { dragOffsetX <= cancelButtonOffsetPx * 0.8f }
    }
    LaunchedEffect(isWithInCancelThreshold) {
        if (isWithInCancelThreshold) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }
    val fabPositionOffset by remember {
        derivedStateOf {
            IntOffset(
                x = dragOffsetX.toInt()
                    .coerceIn(minimumValue = cancelButtonOffsetPx.roundToInt(), maximumValue = 0),
                y = 0
            )
        }
    }
    Box(contentAlignment = Alignment.Center, modifier = modifier.pointerInput(Unit) {
        detectDragGesturesAfterLongPress(onDragStart = {
            needToHandleLongClickEnd = true
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onLongPressStart()
        }, onDragEnd = {
            if (needToHandleLongClickEnd) {
                needToHandleLongClickEnd = false
                onLongPressEnd(isWithInCancelThreshold)
               dragOffsetX =  0f
            }
        }, onDragCancel = {
            if (needToHandleLongClickEnd) {
                needToHandleLongClickEnd = false
                onLongPressEnd(isWithInCancelThreshold)
               dragOffsetX =  0f
            }
        }, onDrag = { change, _ ->
            dragOffsetX += change.positionChange().x
        })
    }) {
        if (isQuickRecording) {
            Box(
                modifier = Modifier
                    .offset(x = cancelButtonOffset)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.errorContainer
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cancel_recording),
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        EchoBubbleFloatingActionButton(
            modifier = Modifier.offset { fabPositionOffset },
            showBubble = isQuickRecording,
            onClick = onClick,
            icon = {
                Icon(
                    imageVector = if (isQuickRecording) Icons.Filled.Microphone else Icons.Filled.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }, colors = rememberBubbleFloatingActionButtonColors(
                primary = if (isWithInCancelThreshold) SolidColor(Color.Red) else MaterialTheme.colorScheme.buttonGradient,
                primaryPressed = MaterialTheme.colorScheme.buttonGradientPressed,
                outerCircle = if (isWithInCancelThreshold) SolidColor(
                    MaterialTheme.colorScheme.errorContainer.copy(0.5f)
                ) else SolidColor(
                    MaterialTheme.colorScheme.primary95
                ), innerCircle = if (isWithInCancelThreshold) SolidColor(
                    MaterialTheme.colorScheme.errorContainer.copy(0.5f)
                ) else SolidColor(
                    MaterialTheme.colorScheme.primary90
                )
            )
        )
    }
}
