package org.revcrm.graphql.fetchers

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.revcrm.graphql.APIContext

class EntityMetadataFetcher : DataFetcher<Any> {

    override fun get(environment: DataFetchingEnvironment): Any {
        val ctx = environment.getContext<APIContext>()

        return ctx.meta.getEntities().filter { it.apiEnabled == true }
    }
}