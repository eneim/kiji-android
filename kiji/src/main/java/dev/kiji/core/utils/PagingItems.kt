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
package dev.kiji.core.utils

interface PagingItems<T : Any> {

  val size: Int

  val indices: IntRange get() = 0 until size

  fun peek(index: Int): T?

  operator fun get(index: Int): T?

  companion object {

    fun <T : Any> empty() = object : PagingItems<T> {
      override val size: Int = 0
      override fun peek(index: Int): T? = null
      override fun get(index: Int): T? = null
    }
  }
}

fun <T : Any> PagingItems<T>.getChunk(chunk: List<Int>): List<T?> = chunk.map { get(it) }
fun <T : Any> PagingItems<T>.peekChunk(chunk: List<Int>): List<T?> = chunk.map { peek(it) }
