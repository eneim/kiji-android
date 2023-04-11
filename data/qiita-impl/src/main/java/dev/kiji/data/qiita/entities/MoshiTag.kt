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
import dev.kiji.data.qiita.Tag

@JsonClass(generateAdapter = true)
internal data class MoshiTag(
  @Json(name = "followers_count")
  override val followersCount: Int,
  @Json(name = "icon_url")
  override val iconUrl: String,
  @Json(name = "id")
  override val id: String,
  @Json(name = "items_count")
  override val itemsCount: Int,
) : Tag
