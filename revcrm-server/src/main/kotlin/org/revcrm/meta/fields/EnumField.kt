package org.revcrm.meta.fields

import graphql.schema.GraphQLEnumType
import graphql.schema.GraphQLType
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService

open class EnumField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        val enumClass = Class.forName(jvmType)
        val enumBuilder = GraphQLEnumType.newEnum()
            .name(enumClass.simpleName)
        enumClass.enumConstants.forEach { enumValue ->
            enumBuilder.value(enumValue.toString()) }
        return enumBuilder.build()
    }

    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLType {
        val enumClass = Class.forName(jvmType)
        val enumBuilder = GraphQLEnumType.newEnum()
            .name(enumClass.simpleName + "Input")
        enumClass.enumConstants.forEach { enumValue ->
            enumBuilder.value(enumValue.toString()) }
        return enumBuilder.build()
    }
}

@Suppress("UNUSED_PARAMETER")
fun mapEnumField(meta: MetadataService, propInfo: EntityPropInfo): Field {
    return EnumField(propInfo)
}
