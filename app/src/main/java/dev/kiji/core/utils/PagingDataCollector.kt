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
package dev.kiji.core.utils

import androidx.paging.DifferCallback
import androidx.paging.ItemSnapshotList
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import androidx.paging.compose.LazyPagingItems
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A copycat of [LazyPagingItems] that works without the Compose context.
 *
 * @see [LazyPagingItems]
 */
class PagingDataCollector<T : Any>(
  private val dispatcher: CoroutineContext = Dispatchers.Main,
  initial: PagingItems<T> = PagingItems.empty(),
) {

  private val _itemsFlow = MutableStateFlow(initial)

  val items: Flow<PagingItems<T>> = _itemsFlow.asStateFlow()

  private val differCallback: DifferCallback = object : DifferCallback {
    override fun onChanged(position: Int, count: Int) {
      if (count > 0) {
        updateSnapshot()
      }
    }

    override fun onInserted(position: Int, count: Int) {
      if (count > 0) {
        updateSnapshot()
      }
    }

    override fun onRemoved(position: Int, count: Int) {
      if (count > 0) {
        updateSnapshot()
      }
    }
  }

  private val pagingDataDiffer = object : PagingDataDiffer<T>(
    differCallback = differCallback,
    mainContext = dispatcher,
  ) {
    override suspend fun presentNewList(
      previousList: NullPaddedList<T>,
      newList: NullPaddedList<T>,
      lastAccessedIndex: Int,
      onListPresentable: () -> Unit,
    ): Int? {
      onListPresentable()
      updateSnapshot()
      return null
    }
  }

  private fun updateSnapshot() {
    _itemsFlow.value = Snapshot(pagingDataDiffer.snapshot())
  }

  suspend fun submitData(pagingData: PagingData<T>) {
    pagingDataDiffer.collectFrom(pagingData)
  }

  private inner class Snapshot(private val data: ItemSnapshotList<T>) : PagingItems<T> {
    override val size: Int get() = data.size
    override fun peek(index: Int): T? = if (size > index) data[index] else null
    override fun get(index: Int): T? {
      pagingDataDiffer[index]
      return data[index]
    }
  }
}
