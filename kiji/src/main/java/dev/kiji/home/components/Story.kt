/*
 * Copyright (c) 2022 Nam Nguyen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.kiji.home.components

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import dev.kiji.core.data.entities.Story
import dev.kiji.core.model.Action

@Composable
fun Story(
    story: Story?,
    currentTimeMillis: Long,
    onAction: (Action<Story>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyLine = 4.dp
    val data: StoryCellData = remember(story, currentTimeMillis) {
        if (story != null) {
            val creation = buildAnnotatedString {
                append(
                    DateUtils.getRelativeTimeSpanString(
                        story.created,
                        currentTimeMillis,
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_RELATIVE,
                    ).toString()
                )
            }
            val footer = buildAnnotatedString {
                val user = story.author?.handle
                if (!user.isNullOrBlank()) {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("$user・")
                    }
                }
                append(creation)
            }
            StoryCellData(
                title = AnnotatedString(story.title),
                header = footer,
                footer = AnnotatedString(story.website.orEmpty()),
            )
        } else {
            StoryCellData()
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                if (story != null) onAction(Action.ReadNow(story))
            },
    ) {
        Column(
            modifier = Modifier.padding(keyLine * 4),
            verticalArrangement = Arrangement.spacedBy(keyLine),
        ) {
            Text(
                text = data.header,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.placeholder(
                    visible = story == null,
                    highlight = PlaceholderHighlight.shimmer(),
                )
            )

            Text(
                text = data.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .placeholder(
                        visible = story == null,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
            )

            if (data.footer.isNotBlank()) {
                Text(
                    text = data.footer,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.placeholder(
                        visible = story == null,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
                )
            }
        }
    }
}

@Stable
private data class StoryCellData(
    val title: AnnotatedString = AnnotatedString("Sample title."),
    val header: AnnotatedString = AnnotatedString("Author and creation time."),
    val footer: AnnotatedString = AnnotatedString("Website or source."),
)
