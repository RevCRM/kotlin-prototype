
package org.revcrm.testdb

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.revcrm.config.Config
import org.revcrm.db.DBService
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.bson.types.ObjectId
import java.lang.reflect.Type

fun getDBServiceForEntities(entityClasses: List<String>): DBService {
    val dbService = DBService()
    val config = Config(
        dbUrl = "127.0.0.1:27017",
        dbName = "revcrm_tests",
        entityClasses = entityClasses
    )
    dbService.initialise(config)
    return dbService
}

var objectIdDeserializer: JsonDeserializer<ObjectId> = object : JsonDeserializer<ObjectId> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ObjectId {
        return ObjectId(json.asString)
    }
}

fun getTestGson(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(ObjectId::class.java, objectIdDeserializer)
        .create()
}