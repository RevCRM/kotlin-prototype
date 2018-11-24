
package org.revcrm.data

import org.hibernate.cfg.Environment
import org.hibernate.tool.hbm2ddl.SchemaExport
import org.hibernate.tool.schema.TargetType
import java.util.EnumSet

fun getDataForEntities(entityList: List<String>): DBService {
    val dbService = DBService()
    val dbConfig = mapOf(
        Environment.DRIVER to "org.h2.Driver",
        Environment.URL to "jdbc:h2:mem:revcrmtest;DB_CLOSE_DELAY=-1",
        Environment.FORMAT_SQL to "true"
    )
    dbService.initialise(dbConfig, entityList)
    return dbService
}

fun recreateSchema(data: DBService) {
    val schemaExport = SchemaExport()
    schemaExport.create(EnumSet.of(TargetType.DATABASE), data.getMetadata())
}