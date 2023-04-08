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

package dev.kiji.core.data.uplabs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
data class UpLabsItem(
    val id: Long,
    @Json(name = "showcased_at") val showcasedAt: ZonedDateTime,
    @Json(name = "comments_count") val commentsCount: Int,
    val url: String,
    val animated: Boolean,
    @Json(name = "animated_teaser_url") val animatedTeaserUrl: String,
    val name: String,
    @Json(name = "maker_name") val makerName: String,
    val points: Int,
    @Json(name = "description") val htmlDescription: String?,
    @Json(name = "description_without_html") val description: String? = htmlDescription,
    val label: String,
    @Json(name = "category_friendly_name") val category: String,
    @Json(name = "teaser_url") val teaserUrl: String,
    @Json(name = "preview_url") val previewUrl: String,
    val images: List<UpLabsImage>
)

@JsonClass(generateAdapter = true)
data class UpLabsImage(
    @Json(name = "content_type") val contentType: String,
    val height: Int,
    val width: Int,
    val size: Long,
    val urls: UpLabsImageUrls
)

@JsonClass(generateAdapter = true)
data class UpLabsImageUrls(
    val full: String,
    val thumbnail: String
)
