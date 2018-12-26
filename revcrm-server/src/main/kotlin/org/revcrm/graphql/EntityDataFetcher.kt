package org.revcrm.graphql

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.revcrm.data.EntityMetadata

class EntityDataFetcher(
    private val entity: EntityMetadata
) : DataFetcher<Any> {

    override fun get(environment: DataFetchingEnvironment): Any {
        val ctx = environment.getContext<APIContext>()
        val klass = Class.forName(entity.className)

        val orderBySpec = environment.getArgument<List<String>>("orderBy")

        val results = ctx.db.withDB { ds ->
            val q = ds.createQuery(klass)
            if (orderBySpec != null) {
                q.order(orderBySpec.joinToString(separator = ","))
            }
            q.asList()
        }

        return EntitySearchResults<Any>(
            results,
            EntitySearchMeta(
                totalCount = results.size
            )
        )
    }
}