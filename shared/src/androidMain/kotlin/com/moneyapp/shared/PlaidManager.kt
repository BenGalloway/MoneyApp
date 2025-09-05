package com.moneyapp.shared

import com.plaid.client.model.CountryCode
import com.plaid.client.model.LinkTokenCreateRequest
import com.plaid.client.model.LinkTokenCreateRequestUser
import com.plaid.client.model.Products
import com.plaid.client.PlaidClient
import retrofit2.Response
import com.plaid.client.model.ItemPublicTokenExchangeRequest

actual class PlaidManager {

    // TODO: Replace with your actual Plaid API keys.
    // These are sandbox keys and will only work in the sandbox environment.
    private val plaidClient: PlaidClient = createPlaidClient()

    actual suspend fun createLinkToken(): String? {
        val user = LinkTokenCreateRequestUser().apply { setClientUserId("user-id") } // A unique ID for the user
        val request = LinkTokenCreateRequest().apply {
            setUser(user)
            setClientName("MoneyApp")
            setProducts(listOf(Products.TRANSACTIONS))
            setCountryCodes(listOf(CountryCode.US))
            setLanguage("en")
            redirectUri = "https://oauth.plaid.com/oauth-redirect"
        }

        return try {
            val response: Response<com.plaid.client.model.LinkTokenCreateResponse> = plaidClient.linkTokenCreate(request)
            if (response.isSuccessful) {
                response.body()?.linkToken
            } else {
                // Handle error
                null
            }
        } catch (e: Exception) {
            // Handle exception
            null
        }
    }

    actual suspend fun exchangePublicToken(publicToken: String): String? {
        val request = ItemPublicTokenExchangeRequest().apply { setPublicToken(publicToken) }
        return try {
            val response = plaidClient.itemPublicTokenExchange(request)
            if (response.isSuccessful) {
                response.body()?.accessToken
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

fun createPlaidClient(): PlaidClient {
    return PlaidClient.newBuilder()
        .clientIdAndSecret("66d7301d53c2b300156e2b03", "d13b6c50785b58a36c1333f173e935")
        .sandboxBaseUrl()
        .build()
}