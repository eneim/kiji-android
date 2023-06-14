/*
 * Copyright (c) 2023 Nam Nguyen, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.kiji.core.json

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Suppress("OPT_IN_USAGE")
class MixedParsersTest {

  // Not supported by Moshi.
  @Serializable
  data class Person(val name: String, val age: Int)

  @JsonClass(generateAdapter = true)
  data class Article(
    val title: String,
    val author: Person,
  )

  @Test
  fun `Moshi with field from foreign class`() {
    val moshi = Moshi.Builder()
      // .addLast(KotlinJsonAdapterFactory()) // <- Ensure `Person` is not parsable by Moshi.
      // Use Kotlinx's Json to parse Person.
      .add { type, _, _ ->
        if (type == Person::class.java) {
          Json.createValueParser(Person::class.java)    // ValueParser<Person>
            .createJsonAdapter()                  // JsonAdapter<Person>
        } else {
          null
        }
      }
      .build()

    val person = Person("John Doe", 30)
    val article = Article("Hello", person)

    val json = moshi.adapter(Article::class.java).toJson(article)
    assertEquals(
      expected = """{"title":"Hello","author":{"name":"John Doe","age":30}}""",
      actual = json,
    )

    val data = moshi.adapter(Article::class.java)
      .fromJson("""{"title":"Hello","author":{"name":"John Doe","age":30}}""")
    assertEquals(
      expected = article,
      actual = data,
    )
  }
}
