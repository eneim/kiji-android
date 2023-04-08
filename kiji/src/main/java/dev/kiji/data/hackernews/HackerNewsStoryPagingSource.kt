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

package dev.kiji.data.hackernews

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.kiji.core.domain.ResultInteractor
import dev.kiji.data.entities.Story
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*

internal class HackerNewsStoryPagingSource(
    private val ids: List<Long>,
    private val fetcher: ResultInteractor<Long, Story?>,
    private val dispatcher: CoroutineDispatcher,
) : PagingSource<Int, Story>() {

    private val indices = ids.indices

    @Suppress("ReturnCount")
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

        val asyncData: List<Deferred<Story?>> = ids
            .subList(loadFromIndex, loadToIndex)
            .map { itemId ->
                async {
                    fetcher.executeSync(itemId)
                }
            }

        return@withContext try {
            val data = asyncData.awaitAll().filterNotNull()
            LoadResult.Page(
                data = data,
                prevKey = prevPageKey,
                nextKey = nextPageKey
            )
        } catch (ignored: Throwable) {
            Napier.w("PagingSource failed.", ignored)
            LoadResult.Error(ignored)
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
        val EMPTY_PAGE: LoadResult.Page<Int, Story> =
            LoadResult.Page(emptyList(), null, null)
    }
}
