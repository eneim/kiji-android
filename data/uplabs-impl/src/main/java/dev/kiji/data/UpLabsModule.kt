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
package dev.kiji.data

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import dev.kiji.core.network.NetworkModule
import dev.kiji.data.uplabs.LegacyUpLabsApi

object UpLabsModule {

  private val KotlinObjectMapper by lazy {
    JsonMapper.builder()
      .addModule(kotlinModule())
      // For parsing ZonedDateTime, commented out because we use Moshi as demonstration.
      // .addModule(JavaTimeModule())
      .build()
  }

  private val LegacyUpLabsApi by lazy {
    LegacyUpLabsApi(
      client = NetworkModule.provideOkHttpClient(),
      mapper = KotlinObjectMapper,
      timeParser = NetworkModule.provideTimeParser(),
    )
  }

  fun provideUpLabsApi(): UpLabsApi = LegacyUpLabsApi
}
