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
package dev.kiji.core.domain

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Source: https://github.com/chrisbanes/tivi
 */
@ExperimentalCoroutinesApi
abstract class PagingDataInteractor<
        P : PagingDataInteractor.Parameters<T>,
        T : Any,
        > : SubjectInteractor<P, PagingData<T>>() {

  interface Parameters<T : Any> {
    val pagingConfig: PagingConfig
  }
}
