package org.revcrm.meta.fields

import graphql.Scalars
import graphql.schema.GraphQLList
import graphql.schema.GraphQLType
import graphql.schema.GraphQLTypeReference
import java.lang.reflect.ParameterizedType
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService
import kotlin.reflect.jvm.javaField

open class StringListField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val properties: Map<String, String> = mapOf(),
    override val constraints: Map<String, String> = mapOf()
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return GraphQLList(Scalars.GraphQLString)
    }
}

open class RelatedEntityListField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val properties: Map<String, String> = mapOf(),
    override val constraints: Map<String, String> = mapOf(),
    val relatedEntity: String
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return GraphQLTypeReference(relatedEntity)
    }
}

fun mapListField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    val type = propInfo.property.javaField!!.genericType
    val typeDesc = type.toString()
    if (type is ParameterizedType && type.rawType.typeName == "java.util.List") {
        val typeArgs = type.actualTypeArguments
        if (typeArgs.size == 1) {
            val listTypeName = typeArgs[0].typeName
            if (listTypeName == "java.lang.String") {
                return StringListField(
                    name = propInfo.name,
                    label = propInfo.label,
                    jvmType = propInfo.jvmType,
                    nullable = propInfo.nullable
                )
            }
            else {
                return RelatedEntityListField(
                    name = propInfo.name,
                    label = propInfo.label,
                    jvmType = propInfo.jvmType,
                    nullable = propInfo.nullable,
                    relatedEntity = listTypeName.split(".").last()
                )
            }
        }
    }
    throw Error("Could not map List field ${propInfo.klass.simpleName}.${propInfo.name}, type=$typeDesc")
}