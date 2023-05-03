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
package app.starzero.navbuilder.sample.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

val dataList = listOf(
    "Kotlin" to "ことりん",
    "Java" to "じゃゔぁ",
    "Swift" to "すいふと",
    "Dart" to "だーと",
    "Go" to "ごー",
    "Rust" to "らすと",
    "JavaScript" to "じゃゔぁすくりぷと"
)

@Composable
fun ListScreen(
    navigateToDetail: (String, String) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "List")
                }
            )
        }
    ) { padding ->
        ListContent(
            onClickItem = navigateToDetail,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun ListContent(
    onClickItem: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(dataList) { data ->
            ListItem(
                headlineText = {
                    Text(text = data.first)
                },
                modifier = Modifier.clickable {
                    onClickItem(
                        data.first,
                        data.second
                    )
                }
            )
        }
    }
}
