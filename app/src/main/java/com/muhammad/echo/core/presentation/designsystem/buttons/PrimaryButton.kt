package com.muhammad.echo.core.presentation.designsystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.muhammad.echo.core.presentation.designsystem.theme.buttonGradient

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    Button(
        onClick = onClick, enabled = enabled, colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline
        ), modifier = modifier
            .clip(CircleShape)
            .background(
                brush = if (enabled) MaterialTheme.colorScheme.buttonGradient else SolidColor(
                    MaterialTheme.colorScheme.surfaceVariant
                )
            )
    ) {
        leadingIcon?.invoke()
        if (leadingIcon != null) {
            Spacer(Modifier.width(6.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline
        )
    }
}