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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.kiji.core.data.entities.SiteMeta
import dev.kiji.core.data.entities.Story
import dev.kiji.core.domain.ItemDetailsInteractor
import dev.kiji.core.domain.PagingDataInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HackerViewsViewModel(
    private val stateHandle: SavedStateHandle, // TODO: use DataStore for persistent cache.
    private val feedInteractor: PagingDataInteractor<HackerNewsFeedPagingInteractor.Params, Story>,
    private val itemInteractor: ItemDetailsInteractor<Long?, Pair<Story, SiteMeta?>?>,
) : ViewModel() {

    private val feedType: StateFlow<HackerNewsFeedType> = stateHandle
        .getStateFlow(KEY_FEED_TYPE, HackerNewsFeedType.TopStories)

    private val storyId: StateFlow<String?> = stateHandle
        .getStateFlow(KEY_CURRENT_STORY_ID, null)

    val feedData: Flow<PagingData<Story>> = feedInteractor.flow.cachedIn(viewModelScope)

    val currentStory: StateFlow<Pair<Story, SiteMeta?>?> = itemInteractor.flow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            feedType
                .mapLatest { HackerNewsFeedPagingInteractor.Params(PAGING_CONFIG, it) }
                .collectLatest { params -> feedInteractor(params) }
        }

        viewModelScope.launch(Dispatchers.IO) {
            storyId.collectLatest { id ->
                val itemId: Long? = id?.toLong()
                itemInteractor(itemId)
            }
        }
    }

    fun setFeedType(type: HackerNewsFeedType) {
        stateHandle[KEY_FEED_TYPE] = type
    }

    fun setCurrentStory(story: Story? = null) = if (story != null) {
        stateHandle[KEY_CURRENT_STORY_ID] = story.oid
    } else {
        stateHandle[KEY_CURRENT_STORY_ID] = null
    }

    companion object {
        private const val KEY_FEED_TYPE = "HackerNewsFeedType"
        private const val KEY_CURRENT_STORY_ID = "HackerNewsStoryId"
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
        )
    }
}
