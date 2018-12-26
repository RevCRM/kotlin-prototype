package org.revcrm.testdb

object TestDB {
    val instance by lazy { createTestDB() }

    private fun createTestDB() = getDBServiceForEntities(
        listOf(
            "org.revcrm.data.testmodels"
        )
    )
}