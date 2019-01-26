package org.revcrm.meta.fields

import graphql.Scalars
import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLList
import graphql.schema.GraphQLOutputType
import graphql.schema.GraphQLType
import graphql.schema.GraphQLTypeReference
import java.lang.reflect.ParameterizedType
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService
import kotlin.reflect.jvm.javaField

open class StringListField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLOutputType {
        return GraphQLList(Scalars.GraphQLString)
    }
    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLInputType {
        return GraphQLList(Scalars.GraphQLString)
    }
}

open class EmbeddedEntityListField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLOutputType {
        val relatedEntity = constraints.get("Entity")!!
        return GraphQLList(GraphQLTypeReference(relatedEntity))
    }
    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLInputType {
        val relatedEntity = constraints.get("Entity")!! + "Input"
        return GraphQLList(GraphQLTypeReference(relatedEntity))
    }
}

@Suppress("UNUSED_PARAMETER")
fun mapListField(meta: MetadataService, propInfo: EntityPropInfo): Field {
    val type = propInfo.property.javaField!!.genericType
    val typeDesc = type.toString()
    if (type is ParameterizedType && type.rawType.typeName == "java.util.List") {
        val typeArgs = type.actualTypeArguments
        if (typeArgs.size == 1) {
            val listTypeName = typeArgs[0].typeName
            if (listTypeName == "java.lang.String") {
                return StringListField(propInfo)
            } else {
                return EmbeddedEntityListField(
                    propInfo,
                    constraints = mapOf(
                        "Entity" to listTypeName.split(".").last()
                    )
                )
            }
        }
    }
    throw Error("Could not map List field ${propInfo.klass.simpleName}.${propInfo.name}, type=$typeDesc")
}