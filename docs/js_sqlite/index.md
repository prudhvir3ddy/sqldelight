# Getting started on Kotlin JS with SQLDelight

{% include 'common/index_gradle_database.md' %}

{% include 'common/index_schema.md' %}

=== "Kotlin"
    ```kotlin
    kotlin {
      sourceSets.jsMain.dependencies {
        implementation("app.cash.sqldelight:sqljs-driver:{{ versions.sqldelight }}")
        implementation(npm("sql.js", "1.6.2"))
        implementation(devNpm("copy-webpack-plugin", "9.1.0"))
      }
    }
    ```
=== "Groovy"
    ```groovy
    kotlin {
      sourceSets.jsMain.dependencies {
        implementation "app.cash.sqldelight:sqljs-driver:{{ versions.sqldelight }}"
        implementation npm("sql.js", "1.6.2")  
        implementation devNpm("copy-webpack-plugin", "9.1.0")
      }
    }
    ```
Unlike on other platforms, the SqlJs driver can not be instantiated directly.
The driver must be loaded asynchronously by calling the `initSqlDriver` function which returns a `Promise<SqlDriver>`.
```kotlin
// As a Promise
val promise: Promise<SqlDriver> = initSqlDriver(Database.Schema)
promise.then { driver -> /* ... */ }

// In a coroutine
suspend fun createDriver() {
    val driver: SqlDriver = initSqlDriver(Database.Schema).await()
    /* ... */
}
```

If building for browsers, some [additional webpack configuration](https://kotlinlang.org/docs/js-project-setup.html#webpack-configuration-file) is also required.
```js
// project/webpack.config.d/fs.js
config.resolve = {
    fallback: {
        fs: false,
        path: false,
        crypto: false,
    }
};

// project/webpack.config.d/wasm.js
const CopyWebpackPlugin = require('copy-webpack-plugin');
config.plugins.push(
    new CopyWebpackPlugin({
        patterns: [
            '../../node_modules/sql.js/dist/sql-wasm.wasm'
        ]
    })
);

```

For [browser testing](https://kotlinlang.org/docs/js-project-setup.html#test-task) with Karma, some similar configuration is required.
```js
// project/karma.config.d/wasm.js
const path = require("path");
const abs = path.resolve("../../node_modules/sql.js/dist/sql-wasm.wasm")

config.files.push({
    pattern: abs,
    served: true,
    watched: false,
    included: false,
    nocache: false,
});

config.proxies["/sql-wasm.wasm"] = `/absolute${abs}`
```

{% include 'common/index_queries.md' %}
