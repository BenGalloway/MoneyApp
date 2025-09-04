package com.moneyapp.shared.cache

import app.cash.sqldelight.ColumnAdapter
import com.moneyapp.shared.model.Transaction
import com.moneyapp.shared.model.TransactionCategory

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

    fun getAllTransactions(): List<Transaction> {
        return dbQuery.selectAll().executeAsList().map { entity ->
            Transaction(
                id = entity.id,
                date = entity.date,
                description = entity.description,
                amount = entity.amount,
                category = entity.category
            )
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
