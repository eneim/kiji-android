/*
 * Copyright (c) 2023 Nam Nguyen, nam@ene.im
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

package dev.kiji.services.hackernews

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class HackerNewsFeedType(val name: String) : Parcelable {

    @Parcelize
    object TopStories : HackerNewsFeedType("TopStories")

    @Parcelize
    object NewStories : HackerNewsFeedType("NewStories")

    @Parcelize
    object BestStories : HackerNewsFeedType("BestStories")

    @Parcelize
    object AskStories : HackerNewsFeedType("AskStories")

    @Parcelize
    object ShowStories : HackerNewsFeedType("ShowStories")

    @Parcelize
    object JobStories : HackerNewsFeedType("JobStories")

    companion object {

        fun fromName(name: String): HackerNewsFeedType? = when (name) {
            TopStories.name -> TopStories
            NewStories.name -> NewStories
            BestStories.name -> BestStories
            AskStories.name -> AskStories
            ShowStories.name -> ShowStories
            JobStories.name -> JobStories
            else -> null
        }
    }
}
