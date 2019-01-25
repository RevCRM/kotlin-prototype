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
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return Scalars.GraphQLString
    }
    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLType {
        return Scalars.GraphQLString
    }
}

open class SelectField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return Scalars.GraphQLString
    }
    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLType {
        return Scalars.GraphQLString
    }
}

@Suppress("UNUSED_PARAMETER")
fun mapStringField(meta: MetadataService, propInfo: EntityPropInfo): Field {
    val properties = mutableMapOf<String, String>()
    val constraints = mutableMapOf<String, String>()

    val selectionList = propInfo.property.findAnnotation<SelectionList>()
    if (selectionList != null) {
        constraints.put("SelectionList", selectionList.code)
        return SelectField(
            propInfo,
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
            propInfo,
            properties = properties,
            constraints = constraints
        )
    }
}