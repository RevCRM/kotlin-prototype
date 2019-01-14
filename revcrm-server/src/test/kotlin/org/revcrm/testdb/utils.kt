
package org.revcrm.testdb

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.revcrm.config.Config
import org.revcrm.db.DBService
import org.bson.types.ObjectId
import org.revcrm.util.objectIdDeserializer

fun getDBServiceForEntities(entityClasses: List<String>, embededClasses: List<String>): DBService {
    val dbService = DBService()
    val config = Config(
        dbUrl = "127.0.0.1:27017",
        dbName = "revcrm_tests",
        entityClasses = entityClasses,
        embeddedClasses = embededClasses
    )
    dbService.initialise(config)
    return dbService
}

fun getTestGson(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(ObjectId::class.java, objectIdDeserializer)
        .create()
}