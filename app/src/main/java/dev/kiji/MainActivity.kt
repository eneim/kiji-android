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
package dev.kiji

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dev.kiji.core.compose.LocalCurrentMinute
import dev.kiji.core.utils.ClockBroadcastReceiver
import dev.kiji.ui.theme.KijiAppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : FragmentActivity() {

  private val clockBroadcastReceiver = ClockBroadcastReceiver()

  @ExperimentalCoroutinesApi
  @ExperimentalFoundationApi
  @ExperimentalMaterialApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      KijiAppTheme {
        Scaffold(
          contentWindowInsets = WindowInsets.safeDrawing,
        ) { padding ->
          CompositionLocalProvider(
            LocalCurrentMinute provides clockBroadcastReceiver.currentTimeMillis,
          ) {
            KijiAppContent(
              navigator = rememberNavController(),
              hackerNewsViewModel = viewModel(),
              qiitaFeedViewModel = viewModel(),
              upLabsViewModel = viewModel(),
              modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            )
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
