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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.kiji.core.domain.PagingDataInteractor
import dev.kiji.core.domain.ResultInteractor
import dev.kiji.data.UpLabsApi
import dev.kiji.data.entities.Story
import dev.kiji.data.uplabs.UpLabsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
internal class UpLabsFeedPagingInteractor(
  private val api: UpLabsApi,
  private val storyFetcher: ResultInteractor<Pair<Int, UpLabsItem>, Story>,
) : PagingDataInteractor<UpLabsFeedPagingInteractor.Params, Story>() {

  override suspend fun createObservable(params: Params): Flow<PagingData<Story>> {
    return Pager(
      config = params.pagingConfig,
      initialKey = null,
      pagingSourceFactory = {
        UpLabsStoryPagingSource(
          api = api,
          mapper = storyFetcher,
          dispatcher = Dispatchers.IO,
        )
      },
    )
      .flow
  }

  data class Params(
    override val pagingConfig: PagingConfig,
  ) : Parameters<Story>
}
