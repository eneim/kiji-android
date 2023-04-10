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

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.ToJson
import dev.kiji.core.network.NetworkModule
import dev.kiji.core.network.buildApi
import dev.kiji.data.hnews.contract.HackerNewsApi
import dev.kiji.data.hnews.contract.HackerNewsItem
import dev.kiji.data.hnews.square.RetrofitHackerNewsApi
import java.time.Duration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level

object HackerNewsDataModule {

  private val client by lazy {
    OkHttpClient.Builder()
      .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(Level.HEADERS))
      .connectTimeout(Duration.ofSeconds(15))
      .callTimeout(Duration.ofSeconds(15))
      .readTimeout(Duration.ofSeconds(15))
      .build()
  }

  private val jacksonObjectMapper = ObjectMapper().apply {
    registerKotlinModule()
  }

  private val RetrofitHackerNewsApi: HackerNewsApi by lazy {
    NetworkModule.provideRetrofitBuilder(
      NetworkModule.provideMoshi()
        .newBuilder()
        .add(HackerNewsItemTypeAdapter),
    )
      .buildApi<RetrofitHackerNewsApi>()
  }

  fun provideHackerNewsApi(): HackerNewsApi =
    RetrofitHackerNewsApi
  // KotlinxHackerNewsApi
  // LegacyHackerNewsApi(client, jacksonObjectMapper)
}

private object HackerNewsItemTypeAdapter {

  private val values = HackerNewsItem.Type.values()

  @FromJson
  fun fromJson(value: String): HackerNewsItem.Type = values
    .firstOrNull { it.value == value }
    ?: throw JsonDataException("Unknown type: $value")

  @ToJson
  fun toJson(type: HackerNewsItem.Type): String = type.value
}
