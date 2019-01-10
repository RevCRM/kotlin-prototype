package org.revcrm.meta.fields

import graphql.Scalars
import graphql.schema.GraphQLType
import org.revcrm.annotations.MultiLine
import org.revcrm.annotations.SelectionList
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size
import kotlin.reflect.full.findAnnotation

open class TextField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val properties: Map<String, String>,
    override val constraints: Map<String, String>
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return Scalars.GraphQLString
    }
}

open class SelectField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val properties: Map<String, String>,
    override val constraints: Map<String, String>
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return Scalars.GraphQLString
    }
}

fun mapStringField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    val properties = mutableMapOf<String, String>()
    val constraints = mutableMapOf<String, String>()

    val selectionList = propInfo.property.findAnnotation<SelectionList>()
    if (selectionList != null) {
        constraints.put("SelectionList", selectionList.code)
        return SelectField(
            name = propInfo.name,
            label = propInfo.label,
            jvmType = propInfo.jvmType,
            nullable = propInfo.nullable,
            properties = properties,
            constraints = constraints
        )
    } else {
        if (propInfo.property.findAnnotation<MultiLine>() != null) {
            properties.put("MultiLine", "true")
        }
        if (propInfo.findJavaAnnotation(NotEmpty::class.java) != null) {
            constraints.put("NotEmpty", "true")
        }
        if (propInfo.findJavaAnnotation(NotBlank::class.java) != null) {
            constraints.put("NotBlank", "true")
        }
        val sizeAnnotation = propInfo.findJavaAnnotation(Size::class.java)
        if (sizeAnnotation != null) {
            constraints.put("SizeMin", sizeAnnotation.min.toString())
            constraints.put("SizeMax", sizeAnnotation.max.toString())
        }
        return TextField(
            name = propInfo.name,
            label = propInfo.label,
            jvmType = propInfo.jvmType,
            nullable = propInfo.nullable,
            properties = properties,
            constraints = constraints
        )
    }
}