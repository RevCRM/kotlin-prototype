package org.revcrm.data.testmodels

import org.revcrm.data.getDBServiceForEntities

object TestDB {
    val instance by lazy { createTestDB() }

    private fun createTestDB() = getDBServiceForEntities(listOf(
        "org.revcrm.data.testmodels"
    ))
}