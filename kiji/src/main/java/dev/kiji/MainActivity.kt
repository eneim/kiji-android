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

package dev.kiji

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.squareup.moshi.Moshi
import dev.kiji.core.compose.rememberFlowWithLifecycle
import dev.kiji.core.data.hackernews.HackerNewsApi
import dev.kiji.core.moshi.HackerNewsStoryTypeAdapter
import dev.kiji.core.moshi.ZonedDateTimeConverter
import dev.kiji.core.utils.ClockBroadcastReceiver
import dev.kiji.home.hackernews.HackerNews
import dev.kiji.home.hackernews.HackerNewsFeedPagingInteractor
import dev.kiji.home.hackernews.HackerViewsViewModel
import dev.kiji.ui.theme.KijiTheme
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration

class MainActivity : ComponentActivity() {

    private val moshiConverterFactory: MoshiConverterFactory = MoshiConverterFactory.create(
        Moshi.Builder()
            .add(ZonedDateTimeConverter)
            .add(HackerNewsStoryTypeAdapter)
            .build()
    )

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.HEADERS
            )
        )
        .connectTimeout(Duration.ofSeconds(15))
        .callTimeout(Duration.ofSeconds(15))
        .readTimeout(Duration.ofSeconds(15))
        .build()

    private val hackerViewsViewModel: HackerViewsViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory() {
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val api = Retrofit.Builder()
                    .baseUrl("https://hacker-news.firebaseio.com")
                    .addConverterFactory(moshiConverterFactory)
                    .callFactory(okHttpClient)
                    .build()
                    .create(HackerNewsApi::class.java)

                @Suppress("UNCHECKED_CAST")
                return HackerViewsViewModel(
                    stateHandle = handle,
                    feedInteractor = HackerNewsFeedPagingInteractor(api = api)
                ) as T
            }
        }
    }

    private val clockBroadcastReceiver = ClockBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(
            /* window = */ window,
            /* decorFitsSystemWindows = */ false
        )

        setContent {
            val context = LocalContext.current
            KijiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HackerNews(
                        data = rememberFlowWithLifecycle(flow = hackerViewsViewModel.feedData)
                            .collectAsLazyPagingItems(),
                        currentTimeMillis = clockBroadcastReceiver.currentTimeMillis,
                    ) {
                        Toast.makeText(context, "Clicked: ${it.data.iid}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(clockBroadcastReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(clockBroadcastReceiver)
    }
}
