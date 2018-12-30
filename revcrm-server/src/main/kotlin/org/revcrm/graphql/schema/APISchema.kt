package org.revcrm.graphql.schema

import graphql.Scalars
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLArgument
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.PropertyDataFetcher
import org.revcrm.graphql.EntityDataFetcher
import org.revcrm.meta.MetadataService

class APISchema(val meta: MetadataService) {

    fun build(): GraphQLSchema {

        val schema = GraphQLSchema.newSchema()

        schema.additionalType(
            GraphQLObjectType.newObject()
                .name("ResultsMeta")
                .field(
                    GraphQLFieldDefinition.newFieldDefinition()
                        .name("totalCount")
                        .type(Scalars.GraphQLInt)
                        .dataFetcher(PropertyDataFetcher.fetching<Any>("totalCount"))
                )
                .build())

        val queryType = GraphQLObjectType.newObject()
            .name("Query")

        val entities = meta.getEntities()

        entities.forEach { entity ->

            if (!entity.apiEnabled) return@forEach

            schema.additionalType(
                buildEntityObjectType(this, entity))

            val entityResultsType = buildEntityResultsType(this, entity)

            queryType.field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name(entity.name)
                    .type(entityResultsType)
                    .argument(
                        GraphQLArgument.newArgument()
                            .name("where")
                            .type(ExtendedScalars.Json)
                            .build())
                    .argument(
                        GraphQLArgument.newArgument()
                            .name("orderBy")
                            .type(GraphQLList(Scalars.GraphQLString))
                            .build())
                    .argument(
                        GraphQLArgument.newArgument()
                            .name("limit")
                            .type(ExtendedScalars.PositiveInt)
                            .build())
                    .argument(
                        GraphQLArgument.newArgument()
                            .name("offset")
                            .type(ExtendedScalars.NonNegativeInt)
                            .build())
                    .dataFetcher(EntityDataFetcher(entity))
            )
        }
        schema.query(queryType.build())

        return schema.build()
    }
}