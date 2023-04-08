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

package dev.kiji.services.qiita

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.kiji.Kiji
import dev.kiji.data.entities.Image
import dev.kiji.data.entities.Service
import dev.kiji.data.entities.Story
import dev.kiji.data.entities.User
import dev.kiji.data.qiita.QiitaApi
import dev.kiji.data.qiita.QiitaApi.Companion.getInstance
import dev.kiji.data.qiita.entities.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QiitaFeedViewModel(application: Application) : AndroidViewModel(application) {

    private val api: QiitaApi = (application as Kiji).getInstance()
    val data = mutableStateListOf<Story>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val items = api.getItems()
            val stories = items.map { item: Item ->
                Story(
                    iid = item.id,
                    oid = item.id,
                    url = item.url,
                    link = item.url,
                    title = item.title,
                    content = item.renderedBody,
                    images = emptyList(),
                    created = item.created,
                    updated = item.updated,
                    author = item.user.let { user ->
                        User(
                            iid = user.id,
                            handle = user.id,
                            url = user.websiteUrl.orEmpty(),
                            image = user.profileImageUrl?.let {
                                Image(url = it)
                            },
                            created = 0L,
                            updated = 0L,
                            service = Service.Qiita
                        )
                    },
                    service = Service.Qiita
                )
            }
            data.addAll(stories)
        }
    }
}
