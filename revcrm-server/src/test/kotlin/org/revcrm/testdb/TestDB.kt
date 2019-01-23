package org.revcrm.testdb

import org.revcrm.config.Config
import org.revcrm.db.DBService

val TEST_DB_CONFIG = Config(
    dbUrl = "127.0.0.1:27017",
    dbName = "revcrm_tests",
    entityClasses = listOf(
        "org.revcrm.testdb.TestFieldsEntity",
        "org.revcrm.testdb.TestEntity2",
        "org.revcrm.testdb.TestConstraintsEntity",
        "org.revcrm.testdb.SensitiveEntity",
        "org.revcrm.testdb.Account",
        "org.revcrm.testdb.TestWithEmbeddedEntity",
        "org.revcrm.testdb.TestWithEmbeddedEntityList",
        "org.revcrm.testdb.TestWithStringList",
        "org.revcrm.testdb.TestWithValidatedDelete"
    ),
    embeddedClasses = listOf(
        "org.revcrm.testdb.TestEmbeddedEntity"
    )
)

object TestDB {
    val instance by lazy { createTestDB() }

    private fun createTestDB(): DBService {
        val dbService = DBService()
        dbService.initialise(TEST_DB_CONFIG)
        return dbService
    }
}