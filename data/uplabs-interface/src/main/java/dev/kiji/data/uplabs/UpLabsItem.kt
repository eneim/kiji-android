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

package dev.kiji.data.uplabs

import java.time.ZonedDateTime

interface UpLabsItem {
  val id: Long
  val showcasedAt: ZonedDateTime
  val commentsCount: Int
  val url: String
  val animated: Boolean
  val animatedTeaserUrl: String
  val name: String
  val makerName: String
  val points: Int
  val htmlDescription: String?
  val description: String?
  val label: String
  val category: String
  val backgroundColor: String
  val teaserUrl: String
  val previewUrl: String
  val images: List<UpLabsImage>
}
