package org.revcrm.graphql.fetchers

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.bson.types.ObjectId
import org.revcrm.db.EntityError
import org.revcrm.db.EntityValidationData
import org.revcrm.db.EntityValidationError
import org.revcrm.graphql.APIContext
import org.revcrm.meta.Entity
import org.revcrm.util.getGsonForExistingObject

class EntityUpdateDataFetcher(
    private val entity: Entity
) : DataFetcher<Any> {

    override fun get(environment: DataFetchingEnvironment): Any {
        val ctx = environment.getContext<APIContext>()
        val klass = Class.forName(entity.className)

        val data = environment.getArgument<Map<String, Any>>("data")
        val recordId = data.get(entity.idField!!.name)

        if (data == null || recordId == null || !(recordId is String)) {
            val validation = EntityValidationData()
            validation.entityErrors.add(EntityError(
                entity = entity.name, entityPath = "",
                code = "IDMissing",
                message = "cannot update without '${entity.idField!!.name}' field"
            ))
            return EntityMutationResult(null, validation)
        }

        var record = ctx.db.withDB { ds ->
            ds.get(klass, ObjectId(recordId))
        }
        if (record == null) {
            val validation = EntityValidationData()
            validation.entityErrors.add(EntityError(
                entity = entity.name, entityPath = "",
                code = "IDNotFound",
                message = "the specified '${entity.idField!!.name}' was not found"
            ))
            return EntityMutationResult(null, validation)
        }

        val gson = getGsonForExistingObject(record)
        val tree = gson.toJsonTree(data)
        record = gson.fromJson(tree, klass)
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