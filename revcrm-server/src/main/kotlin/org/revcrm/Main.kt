package org.revcrm

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.log.Logger.SLF4JLogger
import org.koin.standalone.StandAloneContext.startKoin
import org.revcrm.server.graphQL
import org.revcrm.server.graphiQL
import org.revcrm.server.healthCheck
import org.revcrm.server.staticFiles
import java.text.DateFormat

@KtorExperimentalLocationsAPI
fun main(args: Array<String>) {
    // Init Dependency Injection
    startKoin(listOf(revCRMModule), logger = SLF4JLogger())
    // Start Server
    embeddedServer(Netty, port = 8800, module = Application::main).start(wait = true)
}

@KtorExperimentalLocationsAPI
fun Application.main() {

    install(DefaultHeaders) {
        header("Server", "RevCRM")
    }
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    install(Locations)
    install(StatusPages) {
        exception<Throwable> { cause ->
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
            throw cause
        }
        status(HttpStatusCode.NotFound) {
            call.respond(HttpStatusCode.NotFound, "Not Found")
        }
    }

    routing {
        staticFiles()
        graphQL()
        graphiQL()
        healthCheck()
    }
}