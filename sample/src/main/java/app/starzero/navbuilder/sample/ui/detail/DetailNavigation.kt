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
package app.starzero.navbuilder.sample.ui.detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import app.starzero.navbuilder.nav
import kotlin.random.Random

private const val Route = "detail"
private const val ArgLang = "lang"
private const val ArgJa = "ja"
private const val ArgOption1 = "option1"
private const val ArgOption2 = "option2"

private val navDetail = nav(Route) {
    arg(ArgLang) {
        type = NavType.StringType
    }
    arg(ArgJa) {
        type = NavType.StringType
    }
    arg(ArgOption1, optional = true) {
        type = NavType.StringType
        nullable = true
    }
    arg(ArgOption2, optional = true) {
        type = NavType.IntType
        defaultValue = 0
    }
}

fun NavGraphBuilder.detail(
    navigateBack: () -> Unit
) {
    composable(
        route = navDetail.route,
        arguments = navDetail.navArguments
    ) {
        val viewModel: DetailViewModel = hiltViewModel()
        val args = viewModel.args

        DetailScreen(
            lang = args.lang,
            ja = args.ja,
            option1 = args.option1,
            option2 = args.option2,
            navigateBack = navigateBack
        )
    }
}

fun NavController.navigateToDetail(lang: String, ja: String) {
    val route =if (Random.nextBoolean()) {
        navDetail.generateNavigateRoute(
            ArgLang to lang,
            ArgJa to ja
        )
    } else {
        navDetail.generateNavigateRoute(
            ArgLang to lang,
            ArgJa to ja,
            ArgOption1 to "abc",
            ArgOption2 to 999
        )
    }
    navigate(route)
}

data class DetailArgs(
    val lang: String,
    val ja: String,
    val option1: String?,
    val option2: Int
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        requireNotNull(savedStateHandle[ArgLang]),
        requireNotNull(savedStateHandle[ArgJa]),
        savedStateHandle[ArgOption1],
        requireNotNull(savedStateHandle[ArgOption2])
    )
}
