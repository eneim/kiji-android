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

package dev.kiji.services.uplabs

import dev.kiji.core.data.entities.Image
import dev.kiji.core.data.entities.Service
import dev.kiji.core.data.entities.Story
import dev.kiji.core.data.uplabs.UpLabsItem
import dev.kiji.core.domain.ResultInteractor
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private class UpLabsStoryMapper : ResultInteractor<Pair<Int, UpLabsItem>, Story>() {

    private val today = ZonedDateTime.now()

    override suspend fun doWork(params: Pair<Int, UpLabsItem>): Story {
        val item = params.second
        val daysAgo = params.first
        val images = item.images
            .map {
                Image(
                    url = it.urls.full,
                    width = it.width,
                    height = it.height,
                    thumbnails = emptyList(),
                )
            }.takeIf(List<Image>::isNotEmpty)
            ?: listOf(Image(url = item.previewUrl))

        return Story(
            iid = "${Service.UpLabs.deeplinkHost}/${item.id}",
            oid = item.id.toString(),
            url = item.url,
            link = item.url,
            title = item.name,
            content = item.htmlDescription,
            images = images,
            created = item.showcasedAt.toEpochSecond(),
            updated = item.showcasedAt.toEpochSecond(),
            author = null,
            service = Service.UpLabs,
            groupKey = today.minusDays(daysAgo.toLong()).toLocalDate()
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
        )
    }
}

fun provideUpLabsStoryMapper(): ResultInteractor<Pair<Int, UpLabsItem>, Story> = UpLabsStoryMapper()
