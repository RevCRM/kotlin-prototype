package org.revcrm.graphql.fetchers

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.revcrm.graphql.APIContext
import org.revcrm.meta.Entity

class EntityCreateDataFetcher(
    private val entity: Entity
) : DataFetcher<Any> {

    override fun get(environment: DataFetchingEnvironment): Any {
        val ctx = environment.getContext<APIContext>()
        val klass = Class.forName(entity.className)

        val gson = getGson()
        val data = environment.getArgument<Map<Any, Any>>("data")
        val tree = gson.toJsonTree(data)
        val record = gson.fromJson(tree, klass)

        ctx.db.withDB { ds ->
            ds.save(record)
        }

        return record
    }
}