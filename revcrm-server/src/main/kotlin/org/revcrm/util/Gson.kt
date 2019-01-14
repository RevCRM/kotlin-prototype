package org.revcrm.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.bson.types.ObjectId
import java.lang.reflect.Type

var objectIdDeserializer: JsonDeserializer<ObjectId> = object : JsonDeserializer<ObjectId> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ObjectId {
        return ObjectId(json.asString)
    }
}

fun getGson(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(ObjectId::class.java, objectIdDeserializer)
        .create()
}

fun getGsonForExistingObject(obj: Any): Gson {
    val adapter = ExistingObjectTypeAdapter(obj)
    return GsonBuilder()
        .registerTypeAdapter(obj::class.java, adapter)
        .registerTypeAdapter(ObjectId::class.java, objectIdDeserializer)
        .create()
}

class ExistingObjectTypeAdapter<T : Any> (
    val instance: T
) : InstanceCreator<T> {
    override fun createInstance(type: Type?): T {
        return instance
    }
}