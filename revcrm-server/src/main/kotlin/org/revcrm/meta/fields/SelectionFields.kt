package org.revcrm.meta.fields

import graphql.Scalars
import graphql.schema.GraphQLEnumType
import graphql.schema.GraphQLType
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService

// TODO: Support types other than Enum
open class EnumField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val constraints: Map<String, String> = mapOf()
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        val enumClass = Class.forName(jvmType)
        val enumBuilder = GraphQLEnumType.newEnum()
            .name(enumClass.simpleName)
        enumClass.enumConstants.forEach { enumValue ->
            enumBuilder.value(enumValue.toString()) }
        return enumBuilder.build()
    }
}

fun mapEnumField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return EnumField(
        name = propInfo.name,
        label = propInfo.label,
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable
    )
}

open class BooleanField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val constraints: Map<String, String> = mapOf()
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return Scalars.GraphQLBoolean
    }
}

fun mapBooleanField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return BooleanField(
        name = propInfo.name,
        label = propInfo.label,
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable
    )
}