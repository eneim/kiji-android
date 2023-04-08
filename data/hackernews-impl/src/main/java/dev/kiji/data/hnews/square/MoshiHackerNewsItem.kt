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

package dev.kiji.data.hnews.square

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.kiji.data.hnews.contract.HackerNewsItem

@JsonClass(generateAdapter = true)
internal class MoshiHackerNewsItem(
    override val id: Long,
    @Json(name = "deleted")
    override val isDeleted: Boolean?,
    override val type: HackerNewsItem.Type?,
    @Json(name = "by")
    override val author: String?,
    @Json(name = "time")
    override val createTimestamp: Long?,
    override val text: String?,
    override val dead: Boolean?,
    override val parent: Long?,
    override val poll: Long?,
    override val kids: List<Long>?,
    override val url: String?,
    override val score: Int?,
    override val title: String?,
    override val parts: List<Long>?,
    override val descendants: Int?
) : HackerNewsItem
