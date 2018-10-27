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
import org.koin.ktor.ext.installKoin
import org.koin.log.Logger.SLF4JLogger
import org.revcrm.server.graphQL
import org.revcrm.server.graphiQL
import org.revcrm.server.healthCheck
import org.revcrm.server.staticFiles
import java.text.DateFormat

/**
 * To run in intellij, create a new "Application" configuration and
 * set the "Main class" to "io.ktor.server.netty.EngineMain"
 */

@KtorExperimentalLocationsAPI
fun Application.main() {

    installKoin(listOf(revCRMModule), logger = SLF4JLogger())

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