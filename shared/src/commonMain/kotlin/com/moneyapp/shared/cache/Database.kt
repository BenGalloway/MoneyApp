package com.moneyapp.shared.cache

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.moneyapp.shared.model.Transaction
import com.moneyapp.shared.model.TransactionCategory
import com.moneyapp.shared.model.generatePlaceholderTransactions
import kotlinx.coroutines.flow.Flow

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val categoryAdapter = object : ColumnAdapter<TransactionCategory, String> {
        override fun decode(databaseValue: String): TransactionCategory {
            return TransactionCategory.valueOf(databaseValue)
        }
        override fun encode(value: TransactionCategory): String {
            return value.name
        }
    }

    private val database = AppDatabase(
        driver = databaseDriverFactory.createDriver(),
        TransactionEntityAdapter = TransactionEntity.Adapter(
            categoryAdapter = categoryAdapter
        )
    )

    private val dbQuery = database.appDatabaseQueries

    fun getAllTransactions(): Flow<List<Transaction>> {
        return dbQuery.selectAll().asFlow().mapToList().map { entities ->
            entities.map {
                Transaction(
                    id = it.id,
                    date = it.date,
                    description = it.description,
                    amount = it.amount,
                    category = it.category
                )
            }
        }
    }

    fun populateDatabaseWithPlaceholders() {
        dbQuery.transaction {
            if (dbQuery.selectAll().executeAsList().isEmpty()) {
                insertTransactions(generatePlaceholderTransactions())
            }
        }
    }

    fun insertTransaction(transaction: Transaction) {
        dbQuery.insert(
            id = transaction.id,
            date = transaction.date,
            description = transaction.description,
            amount = transaction.amount,
            category = transaction.category
        )
    }

    fun insertTransactions(transactions: List<Transaction>) {
        dbQuery.transaction {
            transactions.forEach { transaction ->
                insertTransaction(transaction)
            }
        }
    }

    fun clearDatabase() {
        dbQuery.deleteAll()
    }
}