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

package dev.kiji.core.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import dev.kiji.data.entities.Story

@Immutable
interface Event

interface UiModel<T, E : Event> {
  val state: State<T>
  val event: (E) -> Unit
}

sealed interface StoryEvent : Event {
  data class Open(val story: Story) : StoryEvent
  data class Share(val story: Story) : StoryEvent
}

sealed interface ImageStoryEvent: StoryEvent {
  data class View(val imageUrl: String): ImageStoryEvent
}

data class StoryUiModel(
  override val state: State<Story?>,
  override val event: (StoryEvent) -> Unit,
) : UiModel<Story?, StoryEvent>
