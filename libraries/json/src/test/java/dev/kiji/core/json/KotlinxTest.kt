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
package dev.kiji.core.json

import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import kotlin.test.assertEquals
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import okio.buffer
import okio.source
import org.junit.Test

@ExperimentalSerializationApi
class KotlinxTest {

  @Test
  fun `Kotlinx Json to ValueParser`() {

    @Serializable
    data class Data(
      val id: Long,
      val name: String,
    )

    val data = Data(1, "hello")

    val json = Json.encodeToString(data)
    assertEquals(expected = """{"id":1,"name":"hello"}""", actual = json)
    assertEquals(
      expected = data,
      actual = Json.decodeFromString("""{"id":1,"name":"hello"}"""),
    )

    val parser = Json.createValueParser(Data::class.java)
    val dataByParser = parser.read("""{"id":1,"name":"hello"}""".byteInputStream())
    assertEquals(expected = data, actual = dataByParser)

    val stream = FakeOutputStream()
    parser.write(Data(1, "hello"), stream)
    assertEquals(expected = """{"id":1,"name":"hello"}""", actual = stream.toString())
  }

  @Test
  fun `ValueParser in Kotlinx Json`() {
    data class Data(
      val id: Long,
      val name: String,
    )

    val parser = object : ValueParser<Data> {
      override fun read(source: InputStream): Data? {
        val string = source.source().buffer().readString(Charset.defaultCharset())
        val json = JSON_PATTERN.findAll(string)
          .map(MatchResult::destructured)
          .map { result -> result.component1() to result.component2() }
          .toMap()

        return try {
          Data(id = requireNotNull(json["id"]).toLong(), name = requireNotNull(json["name"]))
        } catch (ignore: Exception) {
          null
        }
      }

      override fun write(value: Data, output: OutputStream) {
        // Really manual implementation.
        output.write(
          """{"id":${value.id},"name":${value.name}}""".toByteArray(Charset.defaultCharset()),
        )
      }
    }

    val serializer = parser.asSerializer()

    val data = Data(1, "hello")
    val outputStream = FakeOutputStream()
    Json.encodeToStream(serializer, data, outputStream)
    assertEquals(expected = """{"id":1,"name":"hello"}""", actual = outputStream.toString())

    assertEquals(
      expected = data,
      actual = Json.decodeFromStream(serializer, """{"id":1,"name":"hello"}""".byteInputStream()),
    )
  }

  private companion object {
    val JSON_PATTERN = Regex("[\"'](?<key>\\w+)[\"']:\\s*[\"']?(?<value>[\\w\\s-]*)[\"']?")
  }
}

