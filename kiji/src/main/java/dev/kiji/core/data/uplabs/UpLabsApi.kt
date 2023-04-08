/*
 * Copyright (c) 2022 Nam Nguyen
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

package dev.kiji.core.data.uplabs

import dev.kiji.core.utils.BaseUrl
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

@BaseUrl("https://www.uplabs.com")
interface UpLabsApi {

    @GET("/all.json")
    suspend fun getTop(
        @Query("days_ago") daysAgo: Int, // Default 0
        @Query("page") page: Int // Default 1
    ): Response<List<UpLabsItem>>
}
