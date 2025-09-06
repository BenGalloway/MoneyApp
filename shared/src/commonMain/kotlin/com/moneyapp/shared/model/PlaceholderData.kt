
package com.moneyapp.shared.model

import com.benasher44.uuid.uuid4

fun generatePlaceholderTransactions(): List<Transaction> {
    return listOf(
        Transaction(uuid4().toString(), "2025-09-01", "Grocery Store", 125.43, TransactionCategory.GROCERY),
        Transaction(uuid4().toString(), "2025-09-01", "Electric Bill", 75.20, TransactionCategory.UTILITIES),
        Transaction(uuid4().toString(), "2025-09-02", "Monthly Rent", 1200.00, TransactionCategory.HOUSING),
        Transaction(uuid4().toString(), "2025-09-03", "Gas Station", 45.50, TransactionCategory.TRANSPORTATION),
        Transaction(uuid4().toString(), "2025-09-04", "Movie Tickets", 28.00, TransactionCategory.ENTERTAINMENT),
        Transaction(uuid4().toString(), "2025-09-05", "Paycheck", 2500.00, TransactionCategory.INCOME),
        Transaction(uuid4().toString(), "2025-09-05", "Pharmacy", 15.75, TransactionCategory.HEALTHCARE),
        Transaction(uuid4().toString(), "2025-09-06", "Coffee Shop", 5.25, TransactionCategory.MISC)
    )
}
