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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import dev.kiji.navigation.HomeContent
import dev.kiji.services.hackernews.HackerNewsViewModel
import dev.kiji.services.qiita.QiitaFeedViewModel
import dev.kiji.services.uplabs.UpLabsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * The root Composable of the App.
 */
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@Composable
internal fun KijiAppContent(
  navigator: NavHostController,
  hackerNewsViewModel: HackerNewsViewModel,
  qiitaFeedViewModel: QiitaFeedViewModel,
  upLabsViewModel: UpLabsViewModel,
  modifier: Modifier = Modifier,
) {
  HomeContent(
    hackerNewsViewModel = hackerNewsViewModel,
    qiitaFeedViewModel = qiitaFeedViewModel,
    upLabsViewModel = upLabsViewModel,
    modifier = modifier,
  )
}
