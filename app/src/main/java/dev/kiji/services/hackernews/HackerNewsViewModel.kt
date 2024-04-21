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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.kiji.core.utils.PagingDataCollector
import dev.kiji.core.utils.PagingItems
import dev.kiji.data.HackerNewsModule
import dev.kiji.data.common.CommonDataModule
import dev.kiji.data.entities.SiteMeta
import dev.kiji.data.entities.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
internal class HackerNewsViewModel(
  private val stateHandle: SavedStateHandle,
) : ViewModel() {

  private val hackerNewsApi = HackerNewsModule.provideHackerNewsApi()
  private val metaTagsApi = CommonDataModule.provideMetaTagsApi()
  private val storyFetcher = provideHackerNewsStoryFetcher(hackerNewsApi)

  private val feedInteractor = HackerNewsFeedPagingInteractor(
    api = hackerNewsApi,
    fetcher = storyFetcher,
  )
  private val itemInteractor = HackerNewsItemDetailsInteractor(
    api = metaTagsApi,
    storyFetcher = storyFetcher,
  )

  private val feedType: StateFlow<HackerNewsFeedType> = stateHandle
    .getStateFlow(KEY_FEED_TYPE, HackerNewsFeedType.TopStories)

  private val storyId: StateFlow<String?> = stateHandle
    .getStateFlow(KEY_CURRENT_STORY_ID, null)

  val feedData: Flow<PagingData<Story>> = feedInteractor.flow.cachedIn(viewModelScope)

  private val pagingDataCollector = PagingDataCollector<Story>(initial = PagingItems.empty())
  val stories: Flow<PagingItems<Story>> = pagingDataCollector.items

  val currentStory: StateFlow<Pair<Story, SiteMeta?>?> = itemInteractor.flow
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(),
      initialValue = null,
    )

  init {
    viewModelScope.launch(Dispatchers.IO) {
      feedType
        .mapLatest { HackerNewsFeedPagingInteractor.Params(PAGING_CONFIG, it) }
        .collectLatest { params -> feedInteractor(params) }
    }

    viewModelScope.launch(Dispatchers.IO) {
      storyId.collectLatest { id -> itemInteractor(id?.toLong()) }
    }

    viewModelScope.launch(Dispatchers.IO) {
      feedData.collectLatest(pagingDataCollector::submitData)
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
