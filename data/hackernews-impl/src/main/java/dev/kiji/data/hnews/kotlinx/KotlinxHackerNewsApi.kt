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

package dev.kiji.data.hnews.kotlinx

import dev.kiji.data.hnews.contract.HackerNewsApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import java.time.Duration

private const val BASE_URL = "https://hacker-news.firebaseio.com"

internal object KotlinxHackerNewsApi : HackerNewsApi {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = Duration.ofSeconds(15).toMillis()
            socketTimeoutMillis = Duration.ofSeconds(15).toMillis()
            connectTimeoutMillis = Duration.ofSeconds(15).toMillis()
        }
    }

    override suspend fun getMaxItem(): Long = request("/v0/maxitem.json")

    override suspend fun getItem(itemId: Long): KotlinxHackerNewsItem =
        request("/v0/item/$itemId.json")

    override suspend fun getUser(userId: String): KotlinxHackerNewsUser =
        request("/v0/user/$userId.json")

    override suspend fun getTopStoryIds(): List<Long> = request("/v0/topstories.json")

    override suspend fun getNewStoryIds(): List<Long> = request("/v0/newstories.json")

    override suspend fun getBestStoryIds(): List<Long> = request("/v0/beststories.json")

    override suspend fun getAskStoryIds(): List<Long> = request("/v0/askstories.json")

    override suspend fun getShowStoryIds(): List<Long> = request("/v0/showstories.json")

    override suspend fun getJobStoryIds(): List<Long> = request("/v0/jobstories.json")

    override suspend fun getUpdates(): List<Long> = request("/v0/updates.json")

    private suspend inline fun <reified T : Any> request(path: String): T =
        client.get("$BASE_URL$path").body()
}
