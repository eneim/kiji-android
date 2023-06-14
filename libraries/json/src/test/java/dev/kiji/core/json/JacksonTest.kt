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

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import kotlin.test.assertEquals
import okio.buffer
import okio.source
import org.junit.Test

class JacksonTest {

  internal data class Data(
    val id: Long,
    val name: String,
  )

  @Test
  fun `JsonMapper to ValueParser`() {
    val jackson = jsonMapper {
      addModule(kotlinModule())
    }

    val data = Data(1, "hello")
    val json = jackson.writeValueAsString(data)
    assertEquals(expected = """{"id":1,"name":"hello"}""", actual = json)
    assertEquals(
      expected = data,
      actual = jackson.readValue("""{"id":1,"name":"hello"}""", Data::class.java),
    )

    val parser = jackson.createValueParser(Data::class.java)
    val dataByParser = parser.read("""{"id":1,"name":"hello"}""".byteInputStream())
    assertEquals(expected = data, actual = dataByParser)

    val stream = FakeOutputStream()
    parser.write(Data(1, "hello"), stream)
    assertEquals(expected = """{"id":1,"name":"hello"}""", actual = stream.toString())
  }

  @Test
  fun `ValueParser to Jackson JsonMapper`() {
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
        output.write("""{"id":${value.id},"name":"${value.name}"}""".toByteArray())
      }
    }

    val jackson = jsonMapper {} // Without KotlinModule.
    jackson.registerValueParser(Data::class.java, parser)

    val data = Data(1, "hello")
    val json = jackson.writeValueAsString(data)
    assertEquals(expected = """{"id":1,"name":"hello"}""", actual = json)

    assertEquals(
      expected = data,
      actual = jackson.readValue("""{"id":1,"name":"hello"}""", Data::class.java),
    )
  }

  private companion object {
    val JSON_PATTERN = Regex("[\"'](?<key>\\w+)[\"']:\\s*[\"']?(?<value>[\\w\\s-]*)[\"']?")
  }
}

