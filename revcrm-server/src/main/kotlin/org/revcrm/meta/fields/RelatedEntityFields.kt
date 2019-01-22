package org.revcrm.meta.fields

import graphql.schema.GraphQLType
import graphql.schema.GraphQLTypeReference
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService

open class RelatedEntityField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        val relatedEntity = constraints.get("Entity")!!
        return GraphQLTypeReference(relatedEntity)
    }

    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLType {
        val relatedEntity = constraints.get("Entity")!! + "Input"
        return GraphQLTypeReference(relatedEntity)
    }
}

@Suppress("UNUSED_PARAMETER")
fun mapRelatedEntityField(meta: MetadataService, propInfo: EntityPropInfo, relatedEntity: String): Field {
    return RelatedEntityField(
        propInfo,
        constraints = mapOf(
            "Entity" to relatedEntity
        )
    )
}