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

import android.app.Application
import com.squareup.moshi.Types
import dev.kiji.core.network.NetworkModule
import dev.kiji.core.network.buildApi
import dev.kiji.data.qiita.Comment
import dev.kiji.data.qiita.Item
import dev.kiji.data.qiita.RetrofitQiitaApi
import dev.kiji.data.qiita.User
import dev.kiji.data.qiita.entities.MoshiItem
import dev.kiji.data.qiita.entities.MoshiUser
import okio.buffer
import okio.source

object QiitaModule {

  private val realQiitaApi: QiitaApi by lazy {
    val moshi = NetworkModule.provideMoshi()
    NetworkModule.provideRetrofitBuilder(
      moshi = NetworkModule.provideMoshi().newBuilder()
        .add(User::class.java, moshi.adapter(MoshiUser::class.java)),
    )
      .buildApi<RetrofitQiitaApi>()
  }

  // Testing with mock JSON to save API call count.
  fun provideQiitaApi(application: Application): QiitaApi = object : QiitaApi {

    private val assets = application.assets
    private val type = Types.newParameterizedType(
      List::class.java,
      MoshiItem::class.java,
    )
    private val moshi = NetworkModule.provideMoshi()
    private val parser = moshi.newBuilder()
      .add(User::class.java, moshi.adapter(MoshiUser::class.java))
      .build()

    override suspend fun getItems(page: Int, count: Int, query: String?): List<Item> {
      val source = assets.open("qiita.json").source().buffer()

      return parser.adapter<List<MoshiItem>>(type)
        .fromJson(source)
        .orEmpty()
    }

    override suspend fun getItemComments(itemId: String): List<Comment> = emptyList()
  }
}
