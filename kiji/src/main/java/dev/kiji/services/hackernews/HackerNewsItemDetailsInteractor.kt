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

import dev.kiji.core.domain.ItemDetailsInteractor
import dev.kiji.core.domain.ResultInteractor
import dev.kiji.data.common.MetaTagsApi
import dev.kiji.data.entities.Image
import dev.kiji.data.entities.SiteMeta
import dev.kiji.data.entities.Story
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withTimeoutOrNull

@ExperimentalCoroutinesApi
internal class HackerNewsItemDetailsInteractor(
  private val api: MetaTagsApi,
  private val storyFetcher: ResultInteractor<Long, Story?>,
) : ItemDetailsInteractor<Long?, Pair<Story, SiteMeta?>?>() {

  override suspend fun createObservable(params: Long?): Flow<Pair<Story, SiteMeta?>?> {
    if (params == null) return flowOf(null)
    val story: Story = storyFetcher.executeSync(params) ?: return flowOf(null)
    val meta = withTimeoutOrNull(500.toDuration(DurationUnit.MILLISECONDS)) {
      api.getMetaTags(url = story.link)
    }

    val siteMeta = meta?.let {
        (
          url,
          siteName,
          title,
          description,
          favicon,
          imageUrl,
          imageWidth,
          imageHeight,
        ),
      ->
      SiteMeta(
        title = title,
        description = description,
        url = url,
        favicon = favicon ?: story.faviconUrl,
        image = if (imageUrl != null && imageWidth != null && imageHeight != null) {
          Image(
            url = imageUrl,
            width = imageWidth.toInt(),
            height = imageHeight.toInt(),
          )
        } else {
          null
        },
      )
    }

    return flowOf(story to siteMeta)
  }
}
