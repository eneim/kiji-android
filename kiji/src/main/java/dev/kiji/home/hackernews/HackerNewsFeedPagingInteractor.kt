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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.kiji.core.data.asResult
import dev.kiji.core.data.entities.Story
import dev.kiji.core.data.hackernews.HackerNewsApi
import dev.kiji.core.data.hackernews.HackerNewsStoryPagingSource
import dev.kiji.core.domain.PagingDataInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class HackerNewsFeedPagingInteractor(
    private val api: HackerNewsApi,
) : PagingDataInteractor<HackerNewsFeedPagingInteractor.Params, Story>() {

    override suspend fun createObservable(params: Params): Flow<PagingData<Story>> {
        val getIds = when (params.type) {
            HackerNewsFeedType.TopStories -> api.getTopStories()
            HackerNewsFeedType.NewStories -> api.getNewStories()
            HackerNewsFeedType.AskStories -> api.getAskStories()
            HackerNewsFeedType.BestStories -> api.getBestStories()
            HackerNewsFeedType.JobStories -> api.getJobStories()
            HackerNewsFeedType.ShowStories -> api.getShowStories()
        }

        val ids: List<Long> = getIds.asResult().getOrThrow()

        return Pager(
            config = params.pagingConfig,
            initialKey = null,
            pagingSourceFactory = {
                HackerNewsStoryPagingSource(
                    api = api,
                    ids = ids,
                    dispatcher = Dispatchers.IO
                )
            },
        )
            .flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val type: HackerNewsFeedType
    ) : Parameters<Story>
}
