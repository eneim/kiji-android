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
package dev.kiji.data.qiita.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.kiji.data.qiita.User

@JsonClass(generateAdapter = true)
internal data class MoshiUser(
  @Json(name = "description")
  override val description: String?,
  @Json(name = "facebook_id")
  override val facebookId: String?,
  @Json(name = "followees_count")
  override val followeesCount: Int,
  @Json(name = "followers_count")
  override val followersCount: Int,
  @Json(name = "github_login_name")
  override val githubLoginName: String?,
  @Json(name = "id")
  override val id: String,
  @Json(name = "items_count")
  override val itemsCount: Int,
  @Json(name = "linkedin_id")
  override val linkedinId: String?,
  @Json(name = "location")
  override val location: String?,
  @Json(name = "name")
  override val name: String?,
  @Json(name = "organization")
  override val organization: String?,
  @Json(name = "permanent_id")
  override val permanentId: Int,
  @Json(name = "profile_image_url")
  override val profileImageUrl: String?,
  @Json(name = "team_only")
  override val teamOnly: Boolean,
  @Json(name = "twitter_screen_name")
  override val twitterScreenName: String?,
  @Json(name = "website_url")
  override val websiteUrl: String?,
) : User
