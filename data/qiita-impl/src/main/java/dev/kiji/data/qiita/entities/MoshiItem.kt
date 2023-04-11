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
package dev.kiji.data.qiita.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.kiji.data.qiita.Item
import dev.kiji.data.qiita.User
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
internal data class MoshiItem(
  @Json(name = "body")
  override val body: String,
  @Json(name = "comments_count")
  override val commentsCount: Int,
  @Json(name = "created_at")
  override val createdAt: ZonedDateTime,
  @Json(name = "id")
  override val id: String,
  @Json(name = "likes_count")
  override val likesCount: Int,
  @Json(name = "page_views_count")
  override val pageViewsCount: Int? = -1,
  @Json(name = "reactions_count")
  override val reactionsCount: Int,
  @Json(name = "rendered_body")
  override val renderedBody: String,
  @Json(name = "tags")
  override val tags: List<MoshiTagging>,
  @Json(name = "title")
  override val title: String,
  @Json(name = "updated_at")
  override val updatedAt: ZonedDateTime,
  @Json(name = "url")
  override val url: String,
  @Json(name = "user")
  override val user: User,
) : Item
