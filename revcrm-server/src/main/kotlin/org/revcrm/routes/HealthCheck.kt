package org.revcrm.routes

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get

fun Routing.healthCheck() {
    get("/ping") {
        call.respondText("OK", ContentType.Text.Plain)
    }
    get("/healthcheck") {
        call.respondText("OK", ContentType.Text.Plain)
    }
}