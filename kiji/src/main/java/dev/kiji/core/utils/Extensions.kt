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

package dev.kiji.core.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION

fun Context.resourceToUrl(resourceId: Int): String = "android.resource://$packageName/$resourceId"

private val chromePackages = listOf(
    // stable
    "com.android.chrome",
    // beta
    "com.chrome.beta",
    // dev
    "com.chrome.dev",
    // canary
    "com.chrome.canary",
    // local
    "com.google.android.apps.chrome"
)

/**
 * Chromeがある場合はChromeのパッケージを返す
 */
private fun Context.getChromePackage(): String? {
    val customTabsPackages = getCustomTabsPackages()
    return chromePackages.find { chromePackage ->
        customTabsPackages.any { it.activityInfo.packageName == chromePackage }
    }
}

fun Context.openCustomTab(uri: Uri, @ColorInt toolbarColor: Int) {
    val customTabsPackages = getCustomTabsPackages()
    val chromePackage = getChromePackage()
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setToolbarColor(toolbarColor)
        .build()

    if (chromePackage != null) {
        // Chromeがある場合はChromeを優先的に開く
        customTabsIntent.intent.apply {
            setPackage(chromePackage)
        }
    } else if (customTabsPackages.isNotEmpty()) {
        // Chromeは無いがCustom Tabs対応ブラウザがある場合は最初の1件を自動で選択してCustom Tabsを開く
        customTabsIntent.intent.apply {
            setPackage(customTabsPackages[0].activityInfo.packageName)
        }
    } else {
        error("$customTabsPackages is empty!!!")
    }

    customTabsIntent.launchUrl(this, uri)
}

private fun Context.getCustomTabsPackages(): List<ResolveInfo> {
    val pm = packageManager
    val activityIntent = Intent()
        .setAction(Intent.ACTION_VIEW)
        .addCategory(Intent.CATEGORY_BROWSABLE)
        .setData(Uri.fromParts("http", "", null))
    val resolvedActivityList = pm.queryIntentActivities(activityIntent, PackageManager.MATCH_ALL)
    return resolvedActivityList
        .mapNotNull { info ->
            val serviceIntent = Intent()
            serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)
            info.takeIf { pm.resolveService(serviceIntent, 0) != null }
        }
        .toList()
}
