package com.moneyapp.shared

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class PlaidRedirectServer {
    fun start() {
        embeddedServer(Netty, port = 8080) {
            routing {
                get("/oauth/redirect") {
                    val publicToken = call.parameters["public_token"]
                    if (publicToken != null) {
                        SharedState.publicToken.value = publicToken
                    }
                    call.respondText("You can close this window now.")
                }
            }
        }.start(wait = true)
    }
}
