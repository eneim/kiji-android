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

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.kiji.Kiji
import dev.kiji.core.domain.PagingDataInteractor
import dev.kiji.core.network.buildApi
import dev.kiji.data.entities.Story
import dev.kiji.data.uplabs.UpLabsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class UpLabsViewModel(application: Application) : AndroidViewModel(application) {

  private val api: UpLabsApi = (application as Kiji).apiBuilder.buildApi()
  private val feedInteractor: PagingDataInteractor<UpLabsFeedPagingInteractor.Params, Story> =
    UpLabsFeedPagingInteractor(api, provideUpLabsStoryMapper())

  val feedData: Flow<PagingData<Story>> = feedInteractor.flow.cachedIn(viewModelScope)

  init {
    viewModelScope.launch(Dispatchers.IO) {
      feedInteractor(UpLabsFeedPagingInteractor.Params(PAGING_CONFIG))
    }
  }

  companion object {

    private val PAGING_CONFIG = PagingConfig(
      pageSize = 20,
    )
  }
}
