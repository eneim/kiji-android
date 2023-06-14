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

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.serializer

@ExperimentalSerializationApi
fun <T> Json.createValueParser(type: Class<T>): ValueParser<T> = object : ValueParser<T> {

  override fun read(source: InputStream): T? {
    @Suppress("UNCHECKED_CAST")
    return this@createValueParser.decodeFromStream(
      serializersModule.serializer(type) as KSerializer<T>,
      source,
    )
  }

  override fun write(value: T, output: OutputStream) {
    @Suppress("UNCHECKED_CAST")
    this@createValueParser.encodeToStream(
      serializersModule.serializer(type) as KSerializer<T>,
      value,
      output,
    )
  }
}

@ExperimentalSerializationApi
inline fun <reified T> ValueParser<T>.asSerializer(
  descriptor: SerialDescriptor? = null,
): KSerializer<T> = object : KSerializer<T> {
  override val descriptor: SerialDescriptor =
    descriptor ?: buildClassSerialDescriptor(T::class.java.name)

  override fun deserialize(decoder: Decoder): T {
    val input = requireNotNull(decoder as? JsonDecoder)
    val jsonObject = requireNotNull(input.decodeJsonElement() as JsonObject)
    return requireNotNull(read(jsonObject.toString().byteInputStream()))
  }

  override fun serialize(encoder: Encoder, value: T) {
    ByteArrayOutputStream().use {
      write(value, it)
      val rawString = it.toByteArray().toString(Charset.defaultCharset())
      requireNotNull(encoder as? JsonEncoder).encodeJsonElement(Json.parseToJsonElement(rawString))
    }
  }
}
