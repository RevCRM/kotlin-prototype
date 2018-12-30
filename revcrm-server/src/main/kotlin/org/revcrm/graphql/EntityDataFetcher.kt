package org.revcrm.graphql

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.revcrm.meta.Entity
import org.revcrm.graphql.schema.getOrderBy
import org.revcrm.graphql.schema.getWhere
import xyz.morphia.query.FindOptions

class EntitySearchResults<T>(
    val results: List<T>,
    val meta: EntitySearchMeta
)

class EntitySearchMeta(
    val totalCount: Long
)

class EntityDataFetcher(
    private val entity: Entity
) : DataFetcher<Any> {

    override fun get(environment: DataFetchingEnvironment): Any {
        val ctx = environment.getContext<APIContext>()
        val klass = Class.forName(entity.className)

        val where = getWhere(environment)
        val orderBy = getOrderBy(environment)
        val limit = environment.getArgument<Int?>("limit")
        val offset = environment.getArgument<Int?>("offset")

        val qResult = ctx.db.withDB { ds ->
            val q =
                if (where != null)
                    ds.createQuery(klass, where)
                else
                    ds.createQuery(klass)
            val options = FindOptions()
            options.limit(
                if (limit == null) ctx.defaultResultsLimit else limit
            )
            options.skip(
                if (offset == null) 0 else offset
            )
            if (orderBy != null) {
                q.order(orderBy)
            }
            Pair(q.asList(options), q.count())
        }

        val (results, totalCount) = qResult

        return EntitySearchResults<Any>(
            results,
            EntitySearchMeta(
                totalCount = totalCount
            )
        )
    }
}