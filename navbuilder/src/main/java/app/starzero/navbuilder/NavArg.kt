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

public class NavArg internal constructor(
    internal val name: String,
    internal val type: NavType<*>?,
    internal val optional: Boolean,
    internal val nullable: Boolean,
    internal val defaultValue: Any?,
    internal val defaultValuePresent: Boolean
)

public class NavArgBuilder internal constructor(
    private val name: String,
    private val optional: Boolean
) {
    public var type: NavType<*>? = null
    public var nullable: Boolean = false
    public var defaultValue: Any? = null
        set(value) {
            field = value
            defaultValuePresent = true
        }
    private var defaultValuePresent: Boolean = false

    internal fun build(): NavArg {
        return NavArg(
            name,
            type,
            optional = optional,
            nullable = nullable,
            defaultValue = defaultValue,
            defaultValuePresent = defaultValuePresent
        )
    }
}
