
package org.revcrm.testdb

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bson.types.ObjectId
import org.revcrm.util.objectIdDeserializer

fun getTestGson(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(ObjectId::class.java, objectIdDeserializer)
        .create()
}