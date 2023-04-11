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

import dev.kiji.core.network.BaseUrl
import dev.kiji.data.QiitaApi
import dev.kiji.data.qiita.entities.MoshiComment
import dev.kiji.data.qiita.entities.MoshiItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@BaseUrl("https://qiita.com/")
internal interface RetrofitQiitaApi : QiitaApi {

  @GET("/api/v2/items")
  override suspend fun getItems(
    @Query("page") page: Int,
    @Query("per_page") count: Int,
    @Query("query") query: String?,
  ): List<MoshiItem>

  @GET("/api/v2/items/{item_id}/comments")
  override suspend fun getItemComments(@Path("item_id") itemId: String): List<MoshiComment>
}
