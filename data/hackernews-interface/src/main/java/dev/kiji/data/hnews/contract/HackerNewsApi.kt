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

package dev.kiji.data.hnews.contract

// https://github.com/HackerNews/API
// Endpoint: https://hacker-news.firebaseio.com
interface HackerNewsApi {

    suspend fun getMaxItem(): Long

    suspend fun getItem(itemId: Long): HackerNewsItem

    suspend fun getUser(userId: String): HackerNewsUser

    suspend fun getTopStoryIds(): List<Long>

    suspend fun getNewStoryIds(): List<Long>

    suspend fun getBestStoryIds(): List<Long>

    suspend fun getAskStoryIds(): List<Long>

    suspend fun getShowStoryIds(): List<Long>

    suspend fun getJobStoryIds(): List<Long>

    suspend fun getUpdates(): List<Long>
}
