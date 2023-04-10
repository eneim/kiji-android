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
package dev.kiji.services.uplabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import dev.kiji.core.model.Action
import dev.kiji.data.entities.Story
import dev.kiji.home.components.ImageStory

@ExperimentalFoundationApi
@Composable
fun UpLabsFeed(
  data: LazyPagingItems<Story>,
  currentTimeMillis: Long,
  modifier: Modifier = Modifier,
  onAction: suspend (Action<Story>) -> Unit,
) {
  LazyVerticalStaggeredGrid(
    columns = StaggeredGridCells.Adaptive(minSize = 180.dp),
    contentPadding = WindowInsets.systemBars
      .add(WindowInsets(top = 4.dp, left = 4.dp, right = 4.dp))
      .asPaddingValues(),
    verticalItemSpacing = 4.dp,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    modifier = modifier,
  ) {
    items(data.itemCount) { index ->
      val story = data[index]
      ImageStory(
        story = story,
        currentTimeMillis = currentTimeMillis,
        onAction = onAction,
        modifier = Modifier
          .fillMaxWidth()
          .placeholder(
            visible = story == null,
            highlight = PlaceholderHighlight.shimmer(),
          ),
      )
    }
  }
}
