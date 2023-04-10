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
package dev.kiji.data.hnews.contract

/**
 * Definitions of a HackerNews item returned from the API.
 */
interface HackerNewsItem {
  val id: Long
  val isDeleted: Boolean?
  val type: Type?
  val author: String?
  val createTimestamp: Long?
  val text: String?
  val dead: Boolean?
  val parent: Long?
  val poll: Long?
  val kids: List<Long>?
  val url: String?
  val score: Int?
  val title: String?
  val parts: List<Long>?
  val descendants: Int?

  enum class Type(val value: String) {
    JOB("job"),
    STORY("story"),
    COMMENT("comment"),
    POLL("poll"),
    POLLOPT("pollopt"),
  }
}
