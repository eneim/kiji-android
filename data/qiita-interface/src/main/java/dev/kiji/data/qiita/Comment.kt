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
package dev.kiji.data.qiita

import java.time.ZonedDateTime

interface Comment {
  /**
   * コメントの内容を表すMarkdown形式の文字列
   */
  val body: String

  /**
   * データが作成された日時
   */
  val createdAt: ZonedDateTime

  /**
   * コメントの一意なID
   */
  val id: String

  /**
   * コメントの内容を表すHTML形式の文字列
   */
  val renderedBody: String

  /**
   * データが最後に更新された日時
   */
  val updatedAt: ZonedDateTime

  /**
   * Qiita上のユーザを表します。
   */
  val user: User
}
