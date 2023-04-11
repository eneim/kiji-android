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
package dev.kiji.home

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dev.kiji.R
import dev.kiji.databinding.FragmentHomeBinding
import dev.kiji.navigation.Route
import dev.kiji.services.hackernews.HackerNewsFeedFragment
import dev.kiji.services.qiita.QiitaFeedFragment
import dev.kiji.services.uplabs.UpLabsFeedFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
class HomeFragment : Fragment(R.layout.fragment_home) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding = FragmentHomeBinding.bind(view)

    val routes = Route.items
    val mediator = TabLayoutMediator(binding.tabs, binding.pager, true, true) { tab, pos ->
      val route = routes[pos]
      tab.text = route.name
    }

    binding.pager.adapter = HomeFragmentPagerAdapter(
      childFragmentManager,
      viewLifecycleOwner.lifecycle,
      routes,
    )

    viewLifecycleOwner.lifecycle.addObserver(
      object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) = mediator.attach()
        override fun onDestroy(owner: LifecycleOwner) = mediator.detach()
      },
    )
  }

  @ExperimentalFoundationApi
  @ExperimentalCoroutinesApi
  internal class HomeFragmentPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val routes: Array<Route>,
  ) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = routes.size

    override fun createFragment(position: Int): Fragment =
      when (requireNotNull(routes.getOrNull(position))) {
        Route.HackerNews -> HackerNewsFeedFragment()
        Route.Qiita -> QiitaFeedFragment()
        Route.UpLabs -> UpLabsFeedFragment()
      }
  }
}
