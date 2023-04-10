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
package dev.kiji.data.qiita.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

/**
 * ユーザからの投稿を表します。
 */
@JsonClass(generateAdapter = true)
data class Item(
  /**
   * Markdown形式の本文
   */
  @Json(name = "body")
  val body: String,
  /**
   * この記事へのコメントの数
   */
  @Json(name = "comments_count")
  val commentsCount: Int,
  /**
   * データが作成された日時
   */
  @Json(name = "created_at")
  val createdAt: ZonedDateTime,
  /**
   * 記事の一意なID
   */
  @Json(name = "id")
  val id: String,
  /**
   * この記事への「LGTM！」の数（Qiitaでのみ有効）
   */
  @Json(name = "likes_count")
  val likesCount: Int,
  /**
   * 閲覧数
   */
  @Json(name = "page_views_count")
  val pageViewsCount: Int? = -1,
  /**
   * 絵文字リアクションの数（Qiita Teamでのみ有効）
   */
  @Json(name = "reactions_count")
  val reactionsCount: Int,
  /**
   * HTML形式の本文
   */
  @Json(name = "rendered_body")
  val renderedBody: String,
  /**
   * 記事に付いたタグ一覧
   */
  @Json(name = "tags")
  val tags: List<Tagging>,
  /**
   * 記事のタイトル
   */
  @Json(name = "title")
  val title: String,
  /**
   * データが最後に更新された日時
   */
  @Json(name = "updated_at")
  val updatedAt: ZonedDateTime,
  /**
   * 記事のURL
   */
  @Json(name = "url")
  val url: String,
  /**
   * Qiita上のユーザを表します。
   */
  @Json(name = "user")
  val user: User,
) {

  val created: Long = TimeUnit.SECONDS.toMillis(createdAt.toEpochSecond())
  val updated: Long = TimeUnit.SECONDS.toMillis(updatedAt.toEpochSecond())
}
