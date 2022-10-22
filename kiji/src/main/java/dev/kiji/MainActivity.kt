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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dev.kiji.core.compose.LocalCurrentMinute
import dev.kiji.core.data.entities.Story
import dev.kiji.core.data.hackernews.HackerNewsApi
import dev.kiji.core.data.website.MetaTagsApi
import dev.kiji.core.domain.ResultInteractor
import dev.kiji.core.utils.ClockBroadcastReceiver
import dev.kiji.core.utils.create
import dev.kiji.home.hackernews.HackerNewsFeedPagingInteractor
import dev.kiji.home.hackernews.HackerNewsItemDetailsInteractor
import dev.kiji.home.hackernews.HackerViewsViewModel
import dev.kiji.home.hackernews.provideStoryFetcher
import dev.kiji.ui.theme.KijiAppTheme

class MainActivity : ComponentActivity() {

    private val hackerViewsViewModel: HackerViewsViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory() {
            override fun <T : ViewModel> create(
                key: String, modelClass: Class<T>, handle: SavedStateHandle
            ): T {
                val hackerNewsApi: HackerNewsApi = kijiApp.apiBuilder.create()
                val metaTagsApi: MetaTagsApi = kijiApp.apiBuilder.create()

                val storyFetcher: ResultInteractor<Long, Story?> =
                    provideStoryFetcher(hackerNewsApi)

                @Suppress("UNCHECKED_CAST")
                return HackerViewsViewModel(
                    stateHandle = handle,
                    feedInteractor = HackerNewsFeedPagingInteractor(
                        api = hackerNewsApi, storyFetcher = storyFetcher
                    ),
                    itemInteractor = HackerNewsItemDetailsInteractor(
                        api = metaTagsApi, storyFetcher = storyFetcher
                    ),
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
            CompositionLocalProvider(
                LocalCurrentMinute provides clockBroadcastReceiver.currentTimeMillis,
            ) {
                KijiAppTheme {
                    KijiAppContent(
                        hackerViewsViewModel = hackerViewsViewModel,
                        qiitaFeedViewModel = viewModel(),
                        navController = rememberNavController(),
                    )
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
