package org.revcrm.graphql.schema

import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLArgument
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLTypeReference
import org.revcrm.graphql.fetchers.EntityDataFetcher
import org.revcrm.meta.Entity

fun registerEntityMutations(
    mutationType: GraphQLObjectType.Builder,
    entity: Entity,
    code: GraphQLCodeRegistry.Builder
) {
    val createMutationName = "create${entity.name}"
    mutationType.field(
        GraphQLFieldDefinition.newFieldDefinition()
            .name("create${entity.name}")
            .type(GraphQLTypeReference(entity.name))
            .argument(
                GraphQLArgument.newArgument()
                    .name("data")
                    .type(GraphQLNonNull(GraphQLTypeReference(entity.name + "Input")))
                    .build())
    )
    code.dataFetcher(
        FieldCoordinates.coordinates("Mutation", createMutationName),
        EntityDataFetcher(entity)
    )
}