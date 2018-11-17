package org.revcrm.server

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import org.revcrm.util.getResourceAsText

fun Routing.staticFiles() {

    get("/") {
        val html = getResourceAsText("/www/index.html")
        call.respondText(html, ContentType.Text.Html)
    }

    static("static") {
        resources("/www/static")
    }

}