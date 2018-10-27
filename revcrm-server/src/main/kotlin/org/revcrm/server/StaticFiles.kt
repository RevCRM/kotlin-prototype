package org.revcrm.server

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get

fun Routing.staticFiles() {
    get("/") {
        call.respondText("Hello World!", ContentType.Text.Plain)
    }
    get("/static") {
        call.respondText("Static assets will go here...", ContentType.Text.Plain)
    }
}