package org.revcrm

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
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
import io.ktor.util.KtorExperimentalAPI
import org.koin.ktor.ext.inject
import org.koin.ktor.ext.installKoin
import org.koin.log.Logger.SLF4JLogger
import org.revcrm.auth.RevPrincipal
import org.revcrm.config.Config
import org.revcrm.db.DBService
import org.revcrm.graphql.APIService
import org.revcrm.entities.AuthType
import org.revcrm.entities.RevUser
import org.revcrm.entities.RevUserAuth
import org.revcrm.routes.graphQL
import org.revcrm.routes.graphiQL
import org.revcrm.routes.healthCheck
import org.revcrm.routes.staticFiles
import org.revcrm.util.makeJwtVerifier
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

    log.info("Loading Configuration...")
    val c = environment.config
    val config = Config(
        dbUrl = c.property("revcrm.db.url").getString(),
        dbName = c.property("revcrm.db.name").getString(),
        entityPackages = c.property("revcrm.entityPackages").getList()
    )

    log.info("Initialising Database Connection...")
    val db: DBService by inject()
    db.initialise(config)

    install(DefaultHeaders) {
        header("Server", "RevCRM")
    }

    val jwtIssuer = c.property("jwt.issuer").getString()
    val jwtAudience = c.property("jwt.audience").getString()
    val jwksUrl = c.property("jwt.jwksUrl").getString()

    install(Authentication) {
        jwt("jwt") {
            verifier(makeJwtVerifier(jwksUrl), jwtIssuer)
            validate { credential ->
                if (!credential.payload.audience.contains(jwtAudience)) {
                    null
                } else {
                    val auth = db.withDB { ds ->
                        ds.createQuery(RevUserAuth::class.java)
                            .field("auth_type").equal(AuthType.GOOGLE)
                            .field("auth_id").equal(credential.payload.subject)
                            .get()
                    }
                    if (auth != null) {
                        RevPrincipal(credential.payload, auth)
                    } else {
                        null
                    }
                }
            }
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
        staticFiles()
        graphQL()
        graphiQL()
        healthCheck()
    }

    log.info("Initialising GraphQL Schema...")

    val schema: APIService by inject()
    schema.initialise()

    log.info("TEMP: Ensuring test user...")
    val adminUser = db.withDB { ds ->

        var adminUser = ds.createQuery(RevUser::class.java)
            .field("email").equalIgnoreCase("admin@revcrm.com")
            .get()

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
            ds.save(adminUser)
            ds.save(auth)
        } else {
            adminUser.last_login = LocalDateTime.now()
            ds.save(adminUser)
        }
        adminUser
    }
    println("Admin user: " + adminUser.email)
}
