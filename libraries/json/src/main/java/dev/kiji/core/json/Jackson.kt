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

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * Creates a new [ValueReader] that uses Jackson to read and write JSON.
 */
fun <T> JsonMapper.createValueParser(type: Class<T>): ValueParser<T> = object : ValueParser<T> {
  override fun read(source: InputStream): T? = treeToValue(readTree(source), type)
  override fun write(value: T, output: OutputStream) = writeValue(output, value)
}

/**
 * Registers a [ValueParser] for the given type that [JsonMapper] couldn't handle.
 */
fun <T> JsonMapper.registerValueParser(type: Class<T>, valueParser: ValueParser<T>) {
  // Jackson doesn't know how to deserialize the data of type T yet.
  val deserializer = object : StdDeserializer<T>(type) {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): T {
      val tree = parser.readValueAsTree<TreeNode>()
      return requireNotNull(valueParser.read(writeValueAsBytes(tree).inputStream()))
    }
  }

  // Jackson doesn't know how to serialize the data of type T yet.
  val serializer = object : StdSerializer<T>(type) {
    override fun serialize(value: T, generator: JsonGenerator, provider: SerializerProvider) {
      ByteArrayOutputStream().use { outputStream ->
        valueParser.write(value, outputStream)
        generator.writeRaw(outputStream.toString(Charsets.UTF_8.name()))
      }
    }
  }

  val module = SimpleModule()
  module.addDeserializer(type, deserializer)
  module.addSerializer(type, serializer)
  registerModule(module)
}
