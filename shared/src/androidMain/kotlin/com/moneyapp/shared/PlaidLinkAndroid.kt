package com.moneyapp.shared

import android.content.Context
import com.plaid.link.Plaid
import com.plaid.link.configuration.LinkTokenConfiguration

actual class PlaidLinkHandler(private val context: Context) {
    actual fun openPlaidLink(linkToken: String) {
        val linkTokenConfiguration = LinkTokenConfiguration.Builder()
            .token(linkToken)
            .build()

        Plaid.create(linkTokenConfiguration).open(context)
    }
}