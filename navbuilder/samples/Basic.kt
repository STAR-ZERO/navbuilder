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
package samples

private fun basic() {
    val startNav = nav("start")

    val nextNav = nav("next") {
        arg("id") {
            type = NavType.IntType
        }
        arg("option1") {
            type = NavType.StringType
            nullable = true
        }
        arg("option2") {
            type = NavType.IntType
            defaultValue = 0
        }
    }

    @Composable
    fun AppNavHost(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = startNav.route
        ) {
            composable(
                // "start"
                route = startNav.route,
            ) {
                // ...

                // "next/123?option1=abc"
                val route = nextNav.generateNavigateRoute(
                    "id" to 123,
                    "option1" to "abc"
                )
                navController.navigate(route)
            }

            composable(
                // "next/{id}?option1={option1}&option2={option2}"
                route = nextNav.route,

                // listOf(
                //     navArgument("id") { type = NavType.IntType },
                //     navArgument("option1") { type = NavType.StringType; nullable = true },
                //     navArgument("option2") { type = NavType.IntType; defaultValue = 0 },
                // )
                arguments = nextNav.navArguments
            ) {
                // ...
            }
        }
    }
}
