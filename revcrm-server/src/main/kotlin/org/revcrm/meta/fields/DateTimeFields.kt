package org.revcrm.meta.fields

import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLOutputType
import org.revcrm.graphql.types.GraphQLDate
import org.revcrm.graphql.types.GraphQLDateTime
import org.revcrm.graphql.types.GraphQLTime
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService

open class DateTimeField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLOutputType {
        return GraphQLDateTime
    }
    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLInputType {
        return GraphQLDateTime
    }
}

open class DateField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLOutputType {
        return GraphQLDate
    }
    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLInputType {
        return GraphQLDate
    }
}

open class TimeField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLOutputType {
        return GraphQLTime
    }
    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLInputType {
        return GraphQLTime
    }
}

@Suppress("UNUSED_PARAMETER")
fun mapDateTimeField(meta: MetadataService, propInfo: EntityPropInfo): Field {
    return DateTimeField(propInfo)
}

@Suppress("UNUSED_PARAMETER")
fun mapDateField(meta: MetadataService, propInfo: EntityPropInfo): Field {
    return DateField(propInfo)
}

@Suppress("UNUSED_PARAMETER")
fun mapTimeField(meta: MetadataService, propInfo: EntityPropInfo): Field {
    return TimeField(propInfo)
}