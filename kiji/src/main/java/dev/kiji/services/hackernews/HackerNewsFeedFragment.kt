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

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.airbnb.epoxy.ComposeEpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.epoxy.composableInterop
import dev.kiji.R
import dev.kiji.core.components.Story
import dev.kiji.core.compose.LocalCurrentMinute
import dev.kiji.core.utils.PagingItems
import dev.kiji.core.utils.getChunk
import dev.kiji.core.utils.openCustomTab
import dev.kiji.data.entities.Story
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
  private var lastSeenItemPosition = RecyclerView.NO_POSITION

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding = FragmentEpoxyViewBinding.bind(view)

    val layoutManager = GridLayoutManager(view.context, 1)
    binding.feed.layoutManager = layoutManager

    val gridSpan = (resources.configuration.screenWidthDp / 300).coerceAtLeast(1)
    val controller = FeedController(
      view.context,
      gridSpan,
    )
    binding.feed.setController(controller)

    lastSeenItemPosition =
      savedInstanceState?.getInt(STATE_SCROLL_POSITION) ?: RecyclerView.NO_POSITION
    if (lastSeenItemPosition != RecyclerView.NO_POSITION) {
      layoutManager.scrollToPosition(lastSeenItemPosition / gridSpan)
    }

    binding.feed.addOnScrollListener(
      object : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
          try {
            val topItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
              .takeIf { it != RecyclerView.NO_POSITION } ?: return
            val model = controller.adapter.getModelAtPosition(topItemPosition)
              as? ComposeEpoxyModel ?: return
            lastSeenItemPosition = model.keys.firstOrNull() as? Int ?: RecyclerView.NO_POSITION
          } catch (ignore: IndexOutOfBoundsException) {
            ignore.printStackTrace()
          }
        }
      },
    )

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.stories.flowWithLifecycle(viewLifecycleOwner.lifecycle)
        .collectLatest { stories -> controller.setData(stories) }
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt(STATE_SCROLL_POSITION, lastSeenItemPosition)
  }

  internal class FeedController(
    private val context: Context,
    private val chunkSize: Int,
  ) : TypedEpoxyController<PagingItems<Story>>() {
    override fun buildModels(data: PagingItems<Story>?) {
      val stories = data ?: PagingItems.empty()
      stories.indices.chunked(chunkSize)
        .forEachIndexed { cIndex, chunk ->
          composableInterop(
            id = "story::$chunk",
            keys = chunk.toTypedArray(), // Important for manual saving/restoring scroll position!!
          ) {
            val primaryColor = MaterialTheme.colors.primary.toArgb()
            StoryRow(
              isTopRow = cIndex == 0,
              stories = stories.getChunk(chunk),
              context = context,
              primaryColor = primaryColor,
            )
          }
        }
    }
  }

  companion object {
    private const val STATE_SCROLL_POSITION = "STATE_SCROLL_POSITION"
  }
}

@Composable
internal fun StoryRow(
  isTopRow: Boolean,
  stories: List<Story?>,
  context: Context,
  @ColorInt primaryColor: Int,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {
    if (!isTopRow) {
      Divider()
    }
    Row(modifier = Modifier.height(IntrinsicSize.Max)) {
      stories.forEachIndexed { index, story ->
        Story(
          story = story,
          currentTimeMillis = LocalCurrentMinute.current,
          onAction = {
            context.openCustomTab(Uri.parse(it.data.url), primaryColor)
          },
          modifier = Modifier
            .fillMaxHeight()
            .weight(1f),
        )

        if (index != stories.lastIndex) {
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
