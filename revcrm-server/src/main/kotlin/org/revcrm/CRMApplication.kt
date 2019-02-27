package org.revcrm

import graphql.GraphQL
import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import org.revcrm.config.DBConfig
import org.revcrm.config.DataConfig
import org.revcrm.data.DataLoader
import org.revcrm.db.DBService
import org.revcrm.graphql.APIService
import org.revcrm.meta.MetadataService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@EnableConfigurationProperties(DBConfig::class, DataConfig::class)
@SpringBootApplication
class CRMApplication : ApplicationRunner {

    private val log = LoggerFactory.getLogger(CRMApplication::class.java)

    @Autowired private lateinit var dbConfig: DBConfig
    @Autowired private lateinit var dbService: DBService
    @Autowired private lateinit var dataConfig: DataConfig
    @Autowired private lateinit var metaService: MetadataService
    @Autowired private lateinit var apiService: APIService

    @Bean
    fun buildGraphQLSchema(): GraphQL {
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

    override fun run(args: ApplicationArguments?) {

        log.info("Initialising Database Connection...")
        dbService.initialise(dbConfig)

        log.info("Loading Data...")
        val loader = DataLoader(dbService)
        loader.import(dataConfig.data)

        log.info("Initialising Metadata...")
        metaService.initialise()

        log.info("Initialising GraphQL Schema...")
        apiService.initialise()
    }
}

fun main(args: Array<String>) {
    runApplication<CRMApplication>(*args)
}

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
