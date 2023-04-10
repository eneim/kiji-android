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

package dev.kiji.data

import androidx.annotation.IntRange
import dev.kiji.data.qiita.Comment
import dev.kiji.data.qiita.Item

interface QiitaApi {

  suspend fun getItems(
    @IntRange(from = 1, to = 100) page: Int,
    @IntRange(from = 1, to = 100) count: Int,
    query: String? = null,
  ): List<Item>

  suspend fun getItemComments(itemId: String): List<Comment>
}
