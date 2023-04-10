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
package dev.kiji.services.hackernews

import dev.kiji.core.domain.ResultInteractor
import dev.kiji.data.entities.Service
import dev.kiji.data.entities.Story
import dev.kiji.data.entities.User
import dev.kiji.data.hnews.contract.HackerNewsApi
import dev.kiji.data.hnews.contract.HackerNewsItem

private class HackerNewsStoryFetcher(
  private val api: HackerNewsApi,
) : ResultInteractor<Long, Story?>() {

  override suspend fun doWork(params: Long): Story? {
    val item = api.getItem(params)
    return mapStory(item)
  }

  private suspend fun mapStory(item: HackerNewsItem): Story {
    val authorId = item.author
    val user = if (authorId != null) {
      val hackerNewsUser = api.getUser(authorId)
      val userUrl = "https://news.ycombinator.com/user?id=$authorId"
      User(
        iid = userUrl,
        handle = hackerNewsUser.id,
        url = userUrl,
        image = null,
        created = hackerNewsUser.createTimestamp,
        service = Service.HackerNews,
      )
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
      created = checkNotNull(item.createTimestamp),
      updated = checkNotNull(item.createTimestamp),
      author = user,
      service = Service.HackerNews,
    )
  }
}

fun provideHackerNewsStoryFetcher(api: HackerNewsApi): ResultInteractor<Long, Story?> =
  HackerNewsStoryFetcher(api)
