package org.revcrm.graphql.schema

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import graphql.schema.DataFetchingEnvironment
import org.bson.types.ObjectId

/**
 * Transform an incoming "where" clause as needed so it can be sent to MongoDB:
 *
 * - Replace graphql JSON keys starting with "_" with "$" for MongoDB queries
 *   (because "$" signifies a variable in GraphQL)
 *   e.g. {name: { _eq: "Russell" }} becomes {name: { $eq: "Russell" }}
 *
 * - Replace "id" or "_id" to 24-character string pairs with an ObjectID
 *   reference. (this implementation definitely need improvement!)
 */
internal fun convertOperators(obj: MutableMap<Any, Any>) {
    val toRemove = mutableListOf<String>()
    val toAdd = mutableMapOf<String, Any>()
    obj.forEach { key, value ->
        if (key is String) {
            if ((key == "id" || key == "_id") &&
                value is String && value.length == 24) {
                toRemove.add(key)
                toAdd.put("_id", ObjectId(value))
            } else if (key.startsWith("_")) {
                val newKey = "\$${key.substring(1)}"
                if (obj.containsKey(newKey))
                    throw Error("Cannot translate key '$key' because '$newKey' already exists.")
                else {
                    toAdd.put(newKey, value)
                    toRemove.add(key)
                }
            }
            if (value is MutableMap<*, *>) {
                @Suppress("UNCHECKED_CAST")
                convertOperators(value as MutableMap<Any, Any>)
            } else if (value is List<*>) {
                @Suppress("UNCHECKED_CAST")
                convertOperatorsFromList(value as List<Any>)
            }
        }
    }
    toRemove.forEach { obj.remove(it) }
    toAdd.forEach { key, value -> obj.put(key, value) }
}

private fun convertOperatorsFromList(list: List<Any>) {
    list.forEach { value ->
        if (value is MutableMap<*, *>) {
            @Suppress("UNCHECKED_CAST")
            convertOperators(value as MutableMap<Any, Any>)
        } else if (value is List<*>) {
            @Suppress("UNCHECKED_CAST")
            convertOperatorsFromList(value as List<Any>)
        }
    }
}

fun getWhere(env: DataFetchingEnvironment): DBObject? {
    val whereSpec = env.getArgument<MutableMap<Any, Any>>("where")

    if (whereSpec != null) {
        convertOperators(whereSpec)
        return BasicDBObject(whereSpec.toMap())
    }

    return null
}