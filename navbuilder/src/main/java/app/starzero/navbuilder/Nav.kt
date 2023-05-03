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

import android.net.Uri
import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument

/**
 * Represents a navigation destination and its associated arguments.
 */
public class Nav internal constructor(
    private val name: String,
    arguments: List<NavArg>
) {
    private val requiredArgs: List<NavArg> = arguments
        .filter { !it.optional }

    private val optionalArgs: List<NavArg> = arguments
        .filter { it.optional }

    private val requiredArgsPath: String = requiredArgs
        .joinToString(separator = "/") {
            "{${it.name}}"
        }

    private val optionalArgsQuery: String = optionalArgs
        .joinToString(separator = "&") {
            "${it.name}={${it.name}}"
        }

    /**
     * Returns the generated route string for compose navigation.
     * The route string is generated based on the name of this destination and its arguments.
     * The format of the route string is "name/{arg1}?arg2={arg2}".
     * If there are no arguments, the route string will be "name".
     *
     * @return The generated route string for compose navigation.
     */
    public val route: String = buildString {
        append(name)
        if (requiredArgsPath.isNotEmpty()) {
            append("/")
            append(requiredArgsPath)
        }
        if (optionalArgsQuery.isNotEmpty()) {
            append("?")
            append(optionalArgsQuery)
        }
    }

    /**
     * Returns a list of [NamedNavArgument] objects for compose navigation.
     * The list includes both required and optional arguments.
     *
     * @return A list of [NamedNavArgument] objects for compose navigation.
     */
    public val navArguments: List<NamedNavArgument> = arguments.map { arg ->
        navArgument(
            arg.name
        ) {
            if (arg.type != null) {
                type = arg.type
            }
            nullable = arg.nullable
            if (arg.defaultValuePresent) {
                defaultValue = arg.defaultValue
            }
        }
    }

    /**
     * Generates a navigate route for this navigation destination with the specified arguments.
     * The specified arguments must include all required arguments and may include optional arguments.
     * If any required argument is missing or any unknown argument is provided,
     * an [IllegalArgumentException] will be thrown.
     *
     * @param args The arguments to include in the navigate route. Each argument is specified as a key-value pair.
     * @return The generated navigate route string for this navigation destination with the specified arguments.
     * @throws IllegalArgumentException If any required argument is missing from the specified arguments or
     * if any unknown argument is provided.
     */
    @Suppress("UseRequire")
    public fun generateNavigateRoute(
        vararg args: Pair<String, Any>
    ): String {
        val requiredKeyArg = mutableMapOf<String, Any?>()
        val optionalKeyArg = mutableMapOf<String, Any?>()

        args.forEach { (key, value) ->
            if (requiredArgs.any { it.name == key }) {
                requiredKeyArg[key] = value
            } else if (optionalArgs.any { it.name == key }) {
                optionalKeyArg[key] = value
            } else {
                throw IllegalArgumentException(
                    "Unknown argument key: $key. " +
                        "This key does not match any of the arguments defined in this navigation destination."
                )
            }
        }

        // check required arg key
        requiredArgs.forEach { navArg ->
            if (!requiredKeyArg.keys.any { it == navArg.name }) {
                throw IllegalArgumentException(
                    "${navArg.name} is not provided. " +
                        "This argument is required for this navigation destination."
                )
            }
        }

        val uri = Uri.Builder()
            .path(name)
            .apply {
                requiredArgs.forEach {
                    appendPath("${requiredKeyArg[it.name]}")
                }
                optionalKeyArg.forEach { (key, value) ->
                    appendQueryParameter(key, "$value")
                }
            }
            .build()

        return uri.toString()
    }
}

/**
 * A builder class for creating a [Nav] object that represents a navigation destination with a given name and arguments.
 */
public class NavBuilder internal constructor(private val name: String) {
    private val arguments: MutableList<NavArg> = mutableListOf()

    /**
     * Adds an argument to the navigation destination being built.
     *
     * @param name The name of the argument.
     * @param optional Whether the argument is optional or required. Defaults to `false`.
     * @param init A lambda that configures the [NavArgBuilder] used to create the argument.
     * @throws IllegalArgumentException If an argument with the same name has already been added or
     * an optional argument that is not nullable and does not have a default value.
     */
    public fun arg(
        name: String,
        optional: Boolean = false,
        init: NavArgBuilder.() -> Unit = {}
    ) {
        val duplicated = arguments.any { it.name == name }
        require(!duplicated) {
            "Argument '$name' has already been added to the builder."
        }

        val builder = NavArgBuilder(name, optional)
        builder.init()
        val navArg = builder.build()

        require(!optional || navArg.nullable || navArg.defaultValuePresent) {
            "Argument '$name' is optional, but neither nullable nor has a default value."
        }

        arguments.add(builder.build())
    }

    internal fun build(): Nav {
        return Nav(
            name = name,
            arguments = arguments.toList()
        )
    }
}

/**
 * Creates a [Nav] object that represents a navigation destination with the given name and arguments.
 *
 * Arguments can be optional or required. The [Nav] object provides utilities for generating a route string and
 * a list of named arguments for use in Compose Navigation.
 * It also provides a method, [Nav.generateNavigateRoute], for generating a navigate route to
 * the destination with the specified arguments.
 *
 * @sample samples.basic
 *
 * @param name The name of the navigation destination.
 * @param init A lambda that configures the [NavBuilder] used to create the [Nav] object.
 * @return A [Nav] object that represents a navigation destination with the given name and arguments.
 */
public fun nav(
    name: String,
    init: NavBuilder.() -> Unit = {}
): Nav {
    val builder = NavBuilder(name)
    builder.init()
    return builder.build()
}
