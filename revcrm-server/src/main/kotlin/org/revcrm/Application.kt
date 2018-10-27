package org.revcrm

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.oauth
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
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
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex
import org.koin.ktor.ext.installKoin
import org.koin.log.Logger.SLF4JLogger
import org.revcrm.auth.RevCRMSession
import org.revcrm.auth.googleLogin
import org.revcrm.auth.googleOauthProvider
import org.revcrm.auth.redirectUrl
import org.revcrm.server.graphQL
import org.revcrm.server.graphiQL
import org.revcrm.server.healthCheck
import org.revcrm.server.staticFiles
import java.text.DateFormat

/**
 * To run in intellij, create a new "Application" configuration and
 * set the "Main class" to "io.ktor.server.netty.EngineMain"
 */

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.main() {

    installKoin(listOf(revCRMModule), logger = SLF4JLogger())

    install(DefaultHeaders) {
        header("Server", "RevCRM")
    }
    install(Sessions) {
        cookie<RevCRMSession>("revCRMSessionId") {
            val secretSignKey = hex(System.getenv("REVCRM_COOKIE_SIGN_KEY"))
            transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
        }
    }
    install(Authentication) {
        oauth("google-oauth") {
            client = HttpClient(Apache)
            providerLookup = { googleOauthProvider }
            urlProvider = { redirectUrl("/login-google") }
        }
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
        googleLogin()
        staticFiles()
        graphQL()
        graphiQL()
        healthCheck()
    }
}
