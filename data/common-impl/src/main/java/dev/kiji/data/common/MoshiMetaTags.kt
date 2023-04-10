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
package dev.kiji.data.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class MoshiMetaTags(
  override val url: String,
  @Json(name = "og:site_name")
  override val siteName: String?,
  override val title: String,
  override val description: String?,
  override val favicon: String?,
  @Json(name = "og:image")
  override val imageUrl: String?,
  @Json(name = "og:image:width")
  override val imageWidth: String?,
  @Json(name = "og:image:height")
  override val imageHeight: String?,
) : MetaTags
