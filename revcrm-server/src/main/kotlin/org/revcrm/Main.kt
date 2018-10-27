package org.revcrm

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.TextContent
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.revcrm.server.healthCheck
import org.revcrm.server.staticFiles

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8800, module = Application::main).start(wait = true)
}

fun Application.main() {

    install(StatusPages) {
        exception<Throwable> { cause ->
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
        }
        status(HttpStatusCode.NotFound) {
            call.respond(HttpStatusCode.NotFound, "Not Found")
        }
    }

    routing {
        staticFiles()
        healthCheck()
    }
}