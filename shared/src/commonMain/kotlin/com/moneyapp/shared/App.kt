package com.moneyapp.shared

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.moneyapp.shared.cache.Database
import com.moneyapp.shared.model.Transaction
import com.moneyapp.shared.model.TransactionCategory

@Composable
fun App(database: Database) {
    var transactions by remember { mutableStateOf(emptyList<Transaction>()) }

    LaunchedEffect(Unit) {
        transactions = database.getAllTransactions()
        if (transactions.isEmpty()) {
            val sampleTransactions = listOf(
                Transaction("1", "2025-09-04", "Grocery Store", -75.50, TransactionCategory.GROCERY),
                Transaction("2", "2025-09-04", "Electric Bill", -120.00, TransactionCategory.UTILITIES),
                Transaction("3", "2025-09-03", "Paycheck", 2500.00, TransactionCategory.INCOME),
                Transaction("4", "2025-09-03", "Gas Station", -45.25, TransactionCategory.TRANSPORTATION),
                Transaction("5", "2025-09-02", "Movie Tickets", -30.00, TransactionCategory.ENTERTAINMENT),
                Transaction("6", "2025-09-01", "Rent", -1200.00, TransactionCategory.HOUSING),
                Transaction("7", "2025-08-30", "Doctor's Visit", -150.00, TransactionCategory.HEALTHCARE),
                Transaction("8", "2025-08-28", "Bookstore", -25.75, TransactionCategory.MISC)
            )
            database.insertTransactions(sampleTransactions)
            transactions = database.getAllTransactions()
        }
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Recent Transactions", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                TransactionList(transactions = transactions)
            }
        }
    }
}

@Composable
fun TransactionList(transactions: List<Transaction>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(transactions) { transaction ->
            TransactionRow(transaction)
        }
    }
}

@Composable
fun TransactionRow(transaction: Transaction) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.description, style = MaterialTheme.typography.bodyLarge)
                Text(transaction.category.name, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(
                text = if (transaction.amount < 0) "-$%.2f".format(-transaction.amount) else "$%.2f".format(transaction.amount),
                color = if (transaction.amount < 0) Color.Red else Color.Green,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
