package org.revcrm.graphql.schema

import graphql.Scalars
import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.PropertyDataFetcher

fun registerResultsMetaType(schema: GraphQLSchema.Builder, code: GraphQLCodeRegistry.Builder) {
    schema.additionalType(
        GraphQLObjectType.newObject()
            .name("ResultsMeta")
            .field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name("limit")
                    .type(Scalars.GraphQLInt)
            )
            .field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name("offset")
                    .type(Scalars.GraphQLInt)
            )
            .field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name("totalCount")
                    .type(Scalars.GraphQLLong)
            )
            .build())
    code
        .dataFetcher(
            FieldCoordinates.coordinates("ResultsMeta", "limit"),
            PropertyDataFetcher.fetching<Any>("limit")
        )
        .dataFetcher(
            FieldCoordinates.coordinates("ResultsMeta", "offset"),
            PropertyDataFetcher.fetching<Any>("offset")
        )
        .dataFetcher(
            FieldCoordinates.coordinates("ResultsMeta", "totalCount"),
            PropertyDataFetcher.fetching<Any>("totalCount")
        )
}