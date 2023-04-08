/*
 * Copyright (c) 2023 Nam Nguyen, nam@ene.im
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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import dev.kiji.core.model.Action
import dev.kiji.data.entities.Story

@Composable
fun ImageStory(
    story: Story?,
    currentTimeMillis: Long,
    onAction: (Action<Story>) -> Unit,
    modifier: Modifier = Modifier,
    aspectRatio: Float? = null,
) {

    val image = story?.images?.firstOrNull()
    val ratio: Float = aspectRatio ?: image?.ratio ?: 1F

    Surface(
        modifier = modifier
            .aspectRatio(ratio)
            .clickable {
                if (story != null) onAction(Action.ReadNow(story))
            },
    ) {
        AsyncImage(
            model = image?.url,
            contentDescription = story?.title,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}
