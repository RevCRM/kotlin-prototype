package org.revcrm.data.testmodels

import org.revcrm.data.getDataForEntities
import org.revcrm.data.recreateSchema

object TestDB {
    val instance by lazy { createTestDB() }

    private fun createTestDB() = getDataForEntities(listOf(
        "org.revcrm.data.testmodels.TestModel",
        "org.revcrm.data.testmodels.TestModel2"
    )).apply {
        recreateSchema(this)
    }
}