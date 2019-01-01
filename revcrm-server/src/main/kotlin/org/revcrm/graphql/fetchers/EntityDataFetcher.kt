package org.revcrm.graphql.fetchers

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.revcrm.graphql.APIContext
import org.revcrm.meta.Entity
import org.revcrm.graphql.schema.getOrderBy
import org.revcrm.graphql.schema.getWhere
import xyz.morphia.query.FindOptions

class EntitySearchResults<T>(
    val results: List<T>,
    val meta: EntitySearchMeta
)

class EntitySearchMeta(
    val limit: Int,
    val offset: Int, // Morphia only supports Int values for skip()
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
        val limitArg = environment.getArgument<Int?>("limit")
        val offsetArg = environment.getArgument<Int?>("offset")

        val limit = if (limitArg == null) ctx.defaultResultsLimit else limitArg
        val offset = if (offsetArg == null) 0 else offsetArg

        val qResult = ctx.db.withDB { ds ->
            val q =
                if (where != null)
                    ds.createQuery(klass, where)
                else
                    ds.createQuery(klass)
            val options = FindOptions()
            options.limit(limit)
            options.skip(offset)
            if (orderBy != null) {
                q.order(orderBy)
            }
            Pair(q.asList(options), q.count())
        }

        val (results, totalCount) = qResult

        return EntitySearchResults<Any>(
            results,
            EntitySearchMeta(
                limit = limit,
                offset = offset,
                totalCount = totalCount
            )
        )
    }
}