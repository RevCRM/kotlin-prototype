package org.revcrm.graphql.schema

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import graphql.schema.DataFetchingEnvironment

/**
 * Replace graphql JSON keys starting with _ with $ for MongoDB queries
 * (because "$" signifies a variable in GraphQL)
 *
 * e.g. {first_name: { _eq: "Russell" }}
 * becomes {first_name: { $eq: "Russell" }}
 */
private fun convertOperators(obj: MutableMap<Any, Any>) {
    val toAdd = mutableMapOf<String, Any>()
    val toRemove = mutableListOf<String>()
    obj.forEach { key, value ->
        if (key is String) {
            if (key.startsWith("_")) {
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