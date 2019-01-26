package org.revcrm.graphql.fetchers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bson.types.ObjectId
import org.revcrm.db.EntityContext
import org.revcrm.db.WithEntityContext
import java.lang.reflect.Type

// TODO: Deserialisers should be moved to Field classes
var objectIdDeserializer: JsonDeserializer<ObjectId> = object : JsonDeserializer<ObjectId> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ObjectId {
        return ObjectId(json.asString)
    }
}

fun getGson(context: EntityContext): Gson {
    return GsonBuilder()
        .registerTypeAdapterFactory(GsonEntityContextProvider(context))
        .registerTypeAdapter(ObjectId::class.java, objectIdDeserializer)
        .serializeNulls() // set / read null values (otherwise they are ignored)
        .create()
}

fun getGsonForExistingObject(obj: Any, context: EntityContext): Gson {
    return GsonBuilder()
        .registerTypeAdapterFactory(GsonEntityContextProvider(context))
        .registerTypeAdapter(obj::class.java, ExistingObjectTypeAdapter(obj))
        .registerTypeAdapter(ObjectId::class.java, objectIdDeserializer)
        .serializeNulls() // set / read null values (otherwise they are ignored)
        .create()
}

class ExistingObjectTypeAdapter<T : Any> (
    val instance: T
) : InstanceCreator<T> {
    override fun createInstance(type: Type?): T {
        return instance
    }
}

class GsonEntityContextProvider(
    val context: EntityContext
) : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val delegate = gson.getDelegateAdapter(this, type)

        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T) {
                delegate.write(out, value)
            }

            override fun read(`in`: JsonReader): T {
                val obj = delegate.read(`in`)
                if (obj is WithEntityContext) {
                    obj.context = context
                }
                return obj
            }
        }
    }
}