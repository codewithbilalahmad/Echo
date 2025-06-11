package com.muhammad.echo.echos.presentation.echos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.echo.echos.presentation.echos.models.RelativePosition
import com.muhammad.echo.echos.presentation.echos.models.TrackSizeInfo
import com.muhammad.echo.echos.presentation.models.EchoUi

private val noVerticalLineAboveIconModifier = Modifier.padding(top = 16.dp)
private val noVerticalLineBelowIconModifier = Modifier.padding(8.dp)

@Composable
fun EchoTimelineItem(
    echoUi: EchoUi,
    modifier: Modifier = Modifier,
    relativePosition: RelativePosition,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick : () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit,
) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.TopCenter) {
            if (relativePosition != RelativePosition.SINGLE_ENTRY) {
                VerticalDivider(
                    modifier = when (relativePosition) {
                        RelativePosition.FIRST -> noVerticalLineAboveIconModifier
                        RelativePosition.LAST -> noVerticalLineBelowIconModifier
                        RelativePosition.IN_BETWEEN -> Modifier
                        else -> Modifier
                    }
                )
            }
            Image(
                imageVector = ImageVector.vectorResource(echoUi.mood.iconSet.fill),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(32.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        EchoCard(
            echoUi = echoUi,
            onTrackSizeAvailable = onTrackSizeAvailable,
            onPlayClick = {
                onPlayClick()
            },
            onPauseClick ={
                onPauseClick()
            },
            modifier = Modifier.padding(vertical = 8.dp), onResumeClick = {
                onResumeClick()
            }
        )
    }
}

