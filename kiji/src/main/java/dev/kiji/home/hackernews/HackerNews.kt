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

package dev.kiji.home.hackernews

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import dev.kiji.core.data.entities.Story
import dev.kiji.core.model.Action
import dev.kiji.home.components.Story

@Composable
fun HackerNews(
    data: LazyPagingItems<Story>,
    currentTimeMillis: Long,
    onAction: (Action<Story>) -> Unit,
) {
    LazyColumn(
        userScrollEnabled = data.itemCount > 0,
    ) {
        if (data.itemCount == 0) {
            items(100) {
                Story(
                    story = null,
                    currentTimeMillis = currentTimeMillis,
                    onAction = onAction,
                )

                Divider()
            }
        } else {
            items(data) { item: Story? ->
                Story(
                    story = item,
                    currentTimeMillis = currentTimeMillis,
                    onAction = onAction,
                )

                Divider()
            }
        }
    }
}
