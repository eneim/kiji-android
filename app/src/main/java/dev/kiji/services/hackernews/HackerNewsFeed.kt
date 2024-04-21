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
package dev.kiji.services.hackernews

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import dev.kiji.core.components.TextStory
import dev.kiji.core.model.ImageStoryEvent
import dev.kiji.core.model.StoryEvent
import dev.kiji.core.model.StoryUiModel
import dev.kiji.data.entities.Story

@Composable
fun HackerNewsFeed(
  data: LazyPagingItems<Story>,
  currentTimeMillis: Long,
  modifier: Modifier = Modifier,
  onAction: (Story) -> Unit,
) {
  val state = rememberLazyListState()

  LazyColumn(
    state = state,
    userScrollEnabled = data.itemCount > 0,
    modifier = modifier,
  ) {
    val storyEvent: (StoryEvent) -> Unit = { event ->
      when (event) {
        is StoryEvent.Open -> onAction(event.story)
        is StoryEvent.Share -> {}
        is ImageStoryEvent -> Unit
      }
    }
    if (data.itemCount == 0) {
      items(100) {
        TextStory(
          model = StoryUiModel(remember { mutableStateOf(null) }, storyEvent),
          currentTimeMillis = currentTimeMillis,
        )

        Divider()
      }
    } else {
      items(data.itemCount) { index: Int ->
        val item = data[index]
        TextStory(
          model = StoryUiModel(remember { mutableStateOf(item) }, storyEvent),
          currentTimeMillis = currentTimeMillis,
        )

        Divider()
      }
    }
  }
}
