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

import dev.kiji.core.network.BaseUrl
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

@BaseUrl("https://metatags.io")
internal interface RetrofitMetaTagsApi : MetaTagsApi {

  @Headers("referer: https://metatags.io/")
  @GET("/api/metadata")
  override suspend fun getMetaTags(@Query("domain") url: String): MoshiMetaTags?
}
