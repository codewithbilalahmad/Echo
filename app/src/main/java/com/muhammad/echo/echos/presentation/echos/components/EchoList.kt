package com.muhammad.echo.echos.presentation.echos.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.echo.echos.presentation.echos.models.EchoDaySection
import com.muhammad.echo.echos.presentation.echos.models.RelativePosition
import com.muhammad.echo.echos.presentation.echos.models.TrackSizeInfo

@Composable
fun EchoList(
    modifier: Modifier = Modifier,
    sections: List<EchoDaySection>,
    onPlayClick: (echoId : Int) -> Unit,
    onPauseClick: () -> Unit,onResumeClick : () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit,
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(16.dp)) {
        sections.forEachIndexed { sectionIndex, (dateHeader, echos) ->
            stickyHeader {
                if (sectionIndex > 0) {
                    Spacer(Modifier.height(16.dp))
                }
                Text(
                    text = dateHeader.asString().uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
            }
            itemsIndexed(items = echos, key = {_, echo ->
                echo.id
            }){index, echo ->
                EchoTimelineItem(echoUi = echo, relativePosition = when{
                    index == 0 && echos.size == 1 -> RelativePosition.SINGLE_ENTRY
                    index == 0 -> RelativePosition.FIRST
                    echos.lastIndex == index -> RelativePosition.LAST
                    else -> RelativePosition.IN_BETWEEN
                }, onPlayClick = {
                    onPlayClick(echo.id)
                }, onPauseClick = {
                    onPauseClick()
                }, onTrackSizeAvailable = onTrackSizeAvailable, onResumeClick = {
                    onResumeClick()
                })
            }
        }
    }
}
