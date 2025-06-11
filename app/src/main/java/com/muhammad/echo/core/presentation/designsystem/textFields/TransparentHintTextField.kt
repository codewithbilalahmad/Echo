package com.muhammad.echo.core.presentation.designsystem.textFields

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun TransparentHintTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    hintText: String? = null,
    hintColor: Color = MaterialTheme.colorScheme.outlineVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    BasicTextField(
        value = text,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        modifier = modifier,
        textStyle = textStyle,
        maxLines = maxLines,
        singleLine = singleLine,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions, decorationBox = {innerTextField ->
            Box(contentAlignment = Alignment.CenterStart){
                if(text.isBlank() && hintText != null){
                    Text(text = hintText, color = hintColor, style=  textStyle)
                } else{
                    innerTextField()
                }
            }
        }
    )
}