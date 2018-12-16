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

//        val where = environment.getArgument<String>("where")
//        val results = ctx.db.withTransaction { em ->
//            val builder = em.criteriaBuilder
//            val query = builder.createQuery(klass)
//            query.from(klass)
//            query.distinct(true)
//            em.createQuery(query).resultList
//        }

        return mapOf(
            "results" to null,
            "meta" to mapOf(
                "totalCount" to 1
            )
        )
    }
}