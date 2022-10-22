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

package dev.kiji.core.domain

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.kiji.core.data.hackernews.HackerNewsApi
import dev.kiji.home.hackernews.HackerNewsFeedType

class HackerNewsRefreshWorker(
    appContext: Context,
    params: WorkerParameters,
    private val api: HackerNewsApi,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val feedType = inputData.getString(DATA_KEY_FEED_TYPE)?.let(HackerNewsFeedType::fromName)
            ?: return Result.success()

        return Result.success()
    }

    companion object {
        const val DATA_KEY_FEED_TYPE = "DATA_KEY_FEED_TYPE"
    }
}
