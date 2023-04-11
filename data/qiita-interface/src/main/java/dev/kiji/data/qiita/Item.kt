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

interface Item {
  /**
   * Markdown形式の本文
   */
  val body: String

  /**
   * この記事へのコメントの数
   */
  val commentsCount: Int

  /**
   * データが作成された日時
   */
  val createdAt: ZonedDateTime

  /**
   * 記事の一意なID
   */
  val id: String

  /**
   * この記事への「LGTM！」の数（Qiitaでのみ有効）
   */
  val likesCount: Int

  /**
   * 閲覧数
   */
  val pageViewsCount: Int?

  /**
   * 絵文字リアクションの数（Qiita Teamでのみ有効）
   */
  val reactionsCount: Int

  /**
   * HTML形式の本文
   */
  val renderedBody: String

  /**
   * 記事に付いたタグ一覧
   */
  val tags: List<Tagging>

  /**
   * 記事のタイトル
   */
  val title: String

  /**
   * データが最後に更新された日時
   */
  val updatedAt: ZonedDateTime

  /**
   * 記事のURL
   */
  val url: String

  /**
   * Qiita上のユーザを表します。
   */
  val user: User
}
