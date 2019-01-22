package org.revcrm.meta.fields

import graphql.Scalars
import graphql.schema.GraphQLType
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService

open class BooleanField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return Scalars.GraphQLBoolean
    }
}

@Suppress("UNUSED_PARAMETER")
fun mapBooleanField(meta: MetadataService, propInfo: EntityPropInfo): Field {
    return BooleanField(propInfo)
}