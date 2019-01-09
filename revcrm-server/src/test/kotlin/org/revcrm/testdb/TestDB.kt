package org.revcrm.testdb

object TestDB {
    val instance by lazy { createTestDB() }

    private fun createTestDB() = getDBServiceForEntities(
        listOf(
            "org.revcrm.testdb.TestFieldsEntity",
            "org.revcrm.testdb.TestEntity2",
            "org.revcrm.testdb.TestConstraintsEntity",
            "org.revcrm.testdb.SensitiveEntity",
            "org.revcrm.testdb.Account"
        ),
        listOf()
    )
}