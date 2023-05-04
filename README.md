Nav Builder
===

Nav Builder provides a DSL for generating routes and arguments for use with Navigation Compose. It can also generate the route for navigation.

## Usage

First, use the `nav` function to generate a screen transition.

```kt
val nav = nav("sample") {
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
```

Use the generated object to set the route and arguments for the Navigation Compose’s `composable` function.

```kt
composable(
    route = nav.route,
    arguments = nav.navArguments
) {
    // ...
}

```

<details>
<summary>Equivalent code</summary>

This code is equivalent to the following code

```kt
composable(
    route = "sample/{id}?option1={option1}&option2={option2}",
    arguments = listOf(
        navArgument("id") { type = NavType.IntType },
        navArgument("option1") { type = NavType.StringType; nullable = true },
        navArgument("option2") { type = NavType.IntType; defaultValue = 0 },
    )
) {
    // ...
}
```
</details>

To navigate to another screen, pass arguments as key-value pairs and generate the route for the destination.

```kt
val route = nav.generateNavigateRoute(
    "id" to 123,
    "option1" to "abc"
)
navController.navigate(route)
```

## Download

```kt
implementation("app.starzero.navbuilder:navbuilder:1.0.0")
```

## License

```
Copyright 2023 Kenji Abe

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
