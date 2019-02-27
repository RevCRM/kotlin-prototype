package org.revcrm

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

@SpringBootApplication
@EnableConfigurationProperties(DBConfig::class, DataConfig::class)
class CRMApplication : ApplicationRunner {

    private val log = LoggerFactory.getLogger(CRMApplication::class.java)

    @Autowired private lateinit var dbConfig: DBConfig
    @Autowired private lateinit var dbService: DBService
    @Autowired private lateinit var dataConfig: DataConfig
    @Autowired private lateinit var metaService: MetadataService
    @Autowired private lateinit var apiService: APIService

    override fun run(args: ApplicationArguments?) {
        log.info("Loading Data...")
        val loader = DataLoader(dbService)
        loader.import(dataConfig.data)
        log.info("Data Loaded.")
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
