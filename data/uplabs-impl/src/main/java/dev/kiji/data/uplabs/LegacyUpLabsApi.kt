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
package dev.kiji.data.uplabs

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import dev.kiji.data.UpLabsApi
import dev.kiji.data.uplabs.entities.JacksonUpLabsImage
import dev.kiji.data.uplabs.entities.JacksonUpLabsImageUrls
import dev.kiji.data.uplabs.entities.JacksonUpLabsItem
import java.time.ZonedDateTime
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request

private const val BASE_URL = "https://www.uplabs.com"

internal class LegacyUpLabsApi(
  private val client: OkHttpClient,
  mapper: ObjectMapper,
  private val timeParser: (String) -> ZonedDateTime,
) : UpLabsApi {

  private val baseUrl = requireNotNull(BASE_URL.toHttpUrlOrNull())

  private val mapper = with(mapper) {
    val module = SimpleModule("LegacyUpLabsApi")
    module
      .addDeserializer(
        ZonedDateTime::class.java, ZonedDateTimeDeserializer(timeParser),
      )
      .addDeserializer(
        UpLabsImageUrls::class.java,
        UpLabsImageUrlsDeserializer(),
      )
      .addDeserializer(
        UpLabsImage::class.java,
        UpLabsImageDeserializer(),
      )

    registerModule(module)
  }

  override suspend fun getTop(daysAgo: Int, page: Int): List<UpLabsItem> =
    request<List<JacksonUpLabsItem>>(
      path = "all.json",
      queries = mapOf(
        "days_ago" to daysAgo.toString(),
        "page" to page.toString(),
      ),
    )

  private inline fun <reified T : Any> request(
    path: String,
    queries: Map<String, String> = emptyMap(),
  ): T {
    val url = baseUrl.newBuilder()
      .addPathSegment(path)
      .apply {
        queries.forEach { (key, value) ->
          addQueryParameter(key, value)
        }
      }
      .build()
    val response = client.newCall(Request.Builder().url(url).get().build()).execute()
    return mapper.readValue(requireNotNull(response.body).byteStream())
  }
}

private class UpLabsImageUrlsDeserializer :
  StdDeserializer<UpLabsImageUrls>(UpLabsImageUrls::class.java) {
  override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): UpLabsImageUrls {
    return parser.readValueAs(JacksonUpLabsImageUrls::class.java)
  }
}

private class UpLabsImageDeserializer :
  StdDeserializer<UpLabsImage>(UpLabsImage::class.java) {
  override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): UpLabsImage {
    return parser.readValueAs(JacksonUpLabsImage::class.java)
  }
}

private class ZonedDateTimeDeserializer(
  private val timeParser: (String) -> ZonedDateTime,
) : StdDeserializer<ZonedDateTime>(ZonedDateTime::class.java) {

  override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): ZonedDateTime {
    return timeParser(parser.readValueAsTree<JsonNode>().toString())
  }
}
