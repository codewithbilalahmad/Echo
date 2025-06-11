package com.muhammad.echo.echos.presentation.echos.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.muhammad.echo.R

@Composable
fun EchoExpandableText(modifier: Modifier = Modifier, text: String, collapsedMaxLine: Int = 3) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var isClickable by remember {
        mutableStateOf(false)
    }
    var lastCharacterIndex by remember { mutableIntStateOf(0) }
    val showMoreText = stringResource(R.string.show_more)
    val primary = MaterialTheme.colorScheme.primary
    val textToShow = remember(text, isClickable, isExpanded) {
        buildAnnotatedString {
            when {
                isClickable && !isExpanded -> {
                    val adjustedText = text.substring(startIndex = 0, endIndex = lastCharacterIndex)
                        .drop(showMoreText.length + 3).dropLastWhile {
                            Character.isWhitespace(it) || it == '.'
                        }
                    append(adjustedText)
                    append("...")
                    withStyle(style = SpanStyle(color = primary, fontWeight = FontWeight.Bold)) {
                        append(showMoreText)
                    }
                }

                else -> append(text)
            }
        }
    }
    Text(
        text = textToShow,
        maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isClickable, interactionSource = null, indication = null) {
                isExpanded = !isExpanded
            }
            .animateContentSize(), onTextLayout = { result ->
            if (!isExpanded && result.hasVisualOverflow) {
                isClickable = true
                lastCharacterIndex = result.getLineEnd(lineIndex = collapsedMaxLine - 1)
            }
        }
    )
}
