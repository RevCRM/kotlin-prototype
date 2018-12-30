package org.revcrm.meta.fields

import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLType
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService

open class DateTimeField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val constraints: Map<String, String>
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return ExtendedScalars.DateTime
    }
}

open class DateField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val constraints: Map<String, String>
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return ExtendedScalars.Date
    }
}

open class TimeField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val constraints: Map<String, String>
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return ExtendedScalars.Time
    }
}

fun mapDateTimeField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return DateTimeField(
        name = propInfo.name,
        label = propInfo.name, // TODO: Get from @Field annotation
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        constraints = mapOf()
    )
}

fun mapDateField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return DateField(
        name = propInfo.name,
        label = propInfo.name, // TODO: Get from @Field annotation
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        constraints = mapOf()
    )
}

fun mapTimeField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return TimeField(
        name = propInfo.name,
        label = propInfo.name, // TODO: Get from @Field annotation
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        constraints = mapOf()
    )
}