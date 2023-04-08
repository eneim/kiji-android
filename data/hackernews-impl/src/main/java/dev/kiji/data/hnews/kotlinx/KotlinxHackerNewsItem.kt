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

package dev.kiji.data.hnews.kotlinx

import dev.kiji.data.hnews.contract.HackerNewsItem
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
internal data class KotlinxHackerNewsItem(
    override val id: Long,
    @SerialName(value = "deleted")
    override val isDeleted: Boolean? = null,
    @Serializable(with = TypeAdapter::class)
    override val type: HackerNewsItem.Type? = null,
    @SerialName(value = "by")
    override val author: String? = null,
    @SerialName(value = "time")
    override val createTimestamp: Long? = null,
    override val text: String? = null,
    override val dead: Boolean? = null,
    override val parent: Long? = null,
    override val poll: Long? = null,
    override val kids: List<Long>? = null,
    override val url: String? = null,
    override val score: Int? = null,
    override val title: String? = null,
    override val parts: List<Long>? = null,
    override val descendants: Int? = null,
) : HackerNewsItem

private object TypeAdapter : KSerializer<HackerNewsItem.Type> {

    private val types = HackerNewsItem.Type.values()

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(javaClass.simpleName, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): HackerNewsItem.Type {
        val value = decoder.decodeString()
        return types.first { it.value == value }
    }

    override fun serialize(encoder: Encoder, value: HackerNewsItem.Type) {
        encoder.encodeString(value.value)
    }
}
