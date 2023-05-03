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
package app.starzero.navbuilder

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.starzero.navbuilder.truth.NamedNavArgumentSubject.Companion.assertThat
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class NavTest {
    @Test
    fun testNoArgs() {
        val nav = nav("test")
        assertThat(nav.route).isEqualTo("test")
        assertThat(nav.navArguments).isEmpty()
        assertThat(nav.generateNavigateRoute()).isEqualTo("test")
    }

    @Test
    fun testSimpleArgs() {
        val nav = nav("test") {
            arg("arg1") {
                type = NavType.IntType
            }
            arg("arg2", optional = true) {
                type = NavType.StringType
                nullable = true
            }
            arg("arg3", optional = true) {
                type = NavType.IntType
                defaultValue = 999
            }
        }
        assertThat(nav.route).isEqualTo("test/{arg1}?arg2={arg2}&arg3={arg3}")

        assertThat(nav.navArguments).hasSize(3)
        assertThat(nav.navArguments[0])
            .isEqualTo(navArgument("arg1") { type = NavType.IntType })
        assertThat(nav.navArguments[1])
            .isEqualTo(navArgument("arg2") { type = NavType.StringType; nullable = true })
        assertThat(nav.navArguments[2])
            .isEqualTo(navArgument("arg3") { type = NavType.IntType; defaultValue = 999 })

        assertThat(
            nav.generateNavigateRoute(
                "arg1" to 123,
                "arg2" to "abc",
                "arg3" to 999
            )
        ).isEqualTo("test/123?arg2=abc&arg3=999")

        assertThat(
            nav.generateNavigateRoute(
                "arg1" to 123
            )
        ).isEqualTo("test/123")
    }

    @Test
    fun testArgError() {
        val e1 = assertThrows(IllegalArgumentException::class.java) {
            nav("test") {
                arg("arg")
                arg("arg")
            }
        }
        assertThat(e1).hasMessageThat()
            .isEqualTo("Argument 'arg' has already been added to the builder.")

        val e2 = assertThrows(IllegalArgumentException::class.java) {
            nav("test") {
                arg("arg", optional = true)
            }
        }
        assertThat(e2).hasMessageThat()
            .isEqualTo("Argument 'arg' is optional, but neither nullable nor has a default value.")
    }

    @Test
    fun testGenerateNavigateRouteError() {
        val nav = nav("test") {
            arg("arg1")
            arg("arg2")
            arg("arg3", optional = true) {
                nullable = true
            }
        }

        // Unknown argument key
        val e1 = assertThrows(IllegalArgumentException::class.java) {
            nav.generateNavigateRoute(
                "arg" to 123
            )
        }
        assertThat(e1).hasMessageThat()
            .isEqualTo(
                "Unknown argument key: arg. " +
                    "This key does not match any of the arguments defined in this navigation destination."
            )

        // Required key is not provided
        val e2 = assertThrows(IllegalArgumentException::class.java) {
            nav.generateNavigateRoute(
                "arg1" to 123
            )
        }
        assertThat(e2).hasMessageThat()
            .isEqualTo(
                "arg2 is not provided. " +
                    "This argument is required for this navigation destination."
            )
    }
}
