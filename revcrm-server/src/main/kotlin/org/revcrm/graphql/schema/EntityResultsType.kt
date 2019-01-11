package org.revcrm.graphql.schema

import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLTypeReference
import graphql.schema.PropertyDataFetcher
import org.revcrm.meta.Entity

fun registerEntityResultsType(
    entity: Entity,
    resultsTypeName: String,
    schema: GraphQLSchema.Builder,
    code: GraphQLCodeRegistry.Builder
) {
    schema.additionalType(GraphQLObjectType.newObject()
        .name(resultsTypeName)
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("results")
                .type(GraphQLList.list(GraphQLTypeReference(entity.name)))
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("meta")
                .type(GraphQLTypeReference("ResultsMeta"))
        )
        .build())

    code
        .dataFetcher(
            FieldCoordinates.coordinates(resultsTypeName, "results"),
            PropertyDataFetcher.fetching<Any>("results")
        )
        .dataFetcher(
            FieldCoordinates.coordinates(resultsTypeName, "meta"),
            PropertyDataFetcher.fetching<Any>("meta")
        )
}