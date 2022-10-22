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
import dev.kiji.core.data.entities.Image
import dev.kiji.core.data.entities.SiteMeta
import dev.kiji.core.data.entities.Story
import dev.kiji.core.data.website.MetaTagsApi
import dev.kiji.core.domain.ItemDetailsInteractor
import dev.kiji.core.domain.ResultInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class HackerNewsItemDetailsInteractor(
    private val api: MetaTagsApi,
    private val storyFetcher: ResultInteractor<Long, Story?>,
) : ItemDetailsInteractor<Long?, Pair<Story, SiteMeta?>?>() {

    override suspend fun createObservable(params: Long?): Flow<Pair<Story, SiteMeta?>?> {
        if (params == null) return flowOf(null)
        val story: Story = storyFetcher.executeSync(params) ?: return flowOf(null)

        val meta = api.getMetaTags(url = story.link).asResult().getOrNull()
        val siteMeta = if (meta != null) {
            SiteMeta(
                title = meta.title,
                description = meta.description,
                url = meta.url,
                favicon = meta.favicon ?: story.faviconUrl,
                image = if (meta.imageUrl != null && meta.imageWidth != null && meta.imageHeight != null) {
                    Image(
                        url = meta.imageUrl,
                        width = meta.imageWidth.toInt(),
                        height = meta.imageHeight.toInt()
                    )
                } else {
                    null
                }
            )
        } else {
            null
        }

        return flowOf(story to siteMeta)
    }
}
