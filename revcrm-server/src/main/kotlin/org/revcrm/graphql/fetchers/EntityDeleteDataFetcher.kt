package org.revcrm.graphql.fetchers

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.bson.types.ObjectId
import org.revcrm.db.EntityError
import org.revcrm.db.EntityValidationData
import org.revcrm.db.EntityValidationError
import org.revcrm.db.deleteWithValidation
import org.revcrm.graphql.APIContext
import org.revcrm.meta.Entity

class EntityDeleteDataFetcher(
    private val entity: Entity
) : DataFetcher<Any> {

    override fun get(environment: DataFetchingEnvironment): Any {
        val ctx = environment.getContext<APIContext>()
        val klass = Class.forName(entity.className)

        val recordId = environment.getArgument<String>("id")

        if (recordId == null || !(recordId is String)) {
            throw Error("invalid id type") // should be prevented by graphql type
        }

        var record = ctx.db.withDB { ds ->
            ds.get(klass, ObjectId(recordId))
        }
        if (record == null) {
            val validation = EntityValidationData()
            validation.entityErrors.add(EntityError(
                entity = entity.name, entityPath = "",
                code = "IDNotFound",
                message = "the specified id was not found"
            ))
            return EntityDeleteResult(false, validation)
        }

        var validationData: EntityValidationData? = null

        try {
            ctx.db.withDB { ds ->
                ds.deleteWithValidation(record)
            }
        } catch (e: EntityValidationError) {
            validationData = e.errorData
        }

        if (validationData == null) {
            return EntityDeleteResult(
                result = true,
                validation = EntityValidationData()
            )
        } else {
            return EntityDeleteResult(
                result = false,
                validation = validationData
            )
        }
    }
}