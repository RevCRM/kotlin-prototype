
package org.revcrm.testdb

import org.revcrm.config.Config
import org.revcrm.data.DBService
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.bson.types.ObjectId
import java.lang.reflect.Type

fun getDBServiceForEntities(entityPackages: List<String>): DBService {
    val dbService = DBService()
    val config = Config(
        dbUrl = "127.0.0.1:27017",
        dbName = "revcrm_tests",
        entityPackages = entityPackages
    )
    dbService.initialise(config)
    return dbService
}

var objectIdDeserializer: JsonDeserializer<ObjectId> = object : JsonDeserializer<ObjectId> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ObjectId {
        return ObjectId(json.asString)
    }
}