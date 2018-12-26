package org.revcrm.graphql.schema

import graphql.schema.DataFetchingEnvironment

fun getOrderBy(env: DataFetchingEnvironment): String? {
    val orderBySpec = env.getArgument<List<String>>("orderBy")

    if (orderBySpec != null) {
        return orderBySpec.joinToString(separator = ",")
    }
    return null
}