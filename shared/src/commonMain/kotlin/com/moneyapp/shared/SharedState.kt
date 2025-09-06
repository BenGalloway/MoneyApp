package com.moneyapp.shared

import kotlinx.coroutines.flow.MutableStateFlow

object SharedState {
    val publicToken = MutableStateFlow<String?>(null)
}
