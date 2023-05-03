/*
 * Copyright 2023 Kenji Abe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
}

subprojects {
    // Detekt
    apply<io.gitlab.arturbosch.detekt.DetektPlugin>()
    detekt {
        basePath = rootDir.absolutePath
        config = files(rootDir.resolve("config/detekt.yml"))
        buildUponDefaultConfig = true
        parallel = true
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        reports {
            xml.required.set(false)
        }
    }

    dependencies {
        detektPlugins(rootProject.libs.detekt.formatting)
    }

    // Spotless
    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    spotless {
        kotlin {
            target("**/*.kt")
            licenseHeaderFile(rootProject.file("config/spotless/copyright.kt"))
        }
        format("kts") {
            target("**/*.kts")
            licenseHeaderFile(
                rootProject.file("config/spotless/copyright.kts"),
                "(^(?![\\/ ]\\*).*$)"
            )
        }
        format("xml") {
            target("**/*.xml")
            licenseHeaderFile(rootProject.file("config/spotless/copyright.xml"), "(<[^!?])")
        }
    }
}
