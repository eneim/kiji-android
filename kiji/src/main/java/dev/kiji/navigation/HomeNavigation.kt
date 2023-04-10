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
package dev.kiji.navigation

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.pagerTabIndicatorOffset
import dev.kiji.core.compose.LocalCurrentMinute
import dev.kiji.core.compose.rememberFlowWithLifecycle
import dev.kiji.core.model.Action
import dev.kiji.core.utils.openCustomTab
import dev.kiji.data.entities.Story
import dev.kiji.services.hackernews.HackerNewsFeed
import dev.kiji.services.hackernews.HackerViewsViewModel
import dev.kiji.services.qiita.QiitaFeed
import dev.kiji.services.qiita.QiitaFeedViewModel
import dev.kiji.services.uplabs.UpLabsFeed
import dev.kiji.services.uplabs.UpLabsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@Composable
internal fun HomeContent(
  hackerViewsViewModel: HackerViewsViewModel,
  qiitaFeedViewModel: QiitaFeedViewModel,
  upLabsViewModel: UpLabsViewModel,
  navHostController: NavHostController,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current as Activity
  val primaryColor = MaterialTheme.colors.primary.toArgb()
  val onClickStory: (Action<Story>) -> Unit = remember {
    { context.openCustomTab(Uri.parse(it.data.url), primaryColor) }
  }

  val feedBuilders: List<@Composable () -> Unit> = remember {
    listOf(
      {
        HackerNewsFeed(
          data = remember { hackerViewsViewModel.feedData }.collectAsLazyPagingItems(),
          currentTimeMillis = LocalCurrentMinute.current,
          onAction = onClickStory,
        )
      },
      {
        QiitaFeed(
          data = remember { qiitaFeedViewModel.data },
          currentTimeMillis = LocalCurrentMinute.current,
          onAction = onClickStory,
        )
      },
      {
        UpLabsFeed(
          data = remember { upLabsViewModel.feedData }.collectAsLazyPagingItems(),
          currentTimeMillis = LocalCurrentMinute.current,
          onAction = onClickStory,
        )
      },
    )
  }

  val pagerState: PagerState = rememberPagerState()
  val coroutineScope = rememberCoroutineScope()

  Column(modifier = modifier) {
    TabRow(
      selectedTabIndex = pagerState.currentPage,
      indicator = { tabPositions ->
        TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPositions))
      },
      modifier = Modifier.wrapContentHeight(),
    ) {
      Route.items.forEachIndexed { index, route ->
        Tab(
          text = { Text(route.name) },
          selected = pagerState.currentPage == index,
          onClick = {
            coroutineScope.launch {
              pagerState.animateScrollToPage(index)
            }
          },
        )
      }
    }

    HorizontalPager(
      pageCount = Route.size,
      state = pagerState,
      modifier = Modifier.weight(1f),
    ) { page ->
      feedBuilders[page]()
    }
  }
}

@ExperimentalCoroutinesApi
private fun NavGraphBuilder.withHackerNewsFeed(
  viewModel: HackerViewsViewModel,
) = navigation(
  startDestination = "feed",
  route = Route.HackerNews.value,
  builder = {
    composable("feed") {
      val context = LocalContext.current as Activity
      val primaryColor = MaterialTheme.colors.primary.toArgb()
      HackerNewsFeed(
        data = viewModel.feedData.collectAsLazyPagingItems(),
        currentTimeMillis = LocalCurrentMinute.current,
      ) {
        context.openCustomTab(Uri.parse(it.data.url), primaryColor)
      }
    }
  },
)

private fun NavGraphBuilder.withQiitaFeed(
  viewModel: QiitaFeedViewModel,
) = navigation(
  startDestination = "feed",
  route = Route.Qiita.value,
  builder = {
    composable("feed") {
      val context = LocalContext.current as Activity
      val primaryColor = MaterialTheme.colors.primary.toArgb()
      QiitaFeed(
        data = remember { viewModel.data },
        currentTimeMillis = LocalCurrentMinute.current,
        onAction = {
          context.openCustomTab(Uri.parse(it.data.url), primaryColor)
        },
      )
    }
  },
)

@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
private fun NavGraphBuilder.withUpLabsFeed(
  viewModel: UpLabsViewModel,
) = navigation(
  startDestination = "feed",
  route = Route.UpLabs.value,
  builder = {
    composable("feed") {
      val context = LocalContext.current as Activity
      val primaryColor = MaterialTheme.colors.primary.toArgb()
      UpLabsFeed(
        data = rememberFlowWithLifecycle(flow = viewModel.feedData)
          .collectAsLazyPagingItems(),
        currentTimeMillis = LocalCurrentMinute.current,
        onAction = {
          context.openCustomTab(Uri.parse(it.data.url), primaryColor)
        },
      )
    }
  },
)
