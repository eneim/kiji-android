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
package dev.kiji.data.hnews.square

import dev.kiji.core.network.BaseUrl
import dev.kiji.data.hnews.contract.HackerNewsApi
import retrofit2.http.GET
import retrofit2.http.Path

@BaseUrl(value = "https://hacker-news.firebaseio.com")
internal interface RetrofitHackerNewsApi : HackerNewsApi {

  @GET("/v0/maxitem.json")
  override suspend fun getMaxItem(): Long

  @GET("/v0/item/{id}.json")
  override suspend fun getItem(@Path("id") itemId: Long): MoshiHackerNewsItem

  @GET("/v0/user/{id}.json")
  override suspend fun getUser(@Path("id") userId: String): MoshiHackerNewsUser

  @GET("/v0/topstories.json")
  override suspend fun getTopStoryIds(): List<Long>

  @GET("/v0/newstories.json")
  override suspend fun getNewStoryIds(): List<Long>

  @GET("/v0/beststories.json")
  override suspend fun getBestStoryIds(): List<Long>

  @GET("/v0/askstories.json")
  override suspend fun getAskStoryIds(): List<Long>

  @GET("/v0/showstories.json")
  override suspend fun getShowStoryIds(): List<Long>

  @GET("/v0/jobstories.json")
  override suspend fun getJobStoryIds(): List<Long>

  @GET("/v0/updates.json")
  override suspend fun getUpdates(): List<Long>
}
