package org.revcrm.data

import graphql.Scalars
import graphql.schema.GraphQLEnumType.newEnum
import graphql.schema.GraphQLOutputType
import graphql.schema.GraphQLScalarType

// TODO: Support customiseable jvmType -> graphQLType mappings

class FieldService {

    private val jvmGraphQLScalarTypeMappings = mutableMapOf<String, GraphQLScalarType>(
        "int" to Scalars.GraphQLInt,
        "float" to Scalars.GraphQLFloat,
        "double" to Scalars.GraphQLFloat,
        "boolean" to Scalars.GraphQLBoolean,
        "java.lang.String" to Scalars.GraphQLString,
        "java.sql.Timestamp" to Scalars.GraphQLString
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