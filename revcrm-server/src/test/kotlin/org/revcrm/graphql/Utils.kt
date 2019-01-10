package org.revcrm.graphql

import com.google.gson.reflect.TypeToken
import graphql.ExecutionResult
import org.assertj.core.api.Assertions
import org.revcrm.graphql.fetchers.EntitySearchResults
import org.revcrm.testdb.getTestGson

fun <T> mapGraphQLResult(res: ExecutionResult, entityKey: String, resultClass: Class<T>): EntitySearchResults<T> {
    val gson = getTestGson()
    Assertions.assertThat(res.errors).hasSize(0)
    val entityData = res.getData<Map<String, Any>>()
    val tree = gson.toJsonTree(entityData.get(entityKey))
    return gson.fromJson(tree, TypeToken.getParameterized(
        EntitySearchResults::class.java,
        resultClass
    ).type)
}