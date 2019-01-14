package org.revcrm.graphql.fetchers

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.revcrm.db.EntityValidationData
import org.revcrm.db.EntityValidationError
import org.revcrm.graphql.APIContext
import org.revcrm.meta.Entity
import org.revcrm.util.getGson

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

        var validationData: EntityValidationData? = null

        try {
            ctx.db.withDB { ds ->
                ds.save(record)
            }
        } catch (e: EntityValidationError) {
            validationData = e.errorData
        }

        if (validationData == null) {
            return EntityMutationResult(
                result = record,
                validation = EntityValidationData()
            )
        } else {
            return EntityMutationResult(
                result = null,
                validation = validationData
            )
        }
    }
}