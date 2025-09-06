package com.moneyapp.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.moneyapp.shared.cache.Database
import com.moneyapp.shared.model.Transaction
import com.moneyapp.shared.model.TransactionCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun App(database: Database) {
    val transactions by database.getAllTransactions().collectAsState(initial = emptyList())
    val plaidClient = remember { PlaidClient() }
    var linkToken by remember { mutableStateOf<String?>(null) }
    val uriHandler = LocalUriHandler.current
    val publicToken by SharedState.publicToken.collectAsState()

    LaunchedEffect(Unit) {
        plaidClient.initialize()
        database.populateDatabaseWithPlaceholders()
    }

    LaunchedEffect(publicToken) {
        publicToken?.let {
            val accessToken = plaidClient.exchangePublicToken(it)
            if (accessToken != null) {
                val plaidTransactions = plaidClient.getTransactions(accessToken)
                if (plaidTransactions != null) {
                    database.clearDatabase()
                    val newTransactions = plaidTransactions.map {
                        Transaction(
                            id = it.transactionId,
                            date = it.date.toString(),
                            amount = it.amount,
                            description = it.name,
                            category = TransactionCategory.OTHER
                        )
                    }
                    database.insertTransactions(newTransactions)
                }
            }
        }
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column {
                Button(onClick = {
                    CoroutineScope(Dispatchers.Default).launch {
                        linkToken = plaidClient.createLinkToken()
                        linkToken?.let {
                            val plaidUrl = "https://cdn.plaid.com/link/v2/stable/link.html?isWebview=true&token=$it"
                            uriHandler.openUri(plaidUrl)
                        }
                    }
                }) {
                    Text("Link Bank Account")
                }
                LazyColumn {
                    items(transactions) { transaction ->
                        TransactionItem(transaction)
                    }
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
