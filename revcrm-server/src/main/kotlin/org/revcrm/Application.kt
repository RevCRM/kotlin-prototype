package org.revcrm

import com.auth0.jwt.JWT
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
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
import org.hibernate.cfg.Environment
import org.koin.ktor.ext.inject
import org.koin.ktor.ext.installKoin
import org.koin.log.Logger.SLF4JLogger
import org.revcrm.auth.RevCRMSession
import org.revcrm.auth.googleLogin
import org.revcrm.auth.googleOauthProvider
import org.revcrm.auth.redirectUrl
import org.revcrm.data.DBService
import org.revcrm.graphql.APIService
import org.revcrm.models.AuthType
import org.revcrm.models.RevUser
import org.revcrm.models.RevUserAuth
import org.revcrm.server.graphQL
import org.revcrm.server.graphiQL
import org.revcrm.server.healthCheck
import org.revcrm.server.staticFiles
import org.revcrm.util.makeJwtVerifier
import org.revcrm.util.session
import org.slf4j.LoggerFactory
import java.text.DateFormat
import java.time.LocalDateTime

/**
 * To run in intellij, create a new "Application" configuration:
 * - Set the "Main class" to "io.ktor.server.netty.EngineMain"
 * - Set "Use classpath of module" to "revcrm-server_main"
 * - Create a new .env file (see revcrm.env-example)
 * - Select your .env file on the EnvFile tab
 */

val log = LoggerFactory.getLogger("org.revcrm.main")

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.main() {

    log.info("Starting RevCRM...")

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

    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwksUrl = environment.config.property("jwt.jwksUrl").getString()

    install(Authentication) {
        jwt("jwt") {
            verifier(makeJwtVerifier(jwksUrl), jwtIssuer)
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) {
                    JWTPrincipal(credential.payload)
                }
                else {
                    null
                }
            }
        }
    }
//    install(Authentication) {
//        oauth("google-oauth") {
//            client = HttpClient(Apache)
//            providerLookup = { googleOauthProvider }
//            urlProvider = { redirectUrl("/login-google") }
//        }
//    }
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

    log.info("Loading Configuration...")
    val c = environment.config
    val dbConfig = mapOf(
        Environment.DRIVER to c.property("revcrm.db.driver").getString(),
        Environment.URL to c.property("revcrm.db.url").getString(),
        Environment.USER to c.property("revcrm.db.username").getString(),
        Environment.PASS to c.property("revcrm.db.password").getString(),
        Environment.HBM2DDL_AUTO to "update"
    )
    val entityList = c.property("revcrm.entityList").getList()

    log.info("Initialising Database Connection...")
    val db: DBService by inject()
    db.initialise(dbConfig, entityList)

    log.info("Initialising GraphQL Schema...")

    val schema: APIService by inject()
    schema.initialise()

    log.info("TEMP: Ensuring test user...")
    val adminUser = db.withTransaction { em ->

        var adminUser = em.session.bySimpleNaturalId(RevUser::class.java)
            .load("admin@revcrm.com")

        if (adminUser == null) {
            println("Creating new admin user...")
            adminUser = RevUser(
                first_name = "System",
                last_name = "Administrator",
                email = "admin@revcrm.com",
                last_login = LocalDateTime.now()
            )
            val auth = RevUserAuth(
                user = adminUser,
                auth_type = AuthType.GOOGLE,
                auth_id = "123456"
            )
            em.persist(adminUser)
            em.persist(auth)
        }
        else {
            adminUser.last_login = LocalDateTime.now()
        }
        adminUser
    }
    println("Admin user: " + adminUser.email)

}
