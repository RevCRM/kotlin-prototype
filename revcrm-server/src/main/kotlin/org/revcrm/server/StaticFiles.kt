package org.revcrm.server

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import org.revcrm.util.getResourceAsText

fun Routing.staticFiles() {

    get("/") {
        val html = getResourceAsText("/www/index.html")
        call.respondText(html, ContentType.Text.Html)
    }

    get("/static") {
        call.respondText("Static assets will go here...", ContentType.Text.Plain)
    }

}