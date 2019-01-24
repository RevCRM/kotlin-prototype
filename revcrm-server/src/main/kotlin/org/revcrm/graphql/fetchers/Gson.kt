package org.revcrm.graphql.fetchers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.bson.types.ObjectId
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// TODO: Deserialisers should be moved to Field classes
var objectIdDeserializer: JsonDeserializer<ObjectId> = object : JsonDeserializer<ObjectId> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ObjectId {
        return ObjectId(json.asString)
    }
}
var localDateDeserializer: JsonDeserializer<LocalDate> = object : JsonDeserializer<LocalDate> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDate {
        return LocalDate.parse(json.asString)
    }
}
var localTimeDeserializer: JsonDeserializer<LocalTime> = object : JsonDeserializer<LocalTime> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalTime {
        return LocalTime.parse(json.asString)
    }
}
var localDateTimeDeserializer: JsonDeserializer<LocalDateTime> = object : JsonDeserializer<LocalDateTime> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
        return LocalDateTime.parse(json.asString)
    }
}

fun getGson(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(ObjectId::class.java, objectIdDeserializer)
        .registerTypeAdapter(LocalDate::class.java, localDateDeserializer)
        .registerTypeAdapter(LocalTime::class.java, localTimeDeserializer)
        .registerTypeAdapter(LocalDateTime::class.java, localDateTimeDeserializer)
        .create()
}

fun getGsonForExistingObject(obj: Any): Gson {
    val adapter = ExistingObjectTypeAdapter(obj)
    return GsonBuilder()
        .registerTypeAdapter(obj::class.java, adapter)
        .registerTypeAdapter(LocalDate::class.java, localDateDeserializer)
        .registerTypeAdapter(LocalTime::class.java, localTimeDeserializer)
        .registerTypeAdapter(LocalDateTime::class.java, localDateTimeDeserializer)
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