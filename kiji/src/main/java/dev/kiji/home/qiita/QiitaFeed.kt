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

package dev.kiji.home.qiita

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import dev.kiji.core.data.entities.Story
import dev.kiji.core.model.Action
import dev.kiji.home.components.Story

@Composable
fun QiitaFeed(
    data: SnapshotStateList<Story>,
    currentTimeMillis: Long,
    modifier: Modifier = Modifier,
    onAction: (Action<Story>) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        userScrollEnabled = data.isNotEmpty(),
        modifier = modifier,
        contentPadding = WindowInsets.systemBars.asPaddingValues(),
    ) {
        if (data.isEmpty()) {
            items(100) {
                Story(
                    story = null,
                    currentTimeMillis = currentTimeMillis,
                    onAction = onAction,
                )

                Divider()
            }
        } else {
            items(data) {
                Story(
                    story = it,
                    currentTimeMillis = currentTimeMillis,
                    onAction = onAction,
                )

                Divider()
            }
        }
    }
}