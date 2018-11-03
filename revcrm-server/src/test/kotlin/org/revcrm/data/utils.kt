
package org.revcrm.data

import org.hibernate.cfg.Environment

fun getDataForEntities(entityList: List<String>): RevCRMData {
    val data = RevCRMData()
    val dbConfig = mapOf(
        Environment.DRIVER to "org.h2.Driver",
        Environment.URL to "jdbc:h2:mem:revcrmtest;DB_CLOSE_DELAY=-1"
    )
    data.initialise(dbConfig, entityList)
    return data
}