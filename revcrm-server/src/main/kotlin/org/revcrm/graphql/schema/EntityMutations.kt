package org.revcrm.graphql.schema

import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLArgument
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLTypeReference
import org.revcrm.graphql.fetchers.EntityCreateDataFetcher
import org.revcrm.graphql.fetchers.EntityUpdateDataFetcher
import org.revcrm.meta.Entity

fun registerEntityMutations(
    mutationType: GraphQLObjectType.Builder,
    entity: Entity,
    schema: GraphQLSchema.Builder,
    code: GraphQLCodeRegistry.Builder
) {

    // Create

    val createMutationName = "create${entity.name}"
    val createResultTypeName = "Create${entity.name}Result"

    registerEntityMutationResultType(entity, createResultTypeName, schema)

    mutationType.field(
        GraphQLFieldDefinition.newFieldDefinition()
            .name(createMutationName)
            .type(GraphQLTypeReference(createResultTypeName))
            .argument(
                GraphQLArgument.newArgument()
                    .name("data")
                    .type(GraphQLNonNull(GraphQLTypeReference(entity.name + "Input")))
                    .build())
    )
    code.dataFetcher(
        FieldCoordinates.coordinates("Mutation", createMutationName),
        EntityCreateDataFetcher(entity)
    )

    // Update

    val updateMutationName = "update${entity.name}"
    val updateResultTypeName = "Update${entity.name}Result"

    registerEntityMutationResultType(entity, updateResultTypeName, schema)

    mutationType.field(
        GraphQLFieldDefinition.newFieldDefinition()
            .name(updateMutationName)
            .type(GraphQLTypeReference(updateResultTypeName))
            .argument(
                GraphQLArgument.newArgument()
                    .name("data")
                    .type(GraphQLNonNull(GraphQLTypeReference(entity.name + "Input")))
                    .build())
    )
    code.dataFetcher(
        FieldCoordinates.coordinates("Mutation", updateMutationName),
        EntityUpdateDataFetcher(entity)
    )
}