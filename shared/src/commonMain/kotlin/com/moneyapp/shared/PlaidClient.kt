package com.moneyapp.shared

import com.plaid.client.ApiClient
import com.plaid.client.model.CountryCode
import com.plaid.client.model.ItemPublicTokenExchangeRequest
import com.plaid.client.model.LinkTokenCreateRequest
import com.plaid.client.model.LinkTokenCreateRequestUser
import com.plaid.client.model.Products
import com.plaid.client.model.TransactionsGetRequest
import com.plaid.client.request.PlaidApi
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

@Serializable
data class PlaidConfig(
    val clientId: String,
    val secret: String,
    val publicKey: String
)

data class TransactionResponse(
    val transactionId: String,
    val date: LocalDate,
    val amount: Double,
    val name: String
)

class PlaidClient {
    private lateinit var plaidApi: PlaidApi
    private lateinit var config: PlaidConfig

    @OptIn(ExperimentalResourceApi::class)
    suspend fun initialize() {
        val configJson = resource("plaid-config.json").readBytes().decodeToString()
        config = Json.decodeFromString(PlaidConfig.serializer(), configJson)

        val apiClient = ApiClient(mapOf(
            "clientId" to config.clientId,
            "secret" to config.secret
        ))
        apiClient.setPlaidAdapter(ApiClient.Development)
        plaidApi = apiClient.createService(PlaidApi::class.java)
    }

    suspend fun createLinkToken(): String? {
        val request = LinkTokenCreateRequest()
            .clientName("MoneyApp")
            .language("en")
            .countryCodes(listOf(CountryCode.US))
            .user(LinkTokenCreateRequestUser().clientUserId("user-id"))
            .products(listOf(Products.TRANSACTIONS))
            .redirectUri("http://localhost:8080/oauth/redirect")

        val response = plaidApi.linkTokenCreate(request).execute()
        return if (response.isSuccessful) {
            response.body()?.linkToken
        } else {
            null
        }
    }

    suspend fun exchangePublicToken(publicToken: String): String? {
        val request = ItemPublicTokenExchangeRequest().publicToken(publicToken)
        val response = plaidApi.itemPublicTokenExchange(request).execute()
        return if (response.isSuccessful) {
            response.body()?.accessToken
        } else {
            null
        }
    }

    suspend fun getTransactions(accessToken: String): List<TransactionResponse>? {
        val today = Clock.System.now().toLocalDate(TimeZone.UTC)
        val oneYearAgo = today.toInstant(TimeZone.UTC).minus(365, kotlinx.datetime.DateTimeUnit.DAY, TimeZone.UTC).toLocalDate(TimeZone.UTC)

        val request = TransactionsGetRequest()
            .accessToken(accessToken)
            .startDate(oneYearAgo.toJavaLocalDate())
            .endDate(today.toJavaLocalDate())
        val response = plaidApi.transactionsGet(request).execute()
        return if (response.isSuccessful) {
            response.body()?.transactions?.map {
                TransactionResponse(
                    transactionId = it.transactionId,
                    date = it.date.toKotlinLocalDate(),
                    amount = it.amount,
                    name = it.name
                )
            }
        } else {
            null
        }
    }
}
