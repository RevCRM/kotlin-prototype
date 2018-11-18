package org.revcrm.routes

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import java.io.File

fun Routing.staticFiles() {

    get("/") {
        val html = File("../revcrm-client/dist/index.html").readText()
        call.respondText(html, ContentType.Text.Html)
    }

    static("js") {
        staticRootFolder = File("../revcrm-client/dist")
        files("js")
    }

}