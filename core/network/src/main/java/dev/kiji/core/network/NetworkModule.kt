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
package dev.kiji.core.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import java.time.Duration
import java.time.ZonedDateTime
import java.util.Date
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {

  private val moshi = Moshi.Builder()
    .add(ZonedDateTimeConverter())
    .add(Date::class.java, Rfc3339DateJsonAdapter())
    .build()

  private val client by lazy {
    OkHttpClient.Builder()
      .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
      .connectTimeout(Duration.ofSeconds(15))
      .callTimeout(Duration.ofSeconds(15))
      .readTimeout(Duration.ofSeconds(15))
      .build()
  }

  fun provideOkHttpClient(): OkHttpClient = client

  fun provideMoshi(): Moshi = moshi

  // Sample: injecting Moshi logic of library A to a Jackson logic of library B.
  fun provideTimeParser(): (String) -> ZonedDateTime = {
    requireNotNull(moshi.adapter(ZonedDateTime::class.java).fromJson(it))
  }

  fun provideRetrofitBuilder(
    moshi: Moshi.Builder,
  ): Retrofit.Builder = Retrofit.Builder()
    .callFactory(client::newCall)
    .addConverterFactory(MoshiConverterFactory.create(moshi.build()))
}

private class ZonedDateTimeConverter {

  @FromJson
  fun fromJson(value: String): ZonedDateTime? = ZonedDateTime.parse(value)

  @ToJson
  fun toJson(value: ZonedDateTime): String = value.toString()
}
