package org.revcrm.meta.fields

import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLOutputType
import graphql.schema.GraphQLTypeReference
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService

open class ReferencedEntityField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLOutputType {
        val relatedEntity = constraints.get("Entity")!!
        return GraphQLTypeReference(relatedEntity)
    }
    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLInputType {
        val relatedEntity = constraints.get("Entity")!! + "Reference"
        return GraphQLTypeReference(relatedEntity)
    }
}

@Suppress("UNUSED_PARAMETER")
fun mapReferencedEntityField(meta: MetadataService, propInfo: EntityPropInfo): Field {
    return ReferencedEntityField(
        propInfo,
        constraints = mapOf(
            "Entity" to propInfo.jvmType.split(".").last()
        )
    )
}