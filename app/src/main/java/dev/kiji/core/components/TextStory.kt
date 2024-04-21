/*
 * Copyright (C) 2023 Nam Nguyen, nam@ene.im.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.kiji.core.components

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.kiji.R
import dev.kiji.core.model.StoryEvent
import dev.kiji.core.model.StoryUiModel
import dev.kiji.data.entities.Story
import io.github.aakira.napier.Napier
import io.github.fornewid.placeholder.foundation.PlaceholderHighlight
import io.github.fornewid.placeholder.foundation.placeholder
import io.github.fornewid.placeholder.material.shimmer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TextStory(
  model: StoryUiModel,
  currentTimeMillis: Long,
  modifier: Modifier = Modifier,
  showFooter: Boolean = true,
) {
  val keyLine = 4.dp
  val story: Story? = remember { model.state }.value
  val data: TextStoryCellData = remember(story, currentTimeMillis) {
    if (story != null) {
      val creation = buildAnnotatedString {
        append(
          DateUtils.getRelativeTimeSpanString(
            story.createdMillis,
            currentTimeMillis,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE,
          ),
        )
      }
      val header = buildAnnotatedString {
        val user = story.author?.handle
        if (!user.isNullOrBlank()) {
          withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
            append("$user・")
          }
        }
        append(creation)
      }
      TextStoryCellData(
        title = AnnotatedString(story.title),
        header = header,
        footer = AnnotatedString(story.website.orEmpty()),
      )
    } else {
      TextStoryCellData()
    }
  }

  val coroutineScope = rememberCoroutineScope()

  Surface(
    modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .clickable {
            if (story != null) {
                coroutineScope.launch(Dispatchers.Main.immediate) {
                    model.event(StoryEvent.Open(story))
                }
            }
        },
  ) {
    Column(
      modifier = Modifier.padding(keyLine * 4),
      verticalArrangement = Arrangement.spacedBy(keyLine),
    ) {
      Text(
        text = data.header,
        style = MaterialTheme.typography.body1.copy(
          fontWeight = FontWeight.Normal,
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
          .placeholder(
            visible = story == null,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
            highlight = PlaceholderHighlight.shimmer(),
          ),
      )

      Text(
        text = data.title,
        style = MaterialTheme.typography.body2,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .placeholder(
                visible = story == null,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                highlight = PlaceholderHighlight.shimmer(),
            ),
      )

      if (showFooter && data.footer.isNotBlank()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          val iconUrl = story?.faviconUrl
          if (iconUrl != null) {
            AsyncImage(
              model = ImageRequest.Builder(LocalContext.current)
                .data(iconUrl)
                .crossfade(true)
                .listener(
                  onError = { _, error ->
                    Napier.w("Error: ${error.throwable}, URL: $iconUrl")
                  },
                )
                .build(),
              placeholder = painterResource(id = R.drawable.placeholder),
              error = painterResource(id = R.drawable.placeholder),
              contentDescription = null,
              contentScale = ContentScale.Fit,
              modifier = Modifier.size(16.dp),
            )

            Spacer(modifier = Modifier.width(4.dp))
          }

          Text(
            text = data.footer,
            style = MaterialTheme.typography.body1.copy(
              fontWeight = FontWeight.Normal,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(weight = 1f, fill = false)
                .wrapContentHeight()
                .placeholder(
                    visible = story == null,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    highlight = PlaceholderHighlight.shimmer(),
                ),
          )
        }
      }
    }
  }
}

@Stable
private data class TextStoryCellData(
  val title: AnnotatedString = AnnotatedString("Sample title."),
  val header: AnnotatedString = AnnotatedString("Author and creation time."),
  val footer: AnnotatedString = AnnotatedString("Website or source."),
)
