package app.cash.sqldelight.async.coroutines

import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement
import app.cash.sqldelight.db.SqlSchema

suspend fun <R> SqlDriver.awaitQuery(
  identifier: Int?,
  sql: String,
  mapper: (SqlCursor) -> R,
  parameters: Int,
  binders: (SqlPreparedStatement.() -> Unit)? = null,
): R = executeQuery<R>(identifier, sql, mapper, parameters, binders).await()

suspend fun SqlDriver.await(
  identifier: Int?,
  sql: String,
  parameters: Int,
  binders: (SqlPreparedStatement.() -> Unit)? = null,
): Long = execute(identifier, sql, parameters, binders).await()

suspend fun SqlSchema<*>.awaitCreate(driver: SqlDriver) = create(driver).await()

suspend fun SqlSchema<*>.awaitMigrate(driver: SqlDriver, oldVersion: Int, newVersion: Int) = migrate(driver, oldVersion, newVersion).await()
