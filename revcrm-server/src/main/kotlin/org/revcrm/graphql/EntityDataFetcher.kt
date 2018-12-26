package org.revcrm.graphql

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.revcrm.data.EntityMetadata
import org.revcrm.graphql.schema.getOrderBy
import org.revcrm.graphql.schema.getWhere

class EntityDataFetcher(
    private val entity: EntityMetadata
) : DataFetcher<Any> {

    override fun get(environment: DataFetchingEnvironment): Any {
        val ctx = environment.getContext<APIContext>()
        val klass = Class.forName(entity.className)

        val where = getWhere(environment)
        val orderBy = getOrderBy(environment)

        val results = ctx.db.withDB { ds ->
            val q = if (where != null) ds.createQuery(klass, where) else ds.createQuery(klass)
            if (orderBy != null) {
                q.order(orderBy)
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