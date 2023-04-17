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
package dev.kiji.services.hackernews

enum class HackerNewsFeedType(val value: String) {

  TopStories("Top"),
  NewStories("New"),
  BestStories("Best"),
  AskStories("Ask"),
  ShowStories("Show"),
  JobStories("Jobs"),
  ;

  companion object {

    fun fromName(name: String): HackerNewsFeedType? = when (name) {
      TopStories.value -> TopStories
      NewStories.value -> NewStories
      BestStories.value -> BestStories
      AskStories.value -> AskStories
      ShowStories.value -> ShowStories
      JobStories.value -> JobStories
      else -> null
    }
  }
}
