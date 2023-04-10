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
package dev.kiji.data.common

interface MetaTags {
  val url: String
  val siteName: String?
  val title: String
  val description: String?
  val favicon: String?
  val imageUrl: String?
  val imageWidth: String?
  val imageHeight: String?

  operator fun component1() = url
  operator fun component2() = siteName
  operator fun component3() = title
  operator fun component4() = description
  operator fun component5() = favicon
  operator fun component6() = imageUrl
  operator fun component7() = imageWidth
  operator fun component8() = imageHeight
}
