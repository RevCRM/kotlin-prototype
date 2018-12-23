
package org.revcrm.data

import org.revcrm.config.Config

fun getDBServiceForEntities(entityPackages: List<String>): DBService {
    val dbService = DBService()
    val config = Config(
        dbUrl = "127.0.0.1:27017",
        dbName = "revcrm_tests",
        entityPackages = entityPackages
    )
    dbService.initialise(config)
    return dbService
}