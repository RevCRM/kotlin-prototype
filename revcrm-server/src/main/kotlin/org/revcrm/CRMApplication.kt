package org.revcrm

import graphql.GraphQL
import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class CRMApplication {

    @Bean
    fun getGraphQLSchema(): GraphQL {
        val queryType = GraphQLObjectType.newObject()
            .name("helloWorldQuery")
            .field(
                GraphQLFieldDefinition.newFieldDefinition()
                .type(GraphQLString)
                .name("hello")
                .staticValue("world"))
            .build()
        val schema = GraphQLSchema.newSchema()
            .query(queryType)
            .build()
        return GraphQL.newGraphQL(schema).build()
    }
}

fun main(args: Array<String>) {
    runApplication<CRMApplication>(*args)
}

// fun Application.main() {
//
//    log.info("Starting RevCRM...")
//    installKoin(listOf(revCRMModule), logger = SLF4JLogger())
//
//    log.info("Loading Configuration...")
//    val c = environment.config
//    val config = DBConfig(
//        dbUrl = c.property("revcrm.db.url").getString(),
//        dbName = c.property("revcrm.db.name").getString(),
//        entityClasses = c.property("revcrm.entityClasses").getList(),
//        embeddedClasses = c.property("revcrm.embeddedClasses").getList()
//    )
//    val data = c.property("revcrm.data").getList()
//
//    log.info("Initialising Database Connection...")
//    val db: DBService by inject()
//    db.initialise(config)
//
//    log.info("Loading Data...")
//    val loader = DataLoader(db)
//    loader.import(data)
//
//    log.info("Initialising Metadata...")
//    val meta: MetadataService by inject()
//    meta.initialise()
//
//    log.info("Initialising GraphQL Schema...")
//    val schema: APIService by inject()
//    schema.initialise()
//
//    install(DefaultHeaders) {
//        header("Server", "RevCRM")
//    }
//
//    val jwtIssuer = c.property("jwt.issuer").getString()
//    val jwtAudience = c.property("jwt.audience").getString()
//    val jwksUrl = c.property("jwt.jwksUrl").getString()
//
//    install(Authentication) {
//        jwt("jwt") {
//            verifier(makeJwtVerifier(jwksUrl), jwtIssuer)
//            validate { credential ->
//                if (!credential.payload.audience.contains(jwtAudience)) {
//                    null
//                } else {
//                    val auth = db.withDB { ds ->
//                        ds.createQuery(RevUserAuth::class.java)
//                            .field("auth_type").equal(AuthType.GOOGLE)
//                            .field("auth_id").equal(credential.payload.subject)
//                            .get()
//                    }
//                    if (auth != null) {
//                        RevPrincipal(credential.payload, auth)
//                    } else {
//                        null
//                    }
//                }
//            }
//        }
//    }
//    install(CallLogging)
//    install(ContentNegotiation) {
//        gson {
//            setDateFormat(DateFormat.LONG)
//            setPrettyPrinting()
//        }
//    }
//    install(Locations)
//    install(StatusPages) {
//        exception<Throwable> { cause ->
//            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
//            throw cause
//        }
//        status(HttpStatusCode.NotFound) {
//            call.respond(HttpStatusCode.NotFound, "Not Found")
//        }
//    }
//
//    routing {
//        staticFiles()
//        graphQL()
//        graphiQL()
//        healthCheck()
//    }
//
//    log.info("TEMP: Ensuring test user...")
//    val adminUser = db.withDB { ds ->
//
//        var adminUser = ds.createQuery(RevUser::class.java)
//            .field("email").equalIgnoreCase("admin@revcrm.com")
//            .get()
//
//        if (adminUser == null) {
//            println("Creating new admin user...")
//            adminUser = RevUser(
//                first_name = "System",
//                last_name = "Administrator",
//                email = "admin@revcrm.com",
//                last_login = LocalDateTime.now()
//            )
//            val auth = RevUserAuth(
//                user = adminUser,
//                auth_type = AuthType.GOOGLE,
//                auth_id = "123456"
//            )
//            ds.save(adminUser)
//            ds.save(auth)
//        } else {
//            adminUser.last_login = LocalDateTime.now()
//            ds.save(adminUser)
//        }
//        adminUser
//    }
//    println("Admin user: " + adminUser.email)
// }