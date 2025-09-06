package com.moneyapp.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.moneyapp.shared.cache.Database
import com.moneyapp.shared.model.Transaction

@Composable
fun App(database: Database) {
    database.populateDatabaseWithPlaceholders()
    val transactions = database.getAllTransactions()

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            LazyColumn {
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = transaction.description, style = MaterialTheme.typography.bodyLarge)
            Text(text = transaction.date, style = MaterialTheme.typography.bodySmall)
        }
        Text(
            text = "${transaction.amount}",
            color = if (transaction.amount < 0) Color.Red else Color.Green,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}