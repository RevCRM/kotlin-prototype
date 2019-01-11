package org.revcrm.graphql.schema

import graphql.Scalars
import graphql.scalars.ExtendedScalars
import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLArgument
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLTypeReference
import org.revcrm.graphql.fetchers.EntityQueryDataFetcher
import org.revcrm.meta.Entity

fun registerEntityQueryField(
    queryType: GraphQLObjectType.Builder,
    entity: Entity,
    resultsTypeName: String,
    code: GraphQLCodeRegistry.Builder
) {
    queryType.field(
        GraphQLFieldDefinition.newFieldDefinition()
            .name(entity.name)
            .type(GraphQLTypeReference(resultsTypeName))
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
    )
    code.dataFetcher(
        FieldCoordinates.coordinates("Query", entity.name),
        EntityQueryDataFetcher(entity)
    )
}