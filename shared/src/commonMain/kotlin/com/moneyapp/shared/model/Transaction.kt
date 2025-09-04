package com.moneyapp.shared.model

enum class TransactionCategory {
    GROCERY,
    UTILITIES,
    HOUSING,
    TRANSPORTATION,
    ENTERTAINMENT,
    HEALTHCARE,
    INCOME,
    MISC
}

data class Transaction(
    val id: String,
    val date: String, // Using String for now to avoid dependency issues.
    val description: String,
    val amount: Double,
    val category: TransactionCategory
)
