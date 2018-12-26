package org.revcrm.graphql.schema

import graphql.Scalars
import graphql.schema.GraphQLArgument
import graphql.schema.GraphQLEnumType
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLOutputType
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLSchema
import graphql.schema.PropertyDataFetcher
import org.revcrm.data.CRMMetadata
import org.revcrm.data.FieldMetadata
import org.revcrm.graphql.EntityDataFetcher
import org.revcrm.graphql.GraphQLObjectID

class APISchema(val meta: CRMMetadata) {

    val jvmGraphQLScalarTypeMappings = mutableMapOf<String, GraphQLScalarType>(
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
            val enumBuilder = GraphQLEnumType.newEnum()
                .name(enumClass.simpleName)
            enumClass.enumConstants.forEach { enumValue ->
                enumBuilder.value(enumValue.toString()) }
            return enumBuilder.build()
        }
        return jvmGraphQLScalarTypeMappings.get(field.jvmType)
    }

    fun build(): GraphQLSchema {
        val queryType = GraphQLObjectType.newObject()
            .name("Query")

        val metaType = GraphQLObjectType.newObject()
            .name("ResultsMeta")
            .field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name("totalCount")
                    .type(Scalars.GraphQLInt)
                    .dataFetcher(PropertyDataFetcher.fetching<Any>("totalCount"))
            )
            .build()

        meta.entities.forEach { _, entity ->

            if (!entity.apiEnabled) return@forEach

            val entityType = buildEntityObjectType(this, entity)

            val entityResultsType = GraphQLObjectType.newObject()
                .name(entity.name + "Results")
                .field(
                    GraphQLFieldDefinition.newFieldDefinition()
                        .name("results")
                        .type(GraphQLList.list(entityType))
                        .dataFetcher(PropertyDataFetcher.fetching<Any>("results"))
                )
                .field(
                    GraphQLFieldDefinition.newFieldDefinition()
                        .name("meta")
                        .type(metaType)
                        .dataFetcher(PropertyDataFetcher.fetching<Any>("meta"))
                )
                .build()

            queryType.field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name(entity.name)
                    .type(entityResultsType)
                    .argument(
                        GraphQLArgument.newArgument()
                            .name("where")
                            .type(Scalars.GraphQLString)
                            .build())
                    .dataFetcher(EntityDataFetcher(entity))
            )
        }

        return GraphQLSchema(queryType.build())
    }
}