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
package dev.kiji.data.hnews.legacy

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.TextNode
import dev.kiji.data.hnews.contract.HackerNewsItem

internal data class JacksonHackerNewsItem(
  override val id: Long,
  @JsonProperty("deleted")
  override val isDeleted: Boolean?,
  @JsonProperty("type")
  override val type: HackerNewsItem.Type?,
  @JsonProperty("by")
  override val author: String?,
  @JsonProperty("time")
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
  override val descendants: Int?,
) : HackerNewsItem

internal object TypeSerializer :
  StdDeserializer<HackerNewsItem.Type>(HackerNewsItem.Type::class.java) {

  private val types = HackerNewsItem.Type.values()

  override fun deserialize(
    parser: JsonParser,
    context: DeserializationContext,
  ): HackerNewsItem.Type {
    val node: JsonNode = parser.readValueAsTree() as TextNode
    return types.first { it.value == node.textValue() }
  }
}
