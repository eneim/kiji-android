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
package dev.kiji.services.uplabs

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.kiji.core.domain.ResultInteractor
import dev.kiji.data.UpLabsApi
import dev.kiji.data.entities.Story
import dev.kiji.data.uplabs.UpLabsItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class UpLabsStoryPagingSource(
  private val api: UpLabsApi,
  private val mapper: ResultInteractor<Pair<Int, UpLabsItem>, Story>,
  private val dispatcher: CoroutineDispatcher,
) : PagingSource<Int, Story>() {

  override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
    val anchorPosition = state.anchorPosition ?: return null
    return state.closestPageToPosition(anchorPosition)?.nextKey
  }

  override suspend fun load(
    params: LoadParams<Int>,
  ): LoadResult<Int, Story> = withContext(dispatcher) {
    val page = params.key ?: 0
    val items = api.getTop(page, 1)

    return@withContext try {
      val stories = items.map { item ->
        async { mapper.executeSync(page to item) }
      }
        .awaitAll()

      LoadResult.Page(
        data = stories,
        prevKey = page.minus(1).takeIf { it >= 0 },
        nextKey = page.plus(1),
      )
    } catch (error: Exception) {
      LoadResult.Error(error)
    }
  }
}
