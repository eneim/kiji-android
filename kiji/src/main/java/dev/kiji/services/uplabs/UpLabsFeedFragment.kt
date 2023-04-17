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
package dev.kiji.services.uplabs

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.compose.collectAsLazyPagingItems
import dev.kiji.R
import dev.kiji.core.utils.openCustomTab
import dev.kiji.databinding.FragmentComposeViewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
class UpLabsFeedFragment : Fragment(R.layout.fragment_compose_view) {

  private val viewModel: UpLabsViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding = FragmentComposeViewBinding.bind(view)
    binding.feed.setContent {
      val primaryColor = MaterialTheme.colors.primary.toArgb()
      UpLabsFeed(data = viewModel.feedData.collectAsLazyPagingItems()) {
        requireContext().openCustomTab(Uri.parse(it.url), primaryColor)
      }
    }
  }
}
