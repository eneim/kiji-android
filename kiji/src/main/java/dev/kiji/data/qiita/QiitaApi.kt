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
package dev.kiji.data.qiita

import com.squareup.moshi.Types
import dev.kiji.Kiji
import dev.kiji.core.network.BaseUrl
import dev.kiji.data.qiita.entities.Item
import okio.buffer
import okio.source

@BaseUrl("https://qiita.com")
interface QiitaApi {

  suspend fun getItems(): List<Item>

  companion object {

    fun Kiji.getInstance(): QiitaApi = object : QiitaApi {

      private val lazyData: List<Item> by lazy {
        val source = assets.open("qiita.json")
        val type = Types.newParameterizedType(
          List::class.java,
          Item::class.java,
        )

        moshi.adapter<List<Item>>(type)
          .fromJson(source.source().buffer())
          .orEmpty()
      }

      override suspend fun getItems(): List<Item> = lazyData
    }
  }
}
