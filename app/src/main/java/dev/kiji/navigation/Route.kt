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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import kotlin.enums.EnumEntries

enum class Route(val value: String, val title: String) {
  HackerNews("hackernews", "news.ycombinator.com"), // Kotlinx Serialization
  Qiita("qiita", "qiita.com"),                      // Moshi
  UpLabs("uplabs", "uplabs.com"),                   // Jackson
  ;

  companion object {
    val items: EnumEntries<Route> = entries
    val size: Int get() = items.size
    operator fun get(index: Int): Route = items[index]
  }
}

@Composable
fun KijiNavHost(
  navController: NavHostController,
  startRoute: Route,
  modifier: Modifier = Modifier,
  route: String? = null,
  builder: NavGraphBuilder.() -> Unit,
) {
  NavHost(
    navController = navController,
    startDestination = startRoute.value,
    modifier = modifier,
    route = route,
    builder = builder,
  )
}
