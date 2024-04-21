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
package dev.kiji

import android.app.Application
import android.webkit.WebView
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import dev.kiji.core.network.NetworkModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class Kiji : Application(), ImageLoaderFactory {

  override fun newImageLoader(): ImageLoader {
    return ImageLoader.Builder(this)
      .okHttpClient(NetworkModule.provideOkHttpClient())
      .components {
        add(SvgDecoder.Factory())
      }
      .build()
  }

  override fun onCreate() {
    super.onCreate()
    Napier.base(DebugAntilog())
    WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
  }
}
