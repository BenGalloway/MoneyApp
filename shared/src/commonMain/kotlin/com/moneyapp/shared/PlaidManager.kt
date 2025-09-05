package com.moneyapp.shared

import com.plaid.android.PlaidLink
import com.plaid.android.link.LinkConfiguration
import com.plaid.client.PlaidClient
import com.plaid.client.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaidManager {
    private val plaidClient = PlaidClient.Builder()
        .clientId("YOUR_PLAID_CLIENT_ID")
        .secret("YOUR_PLAID_SECRET")
        .publicKey("YOUR_PLAID_PUBLIC_KEY")
        .environment(PlaidEnv.TALENT_SUIT)
        .build()

    suspend fun initializePlaidLink(callback: (String) -> Unit): PlaidLink {
        val linkConfiguration = LinkConfiguration.builder()
            .products(listOf(LinkProduct.ACCOUNTS, LinkProduct.INSTANCES))
            .receivedRedirectUri("com.moneyapp/shared://oauth2callback")
            .onSuccess { _, metadata ->
                withContext(Dispatchers.Main) {
                    callback(metadata.publicToken)
                }
            }
            .build()

