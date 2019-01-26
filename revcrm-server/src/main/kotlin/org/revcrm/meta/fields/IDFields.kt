package org.revcrm.meta.fields

import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLOutputType
import org.revcrm.graphql.types.GraphQLObjectID
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService

open class IDField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLOutputType {
        return GraphQLObjectID
    }
    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLInputType {
        return GraphQLObjectID
    }
}

@Suppress("UNUSED_PARAMETER")
fun mapIDField(meta: MetadataService, propInfo: EntityPropInfo): Field {
    return IDField(propInfo)
}