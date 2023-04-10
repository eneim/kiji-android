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

package dev.kiji.data.qiita

import java.time.ZonedDateTime

interface Reaction {
  /**
   * データが作成された日時
   */
  val createdAt: ZonedDateTime

  /**
   * 絵文字画像のURL
   */
  val imageUrl: String

  /**
   * 絵文字の識別子
   */
  val name: String

  /**
   * Qiita上のユーザを表します。
   */
  val user: User
}
