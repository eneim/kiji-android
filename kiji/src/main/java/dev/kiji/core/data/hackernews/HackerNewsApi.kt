/*
 * Copyright (c) 2022 Nam Nguyen
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

package dev.kiji.core.data.hackernews

import dev.kiji.core.data.hackernews.entities.HackerNewsItem
import dev.kiji.core.data.hackernews.entities.HackerNewsUser
import dev.kiji.core.utils.BaseUrl
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// https://github.com/HackerNews/API
@BaseUrl("https://hacker-news.firebaseio.com")
interface HackerNewsApi {

    @GET("/v0/maxitem")
    suspend fun getMaxItem(): Response<Long>

    @GET("/v0/item/{id}.json")
    suspend fun getItem(@Path("id") itemId: Long): Response<HackerNewsItem>

    @GET("/v0/user/{id}.json")
    suspend fun getUser(@Path("id") userId: String): Response<HackerNewsUser>

    @GET("/v0/topstories.json")
    suspend fun getTopStories(): Response<List<Long>>

    @GET("/v0/newstories.json")
    suspend fun getNewStories(): Response<List<Long>>

    @GET("/v0/beststories.json")
    suspend fun getBestStories(): Response<List<Long>>

    @GET("/v0/askstories.json")
    suspend fun getAskStories(): Response<List<Long>>

    @GET("/v0/showstories.json")
    suspend fun getShowStories(): Response<List<Long>>

    @GET("/v0/jobstories.json")
    suspend fun getJobStories(): Response<List<Long>>

    @GET("/v0/updates.json")
    suspend fun getUpdates(): Response<List<Long>>
}
