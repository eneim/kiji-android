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

// https://youtrack.jetbrains.com/issue/KTIJ-19369/False-positive-cant-be-called-in-this-context-by-implicit-receiver-with-plugins-in-Gradle-version-catalogs-as-a-TOML-file#focus=Comments-27-6204464.0-0
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.spotless)
  alias(libs.plugins.kotlinter)
}

allprojects {
  afterEvaluate {
    if (hasProperty("android") && hasProperty("dependencies")) {
      dependencies {
        "coreLibraryDesugaring"(libs.desugar.jdk.libs)
      }
    }
  }

  plugins.withType<com.android.build.gradle.BasePlugin>().configureEach {
    extensions.findByType<com.android.build.gradle.BaseExtension>()?.apply {
      compileSdkVersion(libs.versions.build.compileSdk.get().toInt())
      defaultConfig {
        minSdk = libs.versions.build.minSdk.get().toInt()
        targetSdk = libs.versions.build.targetSdk.get().toInt()
        vectorDrawables.useSupportLibrary = true
      }

      compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
      }
    }
  }

  tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    kotlinOptions {
      jvmTarget = "11"
      freeCompilerArgs.plus(arrayOf("-opt-in=kotlin.RequiresOptIn"))
    }
  }
}

subprojects {
  val metricsFolder = project.buildDir.absolutePath + "/compose-metrics"
  val compilerPluginPrefix = "androidx.compose.compiler.plugins.kotlin"
  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
      if (project.findProperty("enableComposeCompilerReports") == "true") {
        freeCompilerArgs
          .plus(
            listOf(
              "-P",
              "plugin:$compilerPluginPrefix:metricsDestination=$metricsFolder",
            ),
          )
          .plus(
            listOf(
              "-P",
              "plugin:$compilerPluginPrefix:reportsDestination=$metricsFolder",
            ),
          )
      }
    }
  }

  apply(plugin = "com.diffplug.spotless")
  spotless {
    kotlin {
      target("src/**/*.kt")
      licenseHeaderFile(rootProject.file("config/spotless/license_header.txt"))
    }
  }

  apply(plugin = "org.jmailen.kotlinter")
  kotlinter {
    disabledRules = arrayOf("filename")
  }
}
