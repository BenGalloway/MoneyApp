package com.moneyapp.shared.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:AppDatabase.db")
        if (!File("AppDatabase.db").exists()) {
            AppDatabase.Schema.create(driver)
        }
        return driver
    }
}
