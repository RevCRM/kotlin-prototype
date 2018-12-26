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

        val where = environment.getArgument<String>("where")
        val results = ctx.db.withDB { ds ->
            ds.createQuery(klass)
                .asList()
        }

        return EntitySearchResults<Any>(
            results,
            EntitySearchMeta(
                totalCount = results.size
            )
        )
    }
}