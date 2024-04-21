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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import dev.kiji.core.components.ImageStory
import dev.kiji.data.entities.Story
import io.github.fornewid.placeholder.foundation.PlaceholderHighlight
import io.github.fornewid.placeholder.foundation.placeholder
import io.github.fornewid.placeholder.material.shimmer

// TODO: support more PagingData configurations for UpLabs.
@ExperimentalFoundationApi
@Composable
fun UpLabsFeed(
  data: LazyPagingItems<Story>,
  modifier: Modifier = Modifier,
  onAction: suspend (Story) -> Unit,
) {
  if (data.itemCount == 0) {
    LazyVerticalGrid(
      userScrollEnabled = false,
      columns = GridCells.Adaptive(minSize = 160.dp),
      contentPadding = PaddingValues(4.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      modifier = modifier,
    ) {
      items(100) {
        ImageStory(
          story = null,
          onAction = onAction,
          modifier = Modifier
              .fillMaxWidth()
              .placeholder(
                  visible = true,
                  shape = RectangleShape,
                  color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                  highlight = PlaceholderHighlight.shimmer(),
              ),
        )
      }
    }
  } else {
    LazyVerticalStaggeredGrid(
      columns = StaggeredGridCells.Adaptive(minSize = 160.dp),
      contentPadding = PaddingValues(4.dp),
      verticalItemSpacing = 4.dp,
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      modifier = modifier,
    ) {
      items(data.itemCount) { index ->
        val story = data[index]
        ImageStory(
          story = story,
          onAction = onAction,
          modifier = Modifier.fillMaxWidth(),
        )
      }
    }
  }
}
