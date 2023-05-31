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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.kiji.core.compose.LocalCurrentMinute
import dev.kiji.core.utils.ClockBroadcastReceiver
import dev.kiji.databinding.ActivityHomeBinding
import dev.kiji.ui.theme.KijiAppTheme

class MainActivity : FragmentActivity() {

  private val clockBroadcastReceiver = ClockBroadcastReceiver()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(
      /* window = */ window,
      /* decorFitsSystemWindows = */ true,
    )

    setContent {
      val systemUiController = rememberSystemUiController()
      val darkTheme = isSystemInDarkTheme()
      // Update the dark content of the system bars to match the theme
      DisposableEffect(systemUiController, darkTheme) {
        systemUiController.systemBarsDarkContentEnabled = !darkTheme
        onDispose {}
      }
      CompositionLocalProvider(
        LocalCurrentMinute provides clockBroadcastReceiver.currentTimeMillis,
      ) {
        KijiAppTheme {
          /* KijiAppContent(
            hackerNewsViewModel = viewModel(),
            qiitaFeedViewModel = viewModel(),
            upLabsViewModel = viewModel(),
            navController = rememberNavController(),
          ) */

          // Using AndroidView to inject the Compose theme down to the content.
          AndroidViewBinding(factory = ActivityHomeBinding::inflate) {}
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
