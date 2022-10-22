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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import dev.kiji.home.hackernews.HackerViewsViewModel
import dev.kiji.home.qiita.QiitaFeedViewModel
import dev.kiji.navigation.HomeNavHost

/**
 * The root Composable of the App.
 */
@Composable
fun KijiAppContent(
    hackerViewsViewModel: HackerViewsViewModel,
    qiitaFeedViewModel: QiitaFeedViewModel,
    navController: NavHostController,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        HomeNavHost(
            hackerViewsViewModel = hackerViewsViewModel,
            qiitaFeedViewModel = qiitaFeedViewModel,
            navHostController = navController
        )
    }
}
