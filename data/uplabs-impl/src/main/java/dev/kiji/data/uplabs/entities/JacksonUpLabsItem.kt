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
package dev.kiji.data.uplabs.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import dev.kiji.data.uplabs.UpLabsImage
import dev.kiji.data.uplabs.UpLabsItem
import java.time.ZonedDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class JacksonUpLabsItem(
  override val id: Long,
  @JsonProperty("showcased_at")
  override val showcasedAt: ZonedDateTime,
  @JsonProperty("comments_count")
  override val commentsCount: Int,
  override val url: String,
  override val animated: Boolean,
  @JsonProperty("animated_teaser_url")
  override val animatedTeaserUrl: String,
  override val name: String,
  @JsonProperty("maker_name")
  override val makerName: String,
  override val points: Int,
  @JsonProperty("description")
  override val htmlDescription: String?,
  @JsonProperty("description_without_html")
  override val description: String?,
  override val label: String,
  @JsonProperty("category_friendly_name")
  override val category: String,
  @JsonProperty("background_color")
  override val backgroundColor: String,
  @JsonProperty("teaser_url")
  override val teaserUrl: String,
  @JsonProperty("preview_url")
  override val previewUrl: String,
  override val images: List<UpLabsImage>,
) : UpLabsItem
