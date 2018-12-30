package org.revcrm.graphql

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment

class EntityMetadataFetcher : DataFetcher<Any> {

    override fun get(environment: DataFetchingEnvironment): Any {
//        val ctx = environment.getContext<APIContext>()

        return listOf<Any>()
    }
}