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

package dev.kiji.data.entities

import android.net.Uri
import androidx.compose.runtime.Immutable
import java.util.concurrent.TimeUnit

/**
 * @property iid Internal ID.
 * @property oid Original ID.
 * @property url The URL to the service's site.
 * @property link The URL to the original article. Default to [url].
 */
@Immutable
data class Story(
    val iid: String,
    val oid: String,
    val url: String,
    val link: String = url,
    val title: String,
    val content: String?,
    val images: List<Image> = emptyList(),
    val created: Long, // Second
    val updated: Long = created,
    val author: User?,
    val service: Service,
    val groupKey: Any? = null,
) {

    val website: String? = Uri.parse(link)?.host

    val createdMillis: Long = TimeUnit.SECONDS.toMillis(created)

    // https://icon.horse/icon/${url}
    // https://api.faviconkit.com/{url}/{size}
    // https://www.google.com/s2/favicons?domain=$it&sz=256
    val faviconUrl: String? = website?.let {
        "https://api.faviconkit.com/$it/256"
        // "https://www.google.com/s2/favicons?domain=$it&sz=256"
    }
}
