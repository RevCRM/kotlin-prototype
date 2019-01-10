package org.revcrm.meta.fields

import graphql.schema.GraphQLType
import graphql.schema.GraphQLTypeReference
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService

open class RelatedEntityField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val apiEnabled: Boolean,
    override val properties: Map<String, String> = mapOf(),
    override val constraints: Map<String, String> = mapOf(),
    val relatedEntity: String
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return GraphQLTypeReference(relatedEntity)
    }
}

fun mapRelatedEntityField(meta: MetadataService, propInfo: EntityPropInfo, relatedEntity: String): IField {
    return RelatedEntityField(
        name = propInfo.name,
        label = propInfo.label,
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        apiEnabled = propInfo.apiEnabled,
        constraints = mapOf(),
        relatedEntity = relatedEntity
    )
}