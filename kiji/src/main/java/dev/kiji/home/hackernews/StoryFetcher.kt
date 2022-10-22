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

package dev.kiji.home.hackernews

import dev.kiji.core.data.asResult
import dev.kiji.core.data.entities.Service
import dev.kiji.core.data.entities.Story
import dev.kiji.core.data.entities.User
import dev.kiji.core.data.hackernews.HackerNewsApi
import dev.kiji.core.data.hackernews.entities.HackerNewsItem
import dev.kiji.core.domain.ResultInteractor

private class StoryFetcher(private val api: HackerNewsApi) : ResultInteractor<Long, Story?>() {

    override suspend fun doWork(params: Long): Story? {
        val item = api.getItem(params).asResult().getOrNull() ?: return null
        return mapStory(item)
    }

    private suspend fun mapStory(item: HackerNewsItem): Story {
        val user = if (item.author != null) {
            api.getUser(item.author).asResult().getOrNull()?.let {
                val userUrl = "https://news.ycombinator.com/user?id=${it.id}"
                User(
                    iid = userUrl,
                    handle = it.id,
                    url = userUrl,
                    image = null,
                    created = it.createdTimestampMillis,
                    service = Service.HackerNews,
                )
            }
        } else {
            null
        }

        val linkToService = "https://news.ycombinator.com/item?id=${item.id}"
        val linkToOriginal = item.url.orEmpty()
        return Story(
            iid = linkToService,
            oid = item.id.toString(),
            url = linkToService,
            link = linkToOriginal,
            title = item.title.orEmpty(),
            content = item.text,
            images = emptyList(),
            created = checkNotNull(item.createdTimestampMillis),
            updated = checkNotNull(item.createdTimestampMillis),
            author = user,
            service = Service.HackerNews
        )
    }
}

fun provideStoryFetcher(api: HackerNewsApi): ResultInteractor<Long, Story?> = StoryFetcher(api)
