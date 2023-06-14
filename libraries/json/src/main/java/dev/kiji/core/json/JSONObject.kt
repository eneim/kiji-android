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
import okio.buffer
import okio.source
import org.json.JSONObject

/**
 * Given the knowledge about converting a [JSONObject] to the value of type [T], converts it to a
 * [ValueReader].
 */
fun <T> ((JSONObject) -> T?).asValueReader(): ValueReader<T> = object : ValueReader<T> {
  override fun read(source: InputStream): T? =
    this@asValueReader(JSONObject(source.source().buffer().readString(Charset.defaultCharset())))
}

/**
 * Given the knowledge about converting a value of type [T] to a [JSONObject], converts it to a
 * [ValueWriter].
 */
fun <T> ((T) -> JSONObject).asValueWriter(): ValueWriter<T> = object : ValueWriter<T> {
  override fun write(value: T, output: OutputStream) {
    val jsonObject = this@asValueWriter(value)
    output.write(jsonObject.toString().toByteArray(Charset.defaultCharset()))
  }
}
