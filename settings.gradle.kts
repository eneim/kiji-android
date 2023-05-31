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

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Kiji"
include(":kiji")
includeFolder("core")
includeFolder("data")
includeFolder("libraries")

fun includeFolder(folderName: String) {
    file(path = "$rootDir/$folderName").listFiles()
        ?.takeIf(Array<File>::isNotEmpty)
        ?.forEach { module: File ->
            val buildFile = "$module/build.gradle"
            val ktsBuildFile = "$module/build.gradle.kts"
            if (file(buildFile).exists() || file(ktsBuildFile).exists()) {
                include("${folderName}:${module.name}")
            }
        }
}
