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

import android.util.JsonReader
import android.util.JsonWriter
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject
import org.junit.runner.RunWith

// Android test because JsonReader, JsonWriter and JSONObject are not available on JVM.
@Suppress("TestFunctionName")
@RunWith(AndroidJUnit4::class)
class ManualJsonTest {

  internal data class Data(
    val id: Long,
    val name: String,
  )

  @Test
  fun JsonReader_to_ValueReader() {
    val reader: (JsonReader) -> Data = reader@{ jsonReader ->
      jsonReader.beginObject()
      var id: Long? = null
      var name: String? = null
      while (jsonReader.hasNext()) {
        when (jsonReader.nextName()) {
          "id" -> id = jsonReader.nextLong()
          "name" -> name = jsonReader.nextString()
          else -> jsonReader.skipValue()
        }
      }
      jsonReader.endObject()
      return@reader Data(
        id = requireNotNull(id),
        name = requireNotNull(name),
      )
    }

    val valueReader = reader.asValueReader()
    val dataByParser = valueReader.read("""{"id":1,"name":"hello"}""".byteInputStream())
    assertEquals(expected = Data(1, "hello"), actual = dataByParser)
  }

  @Test
  fun JsonWriter_to_ValueWriter() {
    val writer: JsonWriter.(Data) -> Unit = writer@{ data ->
      beginObject().name("id").value(data.id).name("name").value(data.name).endObject()
    }

    val valueWriter = writer.asValueWriter()
    val outputStream = FakeOutputStream()
    valueWriter.write(Data(1, "hello"), outputStream)
    assertEquals(expected = """{"id":1,"name":"hello"}""", actual = outputStream.toString())
  }

  @Test
  fun JSONObject_to_ValueReader() {
    val reader: (JSONObject) -> Data = {
      Data(
        id = it.getLong("id"),
        name = it.getString("name"),
      )
    }

    val valueReader = reader.asValueReader()
    val dataByParser = valueReader.read("""{"id":1,"name":"hello"}""".byteInputStream())
    assertEquals(expected = Data(1, "hello"), actual = dataByParser)
  }

  @Test
  fun JSONObject_to_ValueWriter() {
    val writer: (Data) -> JSONObject = {
      JSONObject().apply {
        put("id", it.id)
        put("name", it.name)
      }
    }

    val valueWriter = writer.asValueWriter()
    val outputStream = FakeOutputStream()
    valueWriter.write(Data(1, "hello"), outputStream)
    assertEquals(expected = """{"id":1,"name":"hello"}""", actual = outputStream.toString())
  }
}
