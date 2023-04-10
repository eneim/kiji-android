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
package dev.kiji.data.hnews.legacy

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import dev.kiji.data.hnews.contract.HackerNewsApi
import dev.kiji.data.hnews.contract.HackerNewsItem
import dev.kiji.data.hnews.contract.HackerNewsUser
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request

private const val BASE_URL = "https://hacker-news.firebaseio.com"

internal class LegacyHackerNewsApi(
  private val client: OkHttpClient,
  mapper: ObjectMapper,
) : HackerNewsApi {

  private val baseUrl = requireNotNull(BASE_URL.toHttpUrlOrNull())

  private val mapper = with(mapper) {
    val module = SimpleModule("LegacyHackerNewsApi")
    module.addDeserializer(HackerNewsItem.Type::class.java, TypeSerializer)
    registerModule(module)
  }

  override suspend fun getTopStoryIds(): List<Long> = request("/v0/topstories.json")

  override suspend fun getItem(itemId: Long): JacksonHackerNewsItem =
    request("/v0/item/$itemId.json")

  override suspend fun getMaxItem(): Long = request("/v0/maxitem.json")

  override suspend fun getUser(userId: String): HackerNewsUser = request("/v0/user/$userId.json")

  override suspend fun getNewStoryIds(): List<Long> = request("/v0/newstories.json")

  override suspend fun getBestStoryIds(): List<Long> = request("/v0/beststories.json")

  override suspend fun getAskStoryIds(): List<Long> = request("/v0/askstories.json")

  override suspend fun getShowStoryIds(): List<Long> = request("/v0/showstories.json")

  override suspend fun getJobStoryIds(): List<Long> = request("/v0/jobstories.json")

  override suspend fun getUpdates(): List<Long> = request("/v0/updates.json")

  private inline fun <reified T : Any> request(path: String): T {
    val url = baseUrl.newBuilder()
      .addPathSegment(path)
      .build()
    val response = client.newCall(Request.Builder().url(url).get().build()).execute()
    return mapper.readValue(requireNotNull(response.body).byteStream())
  }
}
