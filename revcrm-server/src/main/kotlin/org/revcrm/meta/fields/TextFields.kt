package org.revcrm.meta.fields

import graphql.Scalars
import graphql.schema.GraphQLType
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

open class TextField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val constraints: Map<String, String>
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return Scalars.GraphQLString
    }
}

fun mapTextField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    val constraints = mutableMapOf<String, String>()
    if (propInfo.findJavaAnnotation(NotEmpty::class.java) != null) {
        constraints.set("NotEmpty", "true")
    }
    if (propInfo.findJavaAnnotation(NotBlank::class.java) != null) {
        constraints.set("NotBlank", "true")
    }
    return TextField(
        name = propInfo.name,
        label = propInfo.label,
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        constraints = constraints
    )
}