package com.muhammad.echo.echos.presentation.echos.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.muhammad.echo.R
import com.muhammad.echo.core.presentation.designsystem.chips.MultiChoiceChip
import com.muhammad.echo.core.presentation.designsystem.dropdown.Selectable
import com.muhammad.echo.core.presentation.designsystem.dropdown.SelectableDropDownOptionsMenu
import com.muhammad.echo.core.presentation.util.UiText
import com.muhammad.echo.echos.presentation.echos.EchosAction
import com.muhammad.echo.echos.presentation.echos.models.EchoFilterChip
import com.muhammad.echo.echos.presentation.echos.models.MoodChipContent
import com.muhammad.echo.echos.presentation.models.MoodUi

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun EchoFilterRow(
    moodChipContent: MoodChipContent,
    hasActiveMoodFilters: Boolean,
    selectedEchoFilterChip: EchoFilterChip?,
    moods: List<Selectable<MoodUi>>,
    topicChipTitle: UiText,
    hasActiveTopicFilters: Boolean,
    topics: List<Selectable<String>>,
    onAction: (EchosAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var dropDownOffset by remember {
        mutableStateOf(IntOffset.Zero)
    }
    val configuration = LocalConfiguration.current
    val dropDownMaxHeight = (configuration.screenHeightDp * 0.3f).dp
    FlowRow(
        modifier = modifier
            .padding(16.dp)
            .onGloballyPositioned {
                dropDownOffset = IntOffset(x = 0, y = it.size.height)
            },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        MultiChoiceChip(
            displayText = moodChipContent.title.asString(),
            onClick = {
                onAction(EchosAction.OnMoodChipClick)
            },
            leadingContent = {
                if (moodChipContent.iconRes.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy((-4).dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        moodChipContent.iconRes.forEach { iconRes ->
                            Image(
                                imageVector = ImageVector.vectorResource(iconRes),
                                contentDescription = moodChipContent.title.asString(),
                                modifier = Modifier.height(16.dp)
                            )
                        }
                    }
                }
            },
            isClearVisible = hasActiveMoodFilters,
            isDropDownVisible = selectedEchoFilterChip == EchoFilterChip.MOODS,
            isHighlighted = hasActiveMoodFilters || selectedEchoFilterChip == EchoFilterChip.MOODS,
            onClearClick = {
                onAction(EchosAction.OnRemoveFilters(EchoFilterChip.MOODS))
            }, dropDownMenu = {
                SelectableDropDownOptionsMenu(
                    items = moods,
                    itemDisplayText = { moodUi ->
                        moodUi.title.asString(context)
                    },
                    onDismiss = {
                        onAction(EchosAction.OnDismissMoodDropDown)
                    },
                    key = { moodUi ->
                        moodUi.title.asString(context)
                    },
                    onItemClick = { moodUi ->
                        onAction(EchosAction.OnFilterByMoodClick(moodUi.item))
                    },
                    dropDownOffset = dropDownOffset,
                    maxDropDownHeight = dropDownMaxHeight,
                    leadingIcon = { moodUi ->
                        Image(
                            imageVector = ImageVector.vectorResource(moodUi.iconSet.fill),
                            contentDescription = moodUi.title.asString()
                        )
                    })
            })
        MultiChoiceChip(
            displayText = topicChipTitle.asString(),
            onClick = {
                onAction(EchosAction.OnTopicChipClick)
            },
            isClearVisible = hasActiveTopicFilters,
            isDropDownVisible = selectedEchoFilterChip == EchoFilterChip.TOPICS,
            isHighlighted = hasActiveTopicFilters || selectedEchoFilterChip == EchoFilterChip.TOPICS,
            onClearClick = {
                onAction(EchosAction.OnRemoveFilters(EchoFilterChip.TOPICS))
            },
            dropDownMenu = {
                if (topics.isEmpty()) {
                    SelectableDropDownOptionsMenu(
                        items = listOf(
                            Selectable(
                                item = stringResource(R.string.you_don_t_have_any_topics_yet),
                                selected = false
                            )
                        ),
                        itemDisplayText = { it },
                        onDismiss = {
                            onAction(EchosAction.OnDismissTopicDropDown)
                        },
                        key = { it },
                        onItemClick = {},
                        dropDownOffset = dropDownOffset,
                        maxDropDownHeight = dropDownMaxHeight
                    )
                } else {
                    SelectableDropDownOptionsMenu(
                        items = topics,
                        itemDisplayText = { topic ->
                            topic
                        },
                        onDismiss = {
                            onAction(EchosAction.OnDismissTopicDropDown)
                        },
                        key = { it },
                        onItemClick = { topic ->
                            onAction(EchosAction.OnFilterByTopicClick(topic.item))
                        },
                        dropDownOffset = dropDownOffset,
                        maxDropDownHeight = dropDownMaxHeight,
                        leadingIcon = { topic ->
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.hashtag),
                                contentDescription = topic
                            )
                        })
                }
            })
    }
}