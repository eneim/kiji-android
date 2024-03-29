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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.kiji.data.entities.Story
import kotlinx.coroutines.launch

@Composable
fun ImageStory(
  story: Story?,
  onAction: suspend (Story) -> Unit,
  modifier: Modifier = Modifier,
  aspectRatio: Float? = null,
) {
  val image = story?.images?.firstOrNull()
  val ratio: Float = aspectRatio ?: image?.ratio ?: 1F
  val coroutineScope = rememberCoroutineScope()
  Surface(
    modifier = modifier
      .aspectRatio(ratio)
      .clickable {
        if (story != null) {
          coroutineScope.launch {
            onAction(story)
          }
        }
      },
  ) {
    AsyncImage(
      model = ImageRequest.Builder(LocalContext.current)
        .data(image?.url)
        .crossfade(true)
        .build(),
      contentDescription = story?.title,
      modifier = modifier.fillMaxSize(),
      contentScale = ContentScale.Crop,
    )
  }
}
