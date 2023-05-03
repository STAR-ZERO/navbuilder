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
package app.starzero.navbuilder.truth

import androidx.navigation.NamedNavArgument
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.google.common.truth.Subject.Factory
import com.google.common.truth.Truth.assertAbout

internal class NamedNavArgumentSubject(
    metadata: FailureMetadata,
    private val actual: NamedNavArgument
) : Subject(metadata, actual) {

    fun isEqualTo(expect: NamedNavArgument) {
        check("name").that(actual.name).isEqualTo(expect.name)
        val actualArg = actual.argument
        val expectArg = expect.argument

        check("type").that(actualArg.type).isEqualTo(expectArg.type)
        check("nullable").that(actualArg.isNullable).isEqualTo(expectArg.isNullable)
        check("defaultValue").that(actualArg.defaultValue).isEqualTo(expectArg.defaultValue)
        check("defaultValuePresent")
            .that(actualArg.isDefaultValuePresent)
            .isEqualTo(expectArg.isDefaultValuePresent)
    }

    companion object {
        private val factory =
            Factory<NamedNavArgumentSubject, NamedNavArgument> { metadata, actual ->
                NamedNavArgumentSubject(metadata, actual)
            }

        fun assertThat(actual: NamedNavArgument): NamedNavArgumentSubject {
            return assertAbout(factory).that(actual)
        }
    }
}
