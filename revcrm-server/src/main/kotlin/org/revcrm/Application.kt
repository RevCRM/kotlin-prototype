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
import org.hibernate.cfg.Environment
import org.koin.ktor.ext.inject
import org.koin.ktor.ext.installKoin
import org.koin.log.Logger.SLF4JLogger
import org.revcrm.auth.RevPrincipal
import org.revcrm.data.DBService
import org.revcrm.graphql.APIService
import org.revcrm.models.AuthType
import org.revcrm.models.RevUser
import org.revcrm.models.RevUserAuth
import org.revcrm.models.accounts.Account
import org.revcrm.routes.graphQL
import org.revcrm.routes.graphiQL
import org.revcrm.routes.healthCheck
import org.revcrm.routes.staticFiles
import org.revcrm.util.makeJwtVerifier
import org.revcrm.util.randomString
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

    log.info("Loading Configuration...")
    val c = environment.config
    val dbConfig = mapOf(
        Environment.DRIVER to c.property("revcrm.db.driver").getString(),
        Environment.URL to c.property("revcrm.db.url").getString(),
        Environment.USER to c.property("revcrm.db.username").getString(),
        Environment.PASS to c.property("revcrm.db.password").getString(),
        Environment.HBM2DDL_AUTO to "update",
        Environment.FORMAT_SQL to "true"
    )
    val entityList = c.property("revcrm.entityList").getList()

    log.info("Initialising Database Connection...")
    val db: DBService by inject()
    db.initialise(dbConfig, entityList)

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
                }
                else {
                    val auth = db.withTransaction { em ->
                        em.createQuery(
                "from RevUserAuth where auth_type = :type and auth_id = :id")
                            .setParameter("type", AuthType.GOOGLE)
                            .setParameter("id", credential.payload.subject)
                            .setMaxResults(1)
                            .resultList
                    }
                    if (auth.size > 0) {
                        RevPrincipal(credential.payload, auth[0] as RevUserAuth)
                    }
                    else {
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
