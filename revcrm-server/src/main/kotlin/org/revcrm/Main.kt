package org.revcrm

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.revcrm.graphql.RevCRMData
import org.revcrm.server.healthCheck
import org.revcrm.server.staticFiles

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8800, module = Application::main).start(wait = true)
}

fun Application.main() {

    install(CallLogging)
    install(StatusPages) {
        exception<Throwable> { cause ->
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
            throw cause
        }
        status(HttpStatusCode.NotFound) {
            call.respond(HttpStatusCode.NotFound, "Not Found")
        }
    }

    val data = RevCRMData()
    val res = data.query("{hello}")
    println("GraphQL Result: " + res.getData<Any>().toString())

    routing {
        staticFiles()
        healthCheck()
    }
}