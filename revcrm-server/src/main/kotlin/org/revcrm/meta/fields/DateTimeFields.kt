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
    override val apiEnabled: Boolean,
    override val properties: Map<String, String>,
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
    override val apiEnabled: Boolean,
    override val properties: Map<String, String>,
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
    override val apiEnabled: Boolean,
    override val properties: Map<String, String>,
    override val constraints: Map<String, String>
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return ExtendedScalars.Time
    }
}

@Suppress("UNUSED_PARAMETER")
fun mapDateTimeField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return DateTimeField(
        name = propInfo.name,
        label = propInfo.label,
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        apiEnabled = propInfo.apiEnabled,
        properties = mapOf(),
        constraints = mapOf()
    )
}

@Suppress("UNUSED_PARAMETER")
fun mapDateField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return DateField(
        name = propInfo.name,
        label = propInfo.label,
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        apiEnabled = propInfo.apiEnabled,
        properties = mapOf(),
        constraints = mapOf()
    )
}

@Suppress("UNUSED_PARAMETER")
fun mapTimeField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return TimeField(
        name = propInfo.name,
        label = propInfo.label,
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        apiEnabled = propInfo.apiEnabled,
        properties = mapOf(),
        constraints = mapOf()
    )
}