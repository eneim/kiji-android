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

interface User {
  /**
   * 自己紹介文
   */
  val description: String?

  /**
   * Facebook ID
   */
  val facebookId: String?

  /**
   * このユーザがフォローしているユーザの数
   */
  val followeesCount: Int

  /**
   * このユーザをフォローしているユーザの数
   */
  val followersCount: Int

  /**
   * GitHub ID
   */
  val githubLoginName: String?

  /**
   * ユーザID
   */
  val id: String

  /**
   * このユーザが qiita.com 上で公開している記事の数 (Qiita Teamでの記事数は含まれません)
   */
  val itemsCount: Int

  /**
   * LinkedIn ID
   */
  val linkedinId: String?

  /**
   * 居住地
   */
  val location: String?

  /**
   * 設定している名前
   */
  val name: String?

  /**
   * 所属している組織
   */
  val organization: String?

  /**
   * ユーザごとに割り当てられる整数のID
   */
  val permanentId: Int

  /**
   * 設定しているプロフィール画像のURL
   */
  val profileImageUrl: String?

  /**
   * Qiita Team専用モードに設定されているかどうか
   */
  val teamOnly: Boolean

  /**
   * Twitterのスクリーンネーム
   */
  val twitterScreenName: String?

  /**
   * 設定しているWebサイトのURL
   */
  val websiteUrl: String?
}
