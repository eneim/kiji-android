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

package dev.kiji.core.data.entities

import android.net.Uri
import com.squareup.moshi.JsonClass

/**
 * @property iid Internal ID.
 * @property oid Original ID.
 * @property url The URL to the service's site.
 * @property link The URL to the original article. Default to [url].
 */
@JsonClass(generateAdapter = true)
data class Story(
    val iid: String,
    val oid: String,
    val url: String,
    val link: String = url,
    val title: String,
    val images: List<Image> = emptyList(),
    val created: Long,
    val updated: Long = created,
    val author: User?,
    val service: Service,
) {

    val website: String? = Uri.parse(link)?.host

    // https://icon.horse/icon/${url}
    // https://api.faviconkit.com/{url}/{size}
    val faviconUrl: String? = website?.let { "https://api.faviconkit.com/$it/256" }
}
