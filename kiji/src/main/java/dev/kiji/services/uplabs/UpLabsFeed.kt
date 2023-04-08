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

package dev.kiji.services.uplabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import dev.kiji.core.data.entities.Story
import dev.kiji.core.model.Action
import dev.kiji.home.components.ImageStory

@ExperimentalFoundationApi
@Composable
fun UpLabsFeed(
    data: LazyPagingItems<Story>,
    currentTimeMillis: Long,
    modifier: Modifier = Modifier,
    onAction: (Action<Story>) -> Unit,
) {
    val groups = remember(data.itemSnapshotList) {
        derivedStateOf {
            data.itemSnapshotList.items.groupBy(Story::groupKey)
        }
    }

    LazyColumn(
        state = rememberLazyListState(),
        contentPadding = WindowInsets.systemBars.asPaddingValues(),
        modifier = modifier,
    ) {
        groups.value.forEach { (groupKey, stories) ->
            if (groupKey != null) {
                stickyHeader(key = groupKey) {
                    Surface(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    ) {
                        Text(
                            text = groupKey.toString(),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            items(
                items = stories,
                key = { it.iid },
                itemContent = { story ->
                    ImageStory(
                        story = story,
                        currentTimeMillis = currentTimeMillis,
                        onAction = onAction,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            )
        }
    }
}
