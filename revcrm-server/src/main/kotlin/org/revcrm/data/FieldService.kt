package org.revcrm.data

import graphql.Scalars
import graphql.schema.GraphQLEnumType.newEnum
import graphql.schema.GraphQLOutputType
import graphql.schema.GraphQLScalarType
import org.revcrm.graphql.GraphQLObjectID

// TODO: Support customiseable jvmType -> graphQLType mappings

class FieldService {

    private val jvmGraphQLScalarTypeMappings = mutableMapOf<String, GraphQLScalarType>(
        "int" to Scalars.GraphQLInt,
        "short" to Scalars.GraphQLShort,
        "long" to Scalars.GraphQLLong,
        "float" to Scalars.GraphQLFloat,
        "double" to Scalars.GraphQLFloat,
        "boolean" to Scalars.GraphQLBoolean,
        "java.lang.String" to Scalars.GraphQLString,
        "java.time.LocalDate" to Scalars.GraphQLString,
        "java.time.LocalTime" to Scalars.GraphQLInt,
        "java.time.LocalDateTime" to Scalars.GraphQLString,
        "org.bson.types.ObjectId" to GraphQLObjectID
    )

    fun getGraphQLScalarTypeForField(field: FieldMetadata): GraphQLOutputType? {
        if (field.jvmType == "org.hibernate.type.EnumType") {
            val enumClass = Class.forName(field.jvmSubtype)
            val enumBuilder = newEnum()
                .name(enumClass.simpleName)
            enumClass.enumConstants.forEach { enumValue ->
                enumBuilder.value(enumValue.toString()) }
            return enumBuilder.build()
        }
        return jvmGraphQLScalarTypeMappings.get(field.jvmType)
    }
}