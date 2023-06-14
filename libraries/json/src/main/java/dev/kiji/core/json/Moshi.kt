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

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.io.InputStream
import java.io.OutputStream
import okio.buffer
import okio.sink
import okio.source

fun <T> Moshi.createValueParser(type: Class<T>): ValueParser<T> = object : ValueParser<T> {

  private val adapter = this@createValueParser.adapter(type)

  override fun read(source: InputStream): T? =
    source.source().buffer().use(adapter::fromJson)

  override fun write(value: T, output: OutputStream) {
    output.sink().buffer().use { sink ->
      adapter.toJson(sink, value)
    }
  }
}

fun <T> ValueParser<T>.createJsonAdapter(): JsonAdapter<T> = object : JsonAdapter<T>() {

  override fun fromJson(reader: JsonReader): T? {
    return reader.nextSource().inputStream().use(::read)
  }

  override fun toJson(writer: JsonWriter, value: T?) {
    value ?: return
    writer.valueSink().outputStream().use {
      write(value, it)
    }
  }
}
