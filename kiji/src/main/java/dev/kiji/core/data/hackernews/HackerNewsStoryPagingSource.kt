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

package dev.kiji.core.data.hackernews

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.kiji.core.data.entities.Service
import dev.kiji.core.data.entities.Story
import dev.kiji.core.data.entities.User
import dev.kiji.core.data.getResult
import dev.kiji.core.data.hackernews.entities.HackerNewsItem
import kotlinx.coroutines.*

internal class HackerNewsStoryPagingSource(
    private val api: HackerNewsApi,
    private val ids: List<Long>,
    private val dispatcher: CoroutineDispatcher,
) : PagingSource<Int, Story>() {

    private val indices = ids.indices

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val id = state.closestItemToPosition(anchorPosition)?.oid?.toLong() ?: return null
        return ids.indexOf(id)
    }

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Story> = withContext(dispatcher) {
        val loadFromIndex: Int = if (params is LoadParams.Refresh) 0 else params.key
            ?: return@withContext EMPTY_PAGE
        val loadToIndex = (loadFromIndex + PAGE_SIZE).coerceAtMost(ids.lastIndex)

        val nextPageKey = (loadToIndex + 1).takeIf { it in indices }
        val prevPageKey = (loadFromIndex - PAGE_SIZE)
            .coerceAtLeast(0)
            .takeIf { key -> key != loadFromIndex }

        val asyncData: List<Deferred<Story>> = ids
            .subList(loadFromIndex, loadToIndex)
            .map { itemId ->
                async {
                    val item = api.getItem(itemId).getResult().getOrThrow()
                    mapStory(item)
                }
            }

        return@withContext try {
            val data = asyncData.awaitAll()
            LoadResult.Page(
                data = data,
                prevKey = prevPageKey,
                nextKey = nextPageKey
            )
        } catch (ignored: Throwable) {
            ignored.printStackTrace()
            LoadResult.Error(ignored)
        }
    }

    private suspend fun mapStory(item: HackerNewsItem): Story {
        val user = if (item.author != null) {
            api.getUser(item.author).getResult().getOrNull()?.let {
                val userUrl = "https://news.ycombinator.com/user?id=${it.id}"
                User(
                    iid = userUrl,
                    handle = it.id,
                    url = userUrl,
                    image = null,
                    created = it.createdTimestampMillis,
                    service = Service.HackerNews,
                )
            }
        } else {
            null
        }

        val linkToService = "https://news.ycombinator.com/item?id=${item.id}"
        val linkToOriginal = item.url.orEmpty()
        return Story(
            iid = linkToService,
            oid = item.id.toString(),
            url = linkToService,
            link = linkToOriginal,
            title = item.title.orEmpty(),
            images = emptyList(),
            created = checkNotNull(item.createdTimestampMillis),
            updated = checkNotNull(item.createdTimestampMillis),
            author = user,
            service = Service.HackerNews
        )
    }

    private companion object {
        const val PAGE_SIZE = 20
        val EMPTY_PAGE: LoadResult.Page<Int, Story> =
            LoadResult.Page(emptyList(), null, null)
    }
}
