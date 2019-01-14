package org.revcrm.testdb

object TestDB {
    val instance by lazy { createTestDB() }

    private fun createTestDB() = getDBServiceForEntities(
        listOf(
            "org.revcrm.testdb.TestFieldsEntity",
            "org.revcrm.testdb.TestEntity2",
            "org.revcrm.testdb.TestConstraintsEntity",
            "org.revcrm.testdb.SensitiveEntity",
            "org.revcrm.testdb.Account",
            "org.revcrm.testdb.TestWithEmbeddedEntity",
            "org.revcrm.testdb.TestWithStringList",
            "org.revcrm.testdb.TestWithValidatedDelete"
        ),
        listOf(
            "org.revcrm.testdb.TestEmbeddedEntity"
        )
    )
}