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

package dev.kiji.navigation

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import dev.kiji.core.compose.LocalCurrentMinute
import dev.kiji.core.compose.rememberFlowWithLifecycle
import dev.kiji.core.utils.openCustomTab
import dev.kiji.services.hackernews.HackerNewsFeed
import dev.kiji.services.hackernews.HackerViewsViewModel
import dev.kiji.services.qiita.QiitaFeed
import dev.kiji.services.qiita.QiitaFeedViewModel
import dev.kiji.services.uplabs.UpLabsFeed
import dev.kiji.services.uplabs.UpLabsViewModel

@Suppress("FunctionNaming")
@Composable
fun HomeNavHost(
    hackerViewsViewModel: HackerViewsViewModel,
    qiitaFeedViewModel: QiitaFeedViewModel,
    upLabsViewModel: UpLabsViewModel,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = "hackernews",
        modifier = modifier,
    ) {
        withHackerNewsFeed(viewModel = hackerViewsViewModel)
        withQiitaFeed(viewModel = qiitaFeedViewModel)
        withUpLabsFeed(viewModel = upLabsViewModel)
    }
}

private fun NavGraphBuilder.withHackerNewsFeed(
    viewModel: HackerViewsViewModel,
) = navigation(
    startDestination = "feed",
    route = "hackernews",
    builder = {
        composable("feed") {
            val context = LocalContext.current as Activity
            val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
            HackerNewsFeed(
                data = viewModel.feedData.collectAsLazyPagingItems(),
                currentTimeMillis = LocalCurrentMinute.current,
            ) {
                context.openCustomTab(Uri.parse(it.data.url), primaryColor)
            }
        }
    }
)

private fun NavGraphBuilder.withQiitaFeed(
    viewModel: QiitaFeedViewModel,
) = navigation(
    startDestination = "feed",
    route = "qiita",
    builder = {
        composable("feed") {
            val context = LocalContext.current as Activity
            val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
            QiitaFeed(
                data = viewModel.data,
                currentTimeMillis = LocalCurrentMinute.current,
                onAction = {
                    context.openCustomTab(Uri.parse(it.data.url), primaryColor)
                }
            )
        }
    }
)

@OptIn(ExperimentalFoundationApi::class)
private fun NavGraphBuilder.withUpLabsFeed(
    viewModel: UpLabsViewModel
) = navigation(
    startDestination = "feed",
    route = "uplabs",
    builder = {
        composable("feed") {
            val context = LocalContext.current as Activity
            val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
            UpLabsFeed(
                data = rememberFlowWithLifecycle(flow = viewModel.feedData)
                    .collectAsLazyPagingItems(),
                currentTimeMillis = LocalCurrentMinute.current,
                onAction = {
                    context.openCustomTab(Uri.parse(it.data.url), primaryColor)
                }
            )
        }
    }
)
