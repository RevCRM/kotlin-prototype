
package org.revcrm.data

import org.hibernate.cfg.Environment
import org.hibernate.tool.hbm2ddl.SchemaExport
import org.hibernate.tool.schema.TargetType
import java.util.EnumSet

fun getDataForEntities(entityList: List<String>): RevCRMData {
    val data = RevCRMData()
    val dbConfig = mapOf(
        Environment.DRIVER to "org.h2.Driver",
        Environment.URL to "jdbc:h2:mem:revcrmtest;DB_CLOSE_DELAY=-1"
    )
    data.initialise(dbConfig, entityList)
    return data
}

fun recreateSchema(data: RevCRMData) {
    val schemaExport = SchemaExport()
    schemaExport.create(EnumSet.of(TargetType.DATABASE), data.metadata)
}