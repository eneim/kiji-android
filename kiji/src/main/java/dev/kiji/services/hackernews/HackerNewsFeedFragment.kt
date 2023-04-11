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
package dev.kiji.services.hackernews

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.airbnb.epoxy.composableInterop
import dev.kiji.R
import dev.kiji.core.components.Story
import dev.kiji.core.compose.LocalCurrentMinute
import dev.kiji.core.utils.openCustomTab
import dev.kiji.databinding.FragmentEpoxyViewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A feed that is backed by Epoxy-based RecyclerView. EpoxyModels are built from Compose UI.
 */
@ExperimentalCoroutinesApi
class HackerNewsFeedFragment : Fragment(R.layout.fragment_epoxy_view) {

  private val viewModel: HackerNewsViewModel by HackerNewsViewModel.getInstance(this)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding = FragmentEpoxyViewBinding.bind(view)
    binding.feed.setRemoveAdapterWhenDetachedFromWindow(false) // Keep the scroll position.

    // TODO: after the available width change, the restored scroll position is wrong.
    //  Using GridLayoutManager + not using Row with chunk can fix this issue though.
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.stories.flowWithLifecycle(viewLifecycleOwner.lifecycle)
        .collectLatest { stories ->
          val chunkSize = resources.configuration.screenWidthDp / 300
          binding.feed.withModels {
            stories.indices.chunked(chunkSize)
              .forEachIndexed { wIndex, chunk ->
                composableInterop("story::$chunk") {
                  val primaryColor = MaterialTheme.colors.primary.toArgb()
                  Column {
                    if (wIndex != 0) {
                      Divider()
                    }

                    Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                      chunk.forEachIndexed { index, storyIndex ->
                        Story(
                          story = stories[storyIndex],
                          currentTimeMillis = LocalCurrentMinute.current,
                          onAction = {
                            requireContext().openCustomTab(Uri.parse(it.data.url), primaryColor)
                          },
                          modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        )

                        if (index != chunk.lastIndex) {
                          Divider(
                            modifier = Modifier
                              .fillMaxHeight()
                              .width(1.dp),
                          )
                        }
                      }
                    }
                  }
                }
              }
          }
        }
    }
  }
}
