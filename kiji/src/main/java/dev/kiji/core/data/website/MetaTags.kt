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

package dev.kiji.core.data.website

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetaTags(
    val url: String,
    @Json(name = "og:site_name")
    val siteName: String?,
    val title: String,
    val description: String?,
    val favicon: String?,
    @Json(name = "og:image")
    val imageUrl: String?,
    @Json(name = "og:image:width")
    val imageWidth: String?,
    @Json(name = "og:image:height")
    val imageHeight: String?,
)
